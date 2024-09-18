package com.msa.hub.vendor.controller;

import com.msa.hub.vendor.domain.Vendor;
import com.msa.hub.vendor.dto.VendorRequestDto;
import com.msa.hub.vendor.dto.VendorResponseDto;
import com.msa.hub.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vendor")
@RequiredArgsConstructor
public class VendorController {

    private final VendorService vendorService;
// **마스터**: 모든 권한
    //- **허브 관리자**: 자신의 허브에 소속된 업체만 관리 가능
    //- **허브 업체**: 자신의 업체만 수정 가능, 다른 업체의 읽기와 검색만 가능

    // 허브 등록
    @PostMapping
    public ResponseEntity<VendorResponseDto> registerVendor(@RequestBody VendorRequestDto vendorRequestDto) {
        // Hub와 UserId는 서비스 계층에서 조회해서 사용
        VendorResponseDto createdVendor = vendorService.registerVendor(vendorRequestDto);
        return ResponseEntity.ok(createdVendor);
    }


    // Vendor 수정
    @PatchMapping("/{vendorId}")
    public ResponseEntity<VendorResponseDto> updateVendor(
            @PathVariable UUID vendorId,
            @RequestBody VendorRequestDto vendorRequestDto,
            @RequestParam String userRole) {
        VendorResponseDto updatedVendor = vendorService.updateVendor(vendorId, vendorRequestDto, userRole);
        return ResponseEntity.ok(updatedVendor);
    }

    // Vendor 삭제 (소프트 삭제)
    @DeleteMapping("/delete/{vendorId}")
    public ResponseEntity<Void> deleteVendor(@PathVariable UUID vendorId) {
        vendorService.deleteVendor(vendorId);
        return ResponseEntity.noContent().build();
    }

    // Vendor 상세 조회
    @GetMapping("/{vendorId}")
    public ResponseEntity<Vendor> getVendorDetail(@PathVariable UUID vendorId) {
        Optional<Vendor> vendor = vendorService.getVendorDetail(vendorId);
        return vendor.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 모든 Vendor 조회 (is_delete = false인 항목만 페이지네이션 및 정렬 추가)
    @GetMapping
    public ResponseEntity<Page<Vendor>> getAllVendors(
            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "vendorName", required = false) String vendorName,
            @RequestParam(value = "vendor_address", required = false) String vendorAddress,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {

        // 유효한 pageSize 및 sortBy 값 검증
        if (pageSize != 10 && pageSize != 30 && pageSize != 50) {
            pageSize = 10;
        }
        if (!sortBy.equals("createdAt") && !sortBy.equals("updatedAt")) {
            sortBy = "createdAt";
        }

        // 정렬 방향 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        // 페이징 및 정렬 설정
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);

        // 서비스에서 페이지네이션된 결과 가져오기 (is_delete = false인 항목만)
        // Page<Vendor> vendors = vendorService.getAllVendors(pageable);
        // 필터링 조건을 사용한 서비스 호출
        Page<Vendor> vendors = vendorService.getFilteredVendors(vendorName, vendorAddress, pageable);


        // 페이징된 결과 반환
        return ResponseEntity.ok(vendors);
    }
}
