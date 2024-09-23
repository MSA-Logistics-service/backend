package msa.logistics.service.logistics.client.hub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HubPathResponseDto {

    private UUID hubPathId;
    private Integer hubPathDuration;
    private List<UUID> otherHubPathId;  // UUID 리스트로 변경
    private UUID startHubId;
    private UUID endHubId;
    private Integer hubRank;
}
