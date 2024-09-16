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
    public Hub registerHub(HubResponseDto hubDTO) {
        // Hub 엔터티로 변환
        Hub hub = new Hub(
                hubDTO.getHubName(),
                hubDTO.getHubAddress(),
                hubDTO.getHubLatitude(),
                hubDTO.getHubLongitude()
        );

        // 엔터티 저장
        return hubRepository.save(hub);
    }

    // Hub 수정
    @Transactional
    public HubResponseDto updateHub(UUID hubId, HubRequestDto hubRequestDto, String userRole) {
        // 해당 Hub가 DB에 존재하는지 확인
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Hub가 존재하지 않습니다."));

        // 관리자만 수정 가능
        //if (userRole.equals("ADMIN")) {
            hub.update(hubRequestDto, userRole);  // Hub 엔터티에 update 메서드를 통해 필드를 업데이트
//        } else {
//            throw new IllegalArgumentException("해당 Hub를 수정할 권한이 없습니다.");
//        }

        // 수정된 Hub 데이터를 반환
        return new HubResponseDto(hub);
    }

    // Hub 삭제 (is_delete = true 로 설정)
    @Transactional
    public void deleteHub(UUID hubId) {
        Hub hub = hubRepository.findById(hubId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Hub가 존재하지 않습니다."));

        hub.delete(true); // 소프트 삭제 처리
        hubRepository.save(hub); // DB에 변경 사항 저장
    }

    // Hub 상세 조회 (is_delete = false인 항목만)
    public Optional<Hub> getHubDetail(UUID hubId) {
        return hubRepository.findByHubIdAndIsDeleteFalse(hubId);
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
