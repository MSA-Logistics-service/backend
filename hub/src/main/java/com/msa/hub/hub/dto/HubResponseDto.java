package com.msa.hub.hub.dto;

import com.msa.hub.hub.domain.Hub;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class HubResponseDto {

    private UUID hubId;
    private String hubName;
    private String hubAddress;
    private Double hubLatitude;
    private Double hubLongitude;
    private Integer hubRank;

    // 엔티티를 DTO로 변환하는 생성자
    public HubResponseDto(Hub hub) {
        this.hubId = hub.getHubId();
        this.hubName = hub.getHubName();
        this.hubAddress = hub.getHubAddress();
        this.hubLatitude = hub.getHubLatitude();
        this.hubLongitude = hub.getHubLongitude();
        this.hubRank = hub.getHubRank();
    }
}
