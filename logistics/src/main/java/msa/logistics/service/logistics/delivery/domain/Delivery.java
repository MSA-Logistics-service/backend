package msa.logistics.service.logistics.delivery.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "p_delivery")
public class Delivery {


    // 배송 아이디
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "delivery_id")
    private UUID deliveryId;

    // 배송 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    //배송지 주소
    @Column(name="delivery_address" , nullable = false)
    private String deliveryAddress;

    //주문아이디 - 주문 part 경진님
    @Column(name = "order_id", nullable = false)
    private UUID orderId;
    //주문 아이디
    //    @JoinColumn(name = "order_id" , nullable = false)
    //    @OneToOne(fetch = FetchType.LAZY)
    //    private Order order;


    //출발 허브 아이디 - hub 서비스에서 조회

    @Column(name = "start_hub_id")
    private UUID departureHubId;

    //도착 허브 아이디 - hub 서비스에서 조회
    @Column(name = "destination_hub_id")
    private UUID destinationHub;

    //현재 허브 아이디 - hub 서비스에서 조회
    @Column(name = "current_hub_id", nullable = false)
    private UUID currentHubId;

    //수령인 슬랙 아이디
    @Column(name = "receiver_slack_id")
    private String receiverSlackId;

    //수령인 아이디 -user 서비스에서 조회
    @Column(name = "receiver_id" , nullable = false)
    private String receiverId;


    public enum DeliveryStatus {
        WAITING, IN_TRANSIT, ARRIVED, DELIVERED
    }

}
