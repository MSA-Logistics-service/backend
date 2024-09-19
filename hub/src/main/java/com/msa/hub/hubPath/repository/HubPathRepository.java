package com.msa.hub.hubPath.repository;

import com.msa.hub.hubPath.domain.HubPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.rmi.server.UID;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubPathRepository extends JpaRepository<HubPath, UUID> {
    // isDelete가 false인 것만 조회
    Page<HubPath> findByIsDeleteFalse(Pageable pageable);


    // 특정 HubPath를 ID로 조회 (isDelete가 false인 경우만)
    Optional<HubPath> findByHubPathIdAndIsDeleteFalse(UUID hubPathId);

    // is_delete = false인 항목을 조건에 따라 필터링하여 페이징 처리
    @Query("SELECT h FROM HubPath h WHERE h.isDelete = false " )
    Page<HubPath> findByFilters(Pageable pageable);

    // 특정 시작 허브와 도착 허브를 가지는 경로 조회
    @Query("SELECT h FROM HubPath h WHERE h.isDelete = false " )
    Page<HubPath> findByFilters(UUID startHubId, UUID endHubId, Pageable pageable);

    // 도착 허브와 현재 허브를 도착 허브로 가지고 있는 경로 조회
    Optional<HubPath> findByStartHub_HubId(UUID  startHubId);

    Optional<HubPath> findByHubRank(Integer hubRank);

}
