package msa.logistics.service.logistics.delivery.dto.response;

import lombok.*;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.delivery.domain.DeliveryStatus;

import java.util.UUID;
@Data
@Builder
public class DeliveryResponseDto {

 //배송 ID
 private UUID deliveryId;

 //배송상태
 private DeliveryStatus deliveryStatus;

 //배송지 주소
 private String deliveryAddress;

 //주문 아이디
 private UUID orderId;

 //출발 허브 아이디
 private UUID startHubId;

 //현재 허브 아이디
 private UUID currentHubId;

 //도착 허브 아이디
 private UUID destinationHubId;

 // 수령인 슬랙 아이디
 private String receiverSlackId;

 // 수령인 아이디
    private UUID receiverId;

    // 변환 메서드
    public static DeliveryResponseDto fromEntity(Delivery delivery) {
        return DeliveryResponseDto.builder()
                .deliveryId(delivery.getDeliveryId())
                .orderId(delivery.getOrderId())
                .startHubId(delivery.getStartHubId())
                .destinationHubId(delivery.getDestinationHubId())
                .receiverSlackId(delivery.getReceiverSlackId())
                .receiverId(delivery.getReceiverId())
                .deliveryAddress(delivery.getDeliveryAddress())
                .deliveryStatus(delivery.getDeliveryStatus())
                .build();
    }

    public static DeliveryResponseDto fromEntityForUpdate(Delivery delivery) {
        return DeliveryResponseDto.builder()
                .receiverId(delivery.getReceiverId())
                .deliveryAddress(delivery.getDeliveryAddress())
                .deliveryStatus(delivery.getDeliveryStatus())
                .receiverSlackId(delivery.getReceiverSlackId())
                .build();
    }




}
