package msa.logistics.service.logistics.delivery.dto.request;

import lombok.*;
import msa.logistics.service.logistics.delivery.domain.DeliveryStatus;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DeliveryEditRequestDto {
    //수령 주소
    private String deliveryAddress;

    //배송 상태
    private DeliveryStatus deliveryStatus;

    // 수령인 슬랙 아이디
    private String receiverSlackId;



}
