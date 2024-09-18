package msa.logistics.service.logistics.delivery.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import java.util.UUID;
@Getter
@Setter
@Builder
public class DeliveryCreateRequestDto {

    //order 아이디
    private UUID orderId;

    // 공금 업체 아이디
    private UUID supplierVenderId;

    // 수령 업체 아이디
    private UUID receiverVenderId;

    //상품 아이디
    private  UUID productId;

    // 수령인 슬랙 아이디
    private String recevierSlackId;
}
