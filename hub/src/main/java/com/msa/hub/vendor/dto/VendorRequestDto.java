package com.msa.hub.vendor.dto;

import com.msa.hub.vendor.domain.VendorType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class VendorRequestDto {

    private String vendorName;  // 벤더 이름
    private VendorType vendorType;  // 벤더 타입
    private String vendorAddress;  // 벤더 주소
    private UUID hubId;  // Hub와의 연관 관계를 위한 hubId
    private Long userId;  // 사용자의 ID
}