package msa.logistics.service.logistics.client.vendor.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.domain.VendorType;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VendorResponseDto {
    private UUID vendorId;
    private String vendorName;
    private VendorType vendorType;
    private String vendorAddress;
    private HubResponseDto hub;  // Hub 정보를 HubResponseDto로 반환
    private Long userId;
}
