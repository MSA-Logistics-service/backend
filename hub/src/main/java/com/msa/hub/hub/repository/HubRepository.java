package com.msa.hub.hub.repository;

import com.msa.hub.hub.domain.Hub;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // 가장 큰 hubRank 값을 가져오는 메서드
    @Query("SELECT COALESCE(MAX(h.hubRank), 0) FROM Hub h WHERE h.isDelete = false")
    Double findMaxHubRank();

    // 특정 hubRank 이상의 모든 hubRank를 1씩 증가시키는 메서드
    @Modifying
    @Query("UPDATE Hub h SET h.hubRank = h.hubRank + 1 WHERE h.hubRank >= :hubRank AND h.isDelete = false")
    void incrementHubRankGreaterThanOrEqual(@Param("hubRank") Double hubRank);

    // 특정 hubRank 이상의 모든 hubRank를 1씩 감소시키는 메서드 (삭제 시 사용)
    @Modifying
    @Query("UPDATE Hub h SET h.hubRank = h.hubRank - 1 WHERE h.hubRank > :hubRank AND h.isDelete = false")
    void decrementHubRankGreaterThan(@Param("hubRank") Double hubRank);
}
