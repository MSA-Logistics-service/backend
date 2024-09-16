package com.msa.hub.vendor.service;

import com.msa.hub.hub.domain.Hub;
import com.msa.hub.hub.repository.HubRepository;
import com.msa.hub.vendor.domain.Vendor;
import com.msa.hub.vendor.dto.VendorRequestDto;
import com.msa.hub.vendor.dto.VendorResponseDto;
import com.msa.hub.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorRepository vendorRepository;
    private final HubRepository hubRepository;


    @Transactional
    public VendorResponseDto registerVendor(VendorRequestDto vendorRequestDto) {
        // Hub가 존재하는지 확인
        Hub hub = hubRepository.findById(vendorRequestDto.getHubId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Hub가 존재하지 않습니다."));

        // UserId는 Request에서 받아온 값 사용
        Long userId = vendorRequestDto.getUserId();

        // Vendor 엔티티 생성 후 저장
        Vendor vendor = new Vendor(
                vendorRequestDto.getVendorName(),
                vendorRequestDto.getVendorType(),
                vendorRequestDto.getVendorAddress(),
                hub,
                userId
        );

        // 저장 후 DTO로 변환하여 반환
        vendorRepository.save(vendor);
        return new VendorResponseDto(vendor);
    }



    // Vendor 수정
    @Transactional
    public VendorResponseDto updateVendor(UUID vendorId, VendorRequestDto vendorRequestDto, String userRole) {
        // 관리자 권한 확인
//        if (!userRole.equals("ADMIN")) {
//            throw new IllegalArgumentException("해당 Vendor를 수정할 권한이 없습니다.");
//        }

        // Vendor가 존재하는지 확인
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Vendor가 존재하지 않습니다."));

        // Hub가 존재하는지 확인 (필요 시 Hub도 변경할 수 있음)
        Hub hub = hubRepository.findById(vendorRequestDto.getHubId())
                .orElseThrow(() -> new IllegalArgumentException("해당 Hub가 존재하지 않습니다."));

        // Vendor 정보 업데이트
        vendor.update(vendorRequestDto, hub);

        // 업데이트된 Vendor를 DTO로 변환하여 반환
        return new VendorResponseDto(vendor);
    }


    // Vendor 삭제 (소프트 삭제: is_delete = true 설정)
    @Transactional
    public void deleteVendor(UUID vendorId) {
        // Vendor가 존재하는지 확인
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 Vendor가 존재하지 않습니다."));

        // 소프트 삭제 처리 (is_delete = true)
        vendor.delete(true);

        // 변경 사항을 저장
        vendorRepository.save(vendor);
    }

    // Vendor 상세 조회 (is_delete = false인 항목만)
    public Optional<Vendor> getVendorDetail(UUID vendorId) {
        return vendorRepository.findByVendorIdAndIsDeleteFalse(vendorId);
    }

    // 모든 Vendor 조회 (is_delete = false인 항목만)
    // is_delete = false인 항목만 페이지네이션과 정렬을 포함하여 조회
    @Transactional(readOnly = true)
    public Page<Vendor> getAllVendors(Pageable pageable) {
        return vendorRepository.findByIsDeleteFalse(pageable);
    }

    // 검색 조건 추가
    @Transactional(readOnly = true)
    public Page<Vendor> getFilteredVendors(String vendorName, String vendorAddress, Pageable pageable) {
        return vendorRepository.findByFilters(vendorName, vendorAddress, pageable);
    }
}
