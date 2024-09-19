package msa.logistics.service.logistics.client.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubResponseDto {

    private UUID hubId;
    private String hubName;
    private String hubAddress;
    private Double hubLatitude;
    private Double hubLongitude;
    private Double hubRank;
}
