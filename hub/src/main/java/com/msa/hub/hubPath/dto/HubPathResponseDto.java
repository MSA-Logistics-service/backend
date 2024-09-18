package com.msa.hub.hubPath.dto;

import com.msa.hub.hubPath.domain.HubPath;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class HubPathResponseDto {

    private UUID hubPathId;
    private Integer hubPathDuration;
    private List<UUID> otherHubPathId;  // UUID 리스트로 변경
    private UUID startHubId;
    private UUID endHubId;
    private Integer hubRank;

    // HubPath 엔티티에서 DTO로 변환하는 생성자
    public HubPathResponseDto(HubPath hubPath) {
        this.hubPathId = hubPath.getHubPathId();
        this.hubPathDuration = hubPath.getHubPathDuration();
        // HubPath 엔티티에서 otherHubPaths의 UUID 리스트로 변환
        this.otherHubPathId = hubPath.getOtherHubPaths().stream()
                .map(HubPath::getHubPathId)
                .collect(Collectors.toList());
        this.startHubId = hubPath.getStartHub().getHubId();
        this.endHubId = hubPath.getEndHub().getHubId();
        this.hubRank = hubPath.getHubRank();
    }
}