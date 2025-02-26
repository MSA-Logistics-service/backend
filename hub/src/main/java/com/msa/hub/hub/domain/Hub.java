package com.msa.hub.hub.domain;

import com.msa.hub.common.entity.BaseEntity;
import com.msa.hub.hub.dto.HubRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_hub")
@SQLRestriction("is_delete is false")
public class Hub extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "hub_id", columnDefinition = "UUID")
    private UUID hubId;

    @Column(name = "hub_name", nullable = false)
    private String hubName;

    @Column(name = "hub_address")
    private String hubAddress;

    @Column(name = "hub_latitude", nullable = false)
    private Double hubLatitude;

    @Column(name = "hub_longitude", nullable = false)
    private Double hubLongitude;

    @Column(name = "hub_rank", nullable = false)
    private Integer hubRank;

    // 생성자
    public Hub(String hubName, String hubAddress, Double hubLatitude, Double hubLongitude, Integer hubRank) {
        this.hubName = hubName;
        this.hubAddress = hubAddress;
        this.hubLatitude = hubLatitude;
        this.hubLongitude = hubLongitude;
        this.hubRank = hubRank;

    }
    // HubRequestDto를 사용하여 필드를 업데이트
    public void update(HubRequestDto hubRequestDto, String user) {
        this.hubName = hubRequestDto.getHubName() != null ? hubRequestDto.getHubName() : this.hubName;
        this.hubAddress = hubRequestDto.getHubAddress() != null ? hubRequestDto.getHubAddress() : this.hubAddress;
        this.hubLatitude = hubRequestDto.getHubLatitude() != null ? hubRequestDto.getHubLatitude() : this.hubLatitude;
        this.hubLongitude = hubRequestDto.getHubLongitude() != null ? hubRequestDto.getHubLongitude() : this.hubLongitude;
        this.hubRank = hubRequestDto.getHubRank() != null ? hubRequestDto.getHubRank() : this.hubRank;
        // 필요하다면 업데이트한 유저 정보도 기록할 수 있음
    }

    // 소프트 삭제 처리
    public void delete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
