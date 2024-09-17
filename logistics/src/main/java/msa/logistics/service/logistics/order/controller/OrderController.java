package msa.logistics.service.logistics.order.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.order.dto.request.OrderCreateRequestDto;
import msa.logistics.service.logistics.order.dto.response.OrderResponseDto;
import msa.logistics.service.logistics.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문 생성
     */
    @PostMapping
    public ResponseEntity<ApiResponseDto<UUID>> createOrder(@RequestBody OrderCreateRequestDto requestDto) {
        UUID orderId = orderService.createOrder(requestDto);
        return ResponseEntity
                .created(URI.create("/api/v1/orders/" + orderId))
                .body(new ApiResponseDto<>(HttpStatus.CREATED, "주문 생성 성공", orderId));
    }

    /**
     * 주문 상세 조회
     */
//    @GetMapping("/{order_id}")
//    public ResponseEntity<ApiResponseDto<OrderResponseDto>> getOrderById(@PathVariable("order_id") UUID orderId) {
//        OrderResponseDto responseDto = orderService.getOrderById(orderId);
//        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "주문 상세 조회 성공", responseDto));
//    }
}
