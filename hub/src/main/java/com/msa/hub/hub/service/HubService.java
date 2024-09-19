package com.msa.hub.hub.service;

import com.msa.hub.hub.domain.Hub;
import com.msa.hub.hub.dto.HubRequestDto;
import com.msa.hub.hub.dto.HubResponseDto;
import com.msa.hub.hub.repository.HubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubService {

    private final HubRepository hubRepository;

    @Transactional
    public HubResponseDto registerHub(HubRequestDto hubRequestDto) {
        // hubRank가 0이거나 null이면 가장 큰 hubRank에 1을 더해 설정
        Integer hubRank = hubRequestDto.getHubRank();
        if (hubRank == null || hubRank == 0) {
            hubRank = hubRepository.findMaxHubRank() + 1;
        } else {
            // hubRank가 0이 아닌 값이 들어오면, 해당 값 이상인 모든 hubRank를 1씩 증가시킴
            hubRepository.incrementHubRankGreaterThanOrEqual(hubRank);
        }

        // Hub 엔티티 생성
        Hub hub = new Hub(
                hubRequestDto.getHubName(),
                hubRequestDto.getHubAddress(),
                hubRequestDto.getHubLatitude(),
                hubRequestDto.getHubLongitude(),
                hubRank
        );

        // 엔티티 저장
        hubRepository.save(hub);

        return new HubResponseDto(hub);
    }

    // Hub 수정
    // Hub Path 에서 is_delete가  false 이고 rank 가 같거나 높은거 찾아서 전부 +1 해줘야함
    @Transactional
    public HubResponseDto updateHub(UUID hubId, HubRequestDto hubRequestDto, String user, String userRole) {
        // 해당 Hub가 DB에 존재하는지 확인
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Hub가 존재하지 않습니다."));

        // 관리자만 수정 가능 (예시로 userRole을 검사)
        if (!userRole.equals("MASTER")) {
            throw new IllegalArgumentException("해당 Hub를 수정할 권한이 없습니다.");
        }

        Integer hubRank = hubRequestDto.getHubRank();
        if (hubRank != null && hubRank > 0) {
            // hubRank가 0이 아닌 값이 들어오면, 해당 값 이상인 모든 hubRank를 1씩 증가시킴
            hubRepository.incrementHubRankGreaterThanOrEqual(hubRank);
        }

        // Hub 정보 업데이트
        hub.update(hubRequestDto, user);


        // 수정된 Hub 데이터를 반환
        return new HubResponseDto(hub);
    }

    // Hub 삭제 (is_delete = true 로 설정)
    // Hub Path 에서 is_delete가  false 인걸 찾아서 있으면 삭제 못함
    @Transactional
    public void deleteHub(UUID hubId) {
        // 해당 Hub가 DB에 존재하는지 확인
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Hub가 존재하지 않습니다."));

        // 삭제 대상 hubRank 값 저장
        Integer hubRankToDelete = hub.getHubRank();

        // 소프트 삭제 처리 (isDelete = true)
        hub.delete(true);
        hubRepository.save(hub);

        // 삭제된 hubRank보다 큰 값들의 hubRank를 1씩 감소시킴
        hubRepository.decrementHubRankGreaterThan(hubRankToDelete);
    }

    // Hub 상세 조회 (is_delete = false인 항목만)
    public Optional<HubResponseDto> getHubDetail(UUID hubId) {
        return hubRepository.findByHubIdAndIsDeleteFalse(hubId)
                            .map(HubResponseDto::new);
    }

    // 모든 Hub 조회 (is_delete = false인 항목만)
    // is_delete = false인 항목만 페이지네이션과 정렬을 포함하여 조회
    @Transactional(readOnly = true)
    public Page<Hub> getAllHubs(Pageable pageable) {
        return hubRepository.findByIsDeleteFalse(pageable);
    }

    // 검색 조건 추가
    @Transactional(readOnly = true)
    public Page<Hub> getFilteredHubs(String hubName, String hubAddress, Pageable pageable) {
        return hubRepository.findByFilters(hubName, hubAddress, pageable);
    }
}
