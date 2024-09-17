package com.msa.hub.hubPath.domain;

import com.msa.hub.common.entity.BaseEntity;
import com.msa.hub.hub.domain.Hub;
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
    @Column(name = "hub_path_duration", columnDefinition = "BINARY(16)")
    private UUID hubPathDuration;

    // 소요 시간은 Integer로 처리 (예: 분 단위)
    @Column(name = "hub_path_route", nullable = false)
    private Integer hubPathRoute;

    // 재귀적 관계 (다른 HubPath와의 관계를 리스트로 처리)
    @ManyToOne
    @JoinColumn(name = "other_hub_path_id")
    private HubPath otherHubPath;

    @OneToMany(mappedBy = "otherHubPath")
    private List<HubPath> relatedHubPaths;

    // start_hub_id 및 end_hub_id는 p_hub 테이블과의 관계 (다대일)
    @ManyToOne
    @JoinColumn(name = "start_hub_id", nullable = false)
    private Hub startHub;

    @ManyToOne
    @JoinColumn(name = "end_hub_id", nullable = false)
    private Hub endHub;

    // 생성자
    public HubPath(Integer hubPathRoute, HubPath otherHubPath, Hub startHub, Hub endHub) {
        this.hubPathRoute = hubPathRoute;
        this.otherHubPath = otherHubPath;
        this.startHub = startHub;
        this.endHub = endHub;
    }
}
