package com.msa.hub.hub.domain;

import com.msa.hub.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_hub")
public class Hub extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "hub_id", columnDefinition = "BINARY(16)")
    private UUID hubId;

    @Column(name = "hub_name", nullable = false)
    private String hubName;

    @Column(name = "hub_address")
    private String hubAddress;

    @Column(name = "hub_latitude", nullable = false)
    private Double hubLatitude;

    @Column(name = "hub_longitude", nullable = false)
    private Double hubLongitude;

    // 생성자
    public Hub(String hubName, String hubAddress, Double hubLatitude, Double hubLongitude) {
        this.hubName = hubName;
        this.hubAddress = hubAddress;
        this.hubLatitude = hubLatitude;
        this.hubLongitude = hubLongitude;
    }
}
