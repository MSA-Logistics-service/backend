package com.msa.hub.hub.dto;

import lombok.Data;

@Data
public class HubResponseDto {
    private String hubName;
    private String hubAddress;
    private Double hubLatitude;
    private Double hubLongitude;
}
