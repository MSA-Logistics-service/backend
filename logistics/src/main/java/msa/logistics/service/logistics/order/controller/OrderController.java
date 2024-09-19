package msa.logistics.service.logistics.order.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.order.dto.request.OrderCreateRequestDto;
import msa.logistics.service.logistics.order.dto.request.OrderUpdateRequestDto;
import msa.logistics.service.logistics.order.dto.response.OrderResponseDto;
import msa.logistics.service.logistics.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
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
    @PreAuthorize("hasAuthority('VENDOR_MANAGER') or hasAuthority('MASTER')")
    public ResponseEntity<ApiResponseDto<UUID>> createOrder(@RequestHeader(value = "X-USER-NAME") String username,
                                                            @RequestBody OrderCreateRequestDto requestDto) {
        UUID orderId = orderService.createOrder(username, requestDto);
        return ResponseEntity
                .created(URI.create("/api/v1/orders/" + orderId))
                .body(new ApiResponseDto<>(HttpStatus.CREATED, "주문 생성 성공", orderId));
    }

    /**
     * 주문 상세 조회
     */
    @GetMapping("/{order_id}")
    @PreAuthorize("hasAuthority('VENDOR_MANAGER') or hasAuthority('MASTER')")
    public ResponseEntity<ApiResponseDto<OrderResponseDto>> getOrderById(@PathVariable("order_id") UUID orderId) {
        OrderResponseDto responseDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "주문 상세 조회 성공", responseDto));
    }

    /**
     * 주문 전체 조회
     */
    @GetMapping
    @PreAuthorize("hasAuthority('VENDOR_MANAGER') or hasAuthority('MASTER')")
    public ResponseEntity<ApiResponseDto<List<OrderResponseDto>>> getAllOrders() {
        List<OrderResponseDto> responseDtoList = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "전체 주문 조회 성공", responseDtoList));
    }

    /**
     * 주문 수정
     */
    @PutMapping("/{order_id}")
    public ResponseEntity<ApiResponseDto<Void>> updateOrder(@PathVariable("order_id") UUID orderId,
                                                            @RequestBody OrderUpdateRequestDto requestDto) {
        orderService.updateOrder(orderId, requestDto);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "주문 수정 성공", null));
    }

    /**
     * 주문 삭제 (논리적 삭제)
     */
    @DeleteMapping("/{order_id}")
    public ResponseEntity<ApiResponseDto<Void>> deleteOrder(@PathVariable("order_id") UUID orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "주문 삭제 성공", null));
    }
}
