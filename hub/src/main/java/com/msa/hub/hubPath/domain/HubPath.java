package com.msa.hub.hubPath.domain;

import com.msa.hub.common.entity.BaseEntity;
import com.msa.hub.hub.domain.Hub;
import com.msa.hub.hubPath.dto.HubPathRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "p_hub_path")
public class HubPath extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "hub_path_id", columnDefinition = "UUID")
    private UUID hubPathId;

    @Column(name = "hub_path_duration", nullable = false)
    private Integer hubPathDuration;

    // 다른 허브 이동 정보 리스트 (재귀적 관계)
    @OneToMany
    @JoinColumn(name = "other_hub_path_id")
    private List<HubPath> otherHubPaths;

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
        this.otherHubPaths = otherHubPaths;
        this.startHub = startHub;
        this.endHub = endHub;
        this.hubRank = hubRank;
    }

    public List<HubPath> getOtherHubPaths() {
        return otherHubPaths;
    }

    public void setOtherHubPaths(List<HubPath> otherHubPaths) {
        this.otherHubPaths = otherHubPaths;
    }

    public void update(HubPathRequestDto hubPathRequestDto, Hub startHub, Hub endHub, String user) {
        // HubPathRequestDto로부터 필드 업데이트
        this.hubPathDuration = hubPathRequestDto.getHubPathDuration();
        this.hubRank = hubPathRequestDto.getHubRank();

        // 전달받은 startHub, endHub로 업데이트
        this.startHub = startHub;
        this.endHub = endHub;
    }

    public void delete() {
        this.isDelete = true;  // 논리적 삭제 처리
    }
}
