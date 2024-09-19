package com.msa.hub.hubPath.service;

import com.msa.hub.hub.domain.Hub;
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

    // HubPath 등록
    @Transactional
    public HubPathResponseDto registerHubPath(HubPathRequestDto hubPathRequestDto) {
        // 시작 허브와 종료 허브 조회
        Hub startHub = getHubById(hubPathRequestDto.getStartHubId());
        Hub endHub = getHubById(hubPathRequestDto.getEndHubId());

        // HubPath 엔티티 생성
        HubPath hubPath = new HubPath(
                hubPathRequestDto.getHubPathDuration(),
                List.of(),  // 초기 빈 리스트
                startHub,   // 시작 허브
                endHub,     // 종료 허브
                startHub.getHubRank()  // 허브 순서
        );

        // 엔티티 저장
        HubPath savedHubPath = hubPathRepository.save(hubPath);

        // 이전 및 다음 HubPath 설정
        updateOtherHubPaths(savedHubPath);

        return new HubPathResponseDto(savedHubPath);
    }

    // HubPath 수정
    @Transactional
    public HubPathResponseDto updateHubPath(UUID hubPathId, HubPathRequestDto hubPathRequestDto, String user) {
        // 기존 HubPath 조회
        HubPath hubPath = getHubPathById(hubPathId);

        // Hub 정보 조회 및 업데이트
        Hub startHub = null;
        Hub endHub = null;

        if (hubPathRequestDto.getStartHubId() != null) {
            startHub = getHubById(hubPathRequestDto.getStartHubId());
        }
        if (hubPathRequestDto.getEndHubId() != null) {
            endHub = getHubById(hubPathRequestDto.getEndHubId());
        }

        hubPath.update(hubPathRequestDto, startHub, endHub, user);

        // 재귀적 관계 업데이트
        if (hubPathRequestDto.getOtherHubPaths() != null) {
            // UUID 리스트를 String 리스트로 변환
            List<String> otherHubPaths = hubPathRequestDto.getOtherHubPaths().stream()
                    .map(UUID::toString)
                    .collect(Collectors.toList());
            hubPath.setOtherHubPaths(otherHubPaths);
        }

        hubPathRepository.save(hubPath);

        return new HubPathResponseDto(hubPath);
    }

    // HubPath 삭제 (논리적 삭제)
    @Transactional
    public void deleteHubPath(UUID hubPathId) {
        // 삭제할 HubPath 조회
        HubPath hubPath = getHubPathById(hubPathId);

        // 재귀적 관계에서 해당 HubPath를 제거
        removeHubPathFromOtherPaths(hubPath);

        // 실제 삭제 대신 논리적 삭제 처리
        hubPath.delete();  // isDelete = true 설정
        hubPathRepository.save(hubPath);
    }

    // HubPath 상세 조회 (is_delete = false인 항목만)
    public Optional<HubPath> getHubPathDetail(UUID hubPathId) {
        return hubPathRepository.findByHubPathIdAndIsDeleteFalse(hubPathId);
    }

    // 모든 HubPath 조회 (is_delete = false인 항목만)
    @Transactional(readOnly = true)
    public Page<HubPath> getAllHubPaths(Pageable pageable) {
        return hubPathRepository.findByIsDeleteFalse(pageable);
    }

    // 검색 조건 추가
    @Transactional(readOnly = true)
    public Page<HubPath> getFilteredHubPaths(UUID startHub, UUID endHub, Pageable pageable) {
        return hubPathRepository.findByFilters(startHub, endHub, pageable);
    }


    // 시작허브 도착허브
    @Transactional(readOnly = true)
    public List<HubPath> getStartEndPaths(UUID startHubId, UUID endHubId) {
        // 경로를 저장할 리스트
        List<HubPath> hubPaths = new ArrayList<>();

        // 시작 허브에서 경로 탐색 시작
        HubPath currentPath = hubPathRepository.findByStartHub_HubId(startHubId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid start hub ID"));

        // 시작허브와 end허브의 rank비교 시작허브의 rank가 작으면 지금그대로 하고 그렇지 않으면 반대 로직 생성해야함.
        // 경로 탐색 시작
        findPathsRecursively(currentPath, endHubId, hubPaths);

        return hubPaths;
    }

    // 경로 탐색 재귀 메서드
    private void findPathsRecursively(HubPath currentPath, UUID endHubId, List<HubPath> hubPaths) {
        // 현재 경로를 리스트에 추가
        hubPaths.add(currentPath);

        // 현재 경로의 도착 허브가 요청한 도착 허브인지 확인
        if (currentPath.getEndHub().getHubId().equals(endHubId)) {
            return; // 도착 허브에 도달했으므로 탐색 종료
        }

        // 현재 경로의 마감 허브를 시작 허브로 가지는 다른 경로를 탐색
        Optional<HubPath> nextPathOpt = hubPathRepository.findByStartHub_HubId(currentPath.getEndHub().getHubId());

        // nextPath가 존재하고, 이미 방문하지 않은 경로일 경우에만 재귀적으로 탐색
        if (nextPathOpt.isPresent()) {
            HubPath nextPath = nextPathOpt.get();
            if (!hubPaths.contains(nextPath)) {
                findPathsRecursively(nextPath, endHubId, hubPaths);
            }
        }
    }

    // 기타 유틸리티 메서드들

    private Hub getHubById(UUID hubId) {
        return hubRepository.findById(hubId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid hub ID: " + hubId));
    }

    private HubPath getHubPathById(UUID hubPathId) {
        return hubPathRepository.findById(hubPathId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid HubPath ID: " + hubPathId));
    }

    private List<HubPath> getHubPathsByIds(List<UUID> ids) {
        return ids.stream()
                .map(this::getHubPathById)
                .collect(Collectors.toList());
    }

    private void updateOtherHubPaths(HubPath savedHubPath) {
        Integer previousHubRank = savedHubPath.getHubRank() - 1;
        Integer nextHubRank = savedHubPath.getHubRank() + 1;

        List<HubPath> otherHubPaths = new ArrayList<>();

        hubPathRepository.findByHubRank(previousHubRank).ifPresent(otherHubPaths::add);
        hubPathRepository.findByHubRank(nextHubRank).ifPresent(otherHubPaths::add);

        // 다른 HubPath의 ID 리스트로 변환하여 저장
        List<String> otherHubPathIds = otherHubPaths.stream()
                .map(HubPath::getHubPathId)
                .map(UUID::toString)
                .collect(Collectors.toList());

        savedHubPath.setOtherHubPaths(otherHubPathIds);
        hubPathRepository.save(savedHubPath);

        // 이전 허브 순위를 가진 HubPath 조회
        hubPathRepository.findByHubRank(previousHubRank).ifPresent(previousHubPath -> {
            List<String> updatedOtherPaths = new ArrayList<>(previousHubPath.getOtherHubPaths());
            // 현재 경로의 ID가 이전 경로의 otherHubPaths에 없는 경우 추가
            if (!updatedOtherPaths.contains(savedHubPath.getHubPathId().toString())) {
                updatedOtherPaths.add(savedHubPath.getHubPathId().toString());
                previousHubPath.setOtherHubPaths(updatedOtherPaths);
                hubPathRepository.save(previousHubPath);  // 업데이트된 이전 허브 경로 저장
            }
        });
    }

    private void removeHubPathFromOtherPaths(HubPath hubPath) {
        List<String> hubPathIdStrList = hubPath.getOtherHubPaths();

        hubPathIdStrList.forEach(otherHubPathIdStr -> {
            HubPath otherHubPath = hubPathRepository.findById(UUID.fromString(otherHubPathIdStr))
                    .orElseThrow(() -> new IllegalArgumentException("Invalid HubPath ID: " + otherHubPathIdStr));

            List<String> updatedOtherPaths = new ArrayList<>(otherHubPath.getOtherHubPaths());
            updatedOtherPaths.remove(hubPath.getHubPathId().toString());
            otherHubPath.setOtherHubPaths(updatedOtherPaths);
            hubPathRepository.save(otherHubPath);
        });
    }
}
