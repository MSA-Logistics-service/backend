package msa.logistics.service.logistics.order.service;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.exception.CustomException;
import msa.logistics.service.logistics.common.exception.ErrorCode;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.delivery.domain.DeliveryRoute;
import msa.logistics.service.logistics.delivery.dto.DeliveryCreateRequestDto;
import msa.logistics.service.logistics.delivery.repository.DeliveryRepository;
import msa.logistics.service.logistics.delivery.repository.DeliveryRouteRepository;
import msa.logistics.service.logistics.delivery.service.DeliveryService;
import msa.logistics.service.logistics.order.domain.Order;
import msa.logistics.service.logistics.order.dto.request.OrderCreateRequestDto;
import msa.logistics.service.logistics.order.repository.OrderRepository;
import msa.logistics.service.logistics.product.domain.Product;
import msa.logistics.service.logistics.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryService deliveryService;

    // 주문 생성 (배송 생성 API 호출)
    @Transactional
    public UUID createOrder(OrderCreateRequestDto requestDto) {
        // 상품 확인
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        // 배송 생성 요청 DTO 생성
        DeliveryCreateRequestDto deliveryRequest  = DeliveryCreateRequestDto.builder()
                .orderId(null) // 주문이 아직 생성되지 않았으므로 null
                .supplierVenderId(requestDto.getSupplierVendorId()) // 실제 값 사용
                .receiverVenderId(requestDto.getReceiverVendorId()) // 실제 값 사용
                .productId(product.getProductId()) // 실제 값 사용
                .recevierSlackId("someSlackId") // 실제 값 사용
                .build();

        // 배송 생성
        UUID deliveryId = deliveryService.createDelivery(deliveryRequest); // 배송 생성 호출
        // 배송 경로 기록 생성은 DeliveryService에서 처리됨

        // 생성된 배송 ID로 Delivery 객체 조회
        Delivery delivery = deliveryService.getDeliveryById(deliveryId);

        // 주문 엔티티 생성
        Order order = Order.builder()
                .quantity(requestDto.getQuantity())
                .supplierVendorId(requestDto.getSupplierVendorId())
                .receiverVendorId(requestDto.getReceiverVendorId())
                .product(product)
                .delivery(delivery) // 배송 ID를 설정할 새로운 Delivery 객체를 생성
                .build();

        // 주문 저장
        return orderRepository.save(order).getOrderId();
    }

    public Delivery getDeliveryById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));
    }

}
