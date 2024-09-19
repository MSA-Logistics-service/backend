package com.msa.hub.vendor.domain;

import com.msa.hub.common.entity.BaseEntity;
import com.msa.hub.hub.domain.Hub;
import com.msa.hub.vendor.dto.VendorRequestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Setter
@Builder
@Table(name = "p_vendor")
@SQLRestriction("is_delete is false")
public class Vendor extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "vendor_id", columnDefinition = "UUID")
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

    // 생성자
    public Vendor(String vendorName, VendorType vendorType, String vendorAddress, Hub hub, Long userId) {
        this.vendorName = vendorName;
        this.vendorType = vendorType;
        this.vendorAddress = vendorAddress;
        this.hub = hub;
        this.userId = userId;
    }

    // Vendor 업데이트 메서드
    public void update(VendorRequestDto vendorRequestDto, Hub hub) {
        this.vendorName = vendorRequestDto.getVendorName();
        this.vendorType = vendorRequestDto.getVendorType();
        this.vendorAddress = vendorRequestDto.getVendorAddress();
        this.hub = hub;  // Hub 변경 가능
    }

    // 소프트 삭제 메서드
    public void delete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
