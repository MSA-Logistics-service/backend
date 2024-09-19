package msa.logistics.service.ai.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

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
}
