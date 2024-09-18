package com.msa.hub.hubPath.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class HubPathRequestDto {

    private UUID hubPathId;
    private Integer hubPathDuration;       // 소요 시간
    private List<UUID> otherHubPaths;      // 다른 허브 경로 리스트
    private UUID startHubId;               // 시작 허브 ID
    private UUID endHubId;                 // 종료 허브 ID
    private Integer hubRank;                // 허브 순서
}
