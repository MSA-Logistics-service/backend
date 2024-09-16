package msa.logistics.service.logistics.delivery.domain;

import jakarta.persistence.*;
import lombok.*;
import msa.logistics.service.logistics.common.entity.BaseEntity;
import msa.logistics.service.logistics.delivery.dto.RouteStatus;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "p_delivery_routes")
public class DeliveryRoute extends BaseEntity {

    //경로 기록 아이디
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "delivery_route_id", nullable = false, unique = true)
    private UUID deliveryRouteId;
    //경로 순서
    @Column(name = "sequence", nullable = false)
    private int sequence;

    //출발 허브 아이디 - hub 서비스에서 조회
    @Column(name = "start_hub_id", nullable = false)
    private UUID startHubId;

    // 도착 허브 아이디 - hub 서비스에서 조회
    @Column(name = "destination_hub_id", nullable = false)
    private UUID destinationHubId;

    //예상 소요 거리- hub 서비스에서 조회
    @Column(name = "estimated_distance", nullable = true)
    private Double estimatedDistance;

    //예상 소요 시간- hub 서비스에서 조회
    @Column(name = "estimated_duration", nullable = true)
    private Integer estimatedDuration;

    //실제 소요 거리- hub 서비스에서 조회
    @Column(name = "actual_distance", nullable = true)
    private Double actualDistance;

    // 실제 소요 시간- hub 서비스에서 조회
    @Column(name = "actual_duration", nullable = true)
    private Integer actualDuration;

    // 현재 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "current_status", nullable = false)
    private RouteStatus currentStatus = RouteStatus.WAITING;

    //배송
    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private Delivery delivery;




}
