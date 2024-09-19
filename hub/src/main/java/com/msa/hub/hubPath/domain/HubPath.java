package com.msa.hub.hubPath.domain;

import com.msa.hub.common.entity.BaseEntity;
import com.msa.hub.hub.domain.Hub;
import com.msa.hub.hubPath.dto.HubPathRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
@SQLRestriction("is_delete is false")
@Table(name = "p_hub_path")
public class HubPath extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "hub_path_id", columnDefinition = "UUID")
    private UUID hubPathId;

    @Column(name = "hub_path_duration", nullable = false)
    private Integer hubPathDuration;

    // 다른 허브 이동 정보 리스트 (재귀적 관계)
    @Column(name = "other_hub_path_id", nullable = true)
    private String otherHubPaths; // 배열을 문자열로 관리

    // 시작 허브 (Hub 엔티티와 연관)
    @ManyToOne
    @JoinColumn(name = "start_hub_id", nullable = false)
    private Hub startHub;

    // 종료 허브 (Hub 엔티티와 연관)
    @ManyToOne
    @JoinColumn(name = "end_hub_id", nullable = false)
    private Hub endHub;

    @Column(name = "hub_rank", nullable = false)
    private Integer hubRank;

    private Boolean isDelete = false;  // 기본값은 false로 설정

    // 생성자
    public HubPath(Integer hubPathDuration, List<HubPath> otherHubPaths, Hub startHub, Hub endHub, Integer hubRank) {
        this.hubPathDuration = hubPathDuration;

        // otherHubPaths 리스트에서 각 HubPath의 ID를 문자열로 변환하여 합침
        List<String> otherHubPathIds = otherHubPaths.stream()
                .map(hubPath -> hubPath.getHubPathId().toString())  // 각 HubPath의 ID를 문자열로 변환
                .collect(Collectors.toList());

        this.otherHubPaths = String.join(",", otherHubPathIds); // ID 문자열들을 합쳐서 설정
        this.startHub = startHub;
        this.endHub = endHub;
        this.hubRank = hubRank;
    }


    public List<String> getOtherHubPaths() {
        if (this.otherHubPaths == null || this.otherHubPaths.isEmpty()) {
            return List.of(); // 비어있는 경우 빈 리스트 반환
        }
        return Arrays.asList(this.otherHubPaths.split(","));
    }

    public void setOtherHubPaths(List<String> otherHubPaths) {
        this.otherHubPaths = String.join(",", otherHubPaths);
    }


    public void update(HubPathRequestDto hubPathRequestDto, Hub startHub, Hub endHub, String user) {
        this.hubPathDuration = hubPathRequestDto.getHubPathDuration() != null ? hubPathRequestDto.getHubPathDuration() : this.hubPathDuration;
        this.hubRank = hubPathRequestDto.getHubRank() != null ? hubPathRequestDto.getHubRank() : this.hubRank;
        this.startHub = startHub != null ? startHub : this.startHub;
        this.endHub = endHub != null ? endHub : this.endHub;

        // this.updatedBy = user;
    }


    public void delete() {
        this.isDelete = true;  // 논리적 삭제 처리
    }
}
