package msa.logistics.service.logistics.delivery.domain;

import jakarta.persistence.*;
import lombok.*;
import msa.logistics.service.logistics.common.entity.BaseEntity;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "p_delivery")
public class Delivery extends BaseEntity {


    // 배송 아이디
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "delivery_id")
    private UUID deliveryId;

    // 배송 상태
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus =DeliveryStatus.WAITING;

    //배송지 주소
    @Column(name="delivery_address" , nullable = false)
    private String deliveryAddress;

    //출발 허브 아이디 - hub 서비스에서 조회

    @Column(name = "start_hub_id")
    private UUID StartHubId;

    //도착 허브 아이디 - hub 서비스에서 조회
    @Column(name = "destination_hub_id")
    private UUID destinationHubId;

    //현재 허브 아이디 - hub 서비스에서 조회
    @Column(name = "current_hub_id", nullable = false)
    private UUID currentHubId;

    //수령인 슬랙 아이디 , 기본값 null
    @Column(name = "receiver_slack_id")
    private String receiverSlackId;

    //수령인 아이디 -user 서비스에서 조회
    @Column(name = "receiver_id" , nullable = false)
    private String receiverId;

    //주문 아이디

//        @OneToOne(mappedBy = "order")
//        private Order order;


    @OneToMany(mappedBy = "delivery")
    private List<DeliveryRoute> deliveryRouteList;



    public enum DeliveryStatus {
        WAITING, IN_TRANSIT, ARRIVED, DELIVERED
    }

}
