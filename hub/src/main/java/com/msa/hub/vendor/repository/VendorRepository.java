package com.msa.hub.vendor.repository;

import com.msa.hub.vendor.domain.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID> {
    // isDelete가 false인 것만 조회
    Page<Vendor> findByIsDeleteFalse(Pageable pageable);


    // 특정 Vendor를 ID로 조회 (isDelete가 false인 경우만)
    Optional<Vendor> findByVendorIdAndIsDeleteFalse(UUID hubId);

    // is_delete = false인 항목을 조건에 따라 필터링하여 페이징 처리
    @Query("SELECT v FROM Vendor v WHERE v.isDelete = false " +
            "AND (:vendorName IS NULL OR v.vendorName LIKE %:vendorName%) " +
            "AND (:vendorAddress IS NULL OR v.vendorAddress LIKE %:vendorAddress%)")
    Page<Vendor> findByFilters(@Param("vendorName") String vendorName,
                            @Param("vendorAddress") String vendorAddress,
                            Pageable pageable);
}
