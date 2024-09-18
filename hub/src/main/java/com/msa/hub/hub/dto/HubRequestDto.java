package com.msa.hub.hub.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class HubRequestDto {

    private String hubName;
    private String hubAddress;
    private Double hubLatitude;
    private Double hubLongitude;
    private Integer hubRank;
}
