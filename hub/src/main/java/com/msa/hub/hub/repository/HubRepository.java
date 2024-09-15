package com.msa.hub.hub.repository;

import com.msa.hub.hub.domain.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository extends JpaRepository<Hub, UUID> {
    // isDelete가 false인 것만 조회
    Page<Hub> findByIsDeleteFalse(Pageable pageable);


    // 특정 Hub를 ID로 조회 (isDelete가 false인 경우만)
    Optional<Hub> findByHubIdAndIsDeleteFalse(UUID hubId);

    // is_delete = false인 항목을 조건에 따라 필터링하여 페이징 처리
    @Query("SELECT h FROM Hub h WHERE h.isDelete = false " +
            "AND (:hubName IS NULL OR h.hubName LIKE %:hubName%) " +
            "AND (:hubAddress IS NULL OR h.hubAddress LIKE %:hubAddress%)")
    Page<Hub> findByFilters(@Param("hubName") String hubName,
                            @Param("hubAddress") String hubAddress,
                            Pageable pageable);
}
