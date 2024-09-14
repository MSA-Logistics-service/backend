package com.msa.hub.vendor.domain;

import com.msa.hub.common.entity.BaseEntity;
import com.msa.hub.hub.domain.Hub;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_vendor")
public class Vendor extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "vendor_id", columnDefinition = "BINARY(16)")
    private UUID vendorId;

    @Column(name = "vendor_name", nullable = false)
    private String vendorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "vendor_type", nullable = false)
    private VendorType vendorType;

    @Column(name = "vendor_address")
    private String vendorAddress;

    // p_hub와의 관계 (같은 서비스에 존재, 1:Many 관계)
    @ManyToOne
    @JoinColumn(name = "hub_id", nullable = false)
    private Hub hub;

    // p_user는 다른 서비스의 엔티티와 1:1 관계 (단순히 ID로 관리)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    public Vendor(String vendorName, VendorType vendorType, String vendorAddress, Hub hub, Long userId) {
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.vendorAddress = vendorAddress;
        this.hub = hub;
        this.userId = userId;
    }
}
