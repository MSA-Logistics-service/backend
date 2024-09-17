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
import msa.logistics.service.logistics.order.dto.request.OrderUpdateRequestDto;
import msa.logistics.service.logistics.order.dto.response.OrderResponseDto;
import msa.logistics.service.logistics.order.repository.OrderRepository;
import msa.logistics.service.logistics.product.domain.Product;
import msa.logistics.service.logistics.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        // 주문 엔티티 생성
        Order order = Order.builder()
                .quantity(requestDto.getQuantity())
                .supplierVendorId(requestDto.getSupplierVendorId())
                .receiverVendorId(requestDto.getReceiverVendorId())
                .product(product)
                .build();
        // 주문 저장
        orderRepository.save(order);

        UUID deliveryId;
        // 생성된 Order 객체를 DeliveryService에 전달하여 배송 생성
        try {
            deliveryId = deliveryService.createDelivery(requestDto,order.getOrderId()); // 주문 객체를 전달
            Delivery delivery = deliveryService.getDeliveryById(deliveryId);

            // 배송 객체를 주문에 설정
            order.setDeliveryId(delivery);

            // 변경된 주문 저장
            orderRepository.save(order);
        } catch (Exception e) {
            // 예외 발생 시 주문 생성 롤백
            throw new CustomException(ErrorCode.DELIVERY_CREATION_FAILED);
        }

        // 모든 작업이 성공적으로 완료되었을 때 트랜잭션 커밋
        return order.getOrderId();
    }


    // 주문 상세 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID orderId) {
        Order order = orderRepository.findByOrderIdAndIsDeleteFalse(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        return new OrderResponseDto(order);
    }

    // 주문 전체 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAllByIsDeleteFalse().stream()
                .map(OrderResponseDto::new)
                .collect(Collectors.toList());
    }

    // 주문 수정
    @Transactional
    public void updateOrder(UUID orderId, OrderUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        Delivery delivery = deliveryRepository.findById(requestDto.getDeliveryId())
                .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_NOT_FOUND));

        order.updateOrder(requestDto.getQuantity(), product, delivery);
    }
}
