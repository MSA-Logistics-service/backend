package com.msa.hub.vendor.dto;

import com.msa.hub.hub.dto.HubResponseDto;
import com.msa.hub.vendor.domain.Vendor;
import com.msa.hub.vendor.domain.VendorType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class VendorResponseDto {

    private UUID vendorId;
    private String vendorName;
    private VendorType vendorType;
    private String vendorAddress;
    private HubResponseDto hub;  // Hub 정보를 HubResponseDto로 반환
    private Long userId;

    // Vendor 엔티티를 받아서 DTO로 변환하는 생성자
    public VendorResponseDto(Vendor vendor) {
        this.vendorId = vendor.getVendorId();
        this.vendorName = vendor.getVendorName();
        this.vendorType = vendor.getVendorType();
        this.vendorAddress = vendor.getVendorAddress();
        this.hub = new HubResponseDto(vendor.getHub()); // Hub 엔티티를 HubResponseDto로 변환
        this.userId = vendor.getUserId();
    }
}
