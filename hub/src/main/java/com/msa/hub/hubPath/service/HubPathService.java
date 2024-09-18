package com.msa.hub.hubPath.service;

import com.msa.hub.hub.domain.Hub;
import com.msa.hub.hub.dto.HubRequestDto;
import com.msa.hub.hub.dto.HubResponseDto;
import com.msa.hub.hub.repository.HubRepository;
import com.msa.hub.hubPath.domain.HubPath;
import com.msa.hub.hubPath.dto.HubPathRequestDto;
import com.msa.hub.hubPath.dto.HubPathResponseDto;
import com.msa.hub.hubPath.repository.HubPathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HubPathService {

    private final HubPathRepository hubPathRepository;
    private final HubRepository hubRepository;

    // 마스터 관리자 또는 스케줄러만 가능
    @Transactional
    public HubPathResponseDto registerHubPath(HubPathRequestDto hubPathRequestDto) {

        // 시작 허브와 종료 허브 조회
        Hub startHub = hubRepository.findById(hubPathRequestDto.getStartHubId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid start hub ID"));

        Hub endHub = hubRepository.findById(hubPathRequestDto.getEndHubId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid end hub ID"));

        // 시작 허브의 hubRank 가져오기
        Integer hubRank = startHub.getHubRank();  // 또는 endHub.getHubRank()로 설정할 수 있음
        // HubPath 엔티티 생성
        HubPath hubPath = new HubPath(
                hubPathRequestDto.getHubPathDuration(),  // 소요 시간
                List.of(),                               // 빈 리스트로 생성
                startHub,                                // 시작 허브
                endHub,                                  // 종료 허브
                hubRank           // 허브 순서
        );

        // 엔티티 저장
        HubPath savedHubPath = hubPathRepository.save(hubPath);

        // Step 2: 다른 허브 경로들 추가 (재귀적 관계)
        List<HubPath> otherHubPaths = hubPathRequestDto.getOtherHubPaths().stream()
                .map(id -> hubPathRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid HubPath ID: " + id)))
                .collect(Collectors.toList());

        // 생성된 HubPath에 재귀적 관계로 다른 허브 경로 추가
        savedHubPath.setOtherHubPaths(otherHubPaths);

        // Step 3: 다른 허브 경로에서 내 UUID가 없는지 확인하고 업데이트
        otherHubPaths.stream()
                .filter(otherHubPath -> !otherHubPath.getOtherHubPaths().contains(savedHubPath))  // 내 UUID가 있는지 확인
                .forEach(otherHubPath -> {
                    List<HubPath> updatedOtherHubPaths = new ArrayList<>(otherHubPath.getOtherHubPaths());
                    updatedOtherHubPaths.add(savedHubPath);  // 내 UUID를 추가
                    otherHubPath.setOtherHubPaths(updatedOtherHubPaths);
                    hubPathRepository.save(otherHubPath);  // 업데이트된 다른 허브 경로 저장
                });

        // 업데이트된 HubPath 저장
        hubPathRepository.save(savedHubPath);

        return new HubPathResponseDto(hubPath);
    }

    // HubPath 수정
    // 마스터 관리자 또는 스케줄러만 가능
    @Transactional
    public HubPathResponseDto updateHubPath(UUID hubPathId, HubPathRequestDto hubPathRequestDto, String user, String userRole) {
        // 기존 HubPath 조회
        HubPath hubPath = hubPathRepository.findById(hubPathId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid HubPath ID: " + hubPathId));

        // HubRepository를 사용해 Hub 정보 조회
        Hub startHub = hubRepository.findById(hubPathRequestDto.getStartHubId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid start hub ID"));

        Hub endHub = hubRepository.findById(hubPathRequestDto.getEndHubId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid end hub ID"));

        // 엔티티의 update 메서드에 필요한 값들을 전달
        hubPath.update(hubPathRequestDto, startHub, endHub, user);

        // 재귀적 관계 업데이트
        List<HubPath> otherHubPaths = hubPathRequestDto.getOtherHubPaths().stream()
                .map(id -> hubPathRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid HubPath ID: " + id)))
                .collect(Collectors.toList());

        hubPath.setOtherHubPaths(otherHubPaths);

        // 변경 사항 저장
        hubPathRepository.save(hubPath);

        return new HubPathResponseDto(hubPath);
    }


    // HubPath 삭제 (is_delete = true 로 설정)
    // 마스터 관리자 또는 스케줄러만 가능
    @Transactional
    public void deleteHubPath(UUID hubPathId) {
        // 삭제할 HubPath 조회
        HubPath hubPath = hubPathRepository.findById(hubPathId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid HubPath ID: " + hubPathId));

        // 재귀적 관계에서 해당 HubPath를 제거
        hubPath.getOtherHubPaths().forEach(otherHubPath -> {
            otherHubPath.getOtherHubPaths().remove(hubPath);
            hubPathRepository.save(otherHubPath);
        });

        // 실제 삭제 대신 논리적 삭제 처리
        hubPath.delete();  // isDelete = true 설정

        // 변경 사항 저장
        hubPathRepository.save(hubPath);
    }



    // HubPath 상세 조회 (is_delete = false인 항목만)
    // 로그인 사용자 가능
    public Optional<HubPath> getHubPathDetail(UUID hubPathId) {
        return hubPathRepository.findByHubPathIdAndIsDeleteFalse(hubPathId);
    }

    // 모든 HubPath 조회 (is_delete = false인 항목만)
    // is_delete = false인 항목만 페이지네이션과 정렬을 포함하여 조회
    // 로그인 사용자 가능
    @Transactional(readOnly = true)
    public Page<HubPath> getAllHubPaths(Pageable pageable) {
        return hubPathRepository.findByIsDeleteFalse(pageable);
    }

    // 검색 조건 추가
    // 로그인 사용자 가능
    @Transactional(readOnly = true)
    public Page<HubPath> getFilteredHubPaths(String startHub, String endHub, Pageable pageable) {
        return hubPathRepository.findByFilters(startHub, endHub, pageable);
    }

    // 시작허브 도착허브
    @Transactional(readOnly = true)
    public List<HubPath> getStardEndPaths(UUID  startHubId, UUID  endHubId) {
        // 경로를 저장할 리스트
        List<HubPath> hubPaths = new ArrayList<>();

        // 시작 허브에서 경로 탐색 시작
        HubPath currentPath = hubPathRepository.findByStartHub_HubId(startHubId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid start hub ID"));

        // 경로 탐색 시작
        findPathsRecursively(currentPath, endHubId, hubPaths);

        return hubPaths;
    }

    private void findPathsRecursively(HubPath currentPath, UUID endHubId, List<HubPath> hubPaths) {
        // 현재 경로를 리스트에 추가
        hubPaths.add(currentPath);

        // 현재 경로의 도착 허브가 요청한 도착 허브인지 확인
        if (currentPath.getEndHub().getHubId().equals(endHubId)) {
            return; // 도착 허브에 도달했으므로 탐색 종료
        }

        // 다음 경로들을 재귀적으로 탐색
        for (HubPath nextPath : currentPath.getOtherHubPaths()) {
            if (!hubPaths.contains(nextPath)) { // 이미 방문한 경로는 제외
                findPathsRecursively(nextPath, endHubId, hubPaths);
            }
        }
    }

}
