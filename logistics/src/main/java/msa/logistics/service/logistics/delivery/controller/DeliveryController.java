package msa.logistics.service.logistics.delivery.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.delivery.dto.request.DeliveryEditRequestDto;
import msa.logistics.service.logistics.delivery.dto.response.DeliveryResponseDto;
import msa.logistics.service.logistics.delivery.service.DeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    // 배송 상세조회
    @GetMapping("/{deliveryId}")
    @PreAuthorize("hasAuthority('MASTER')")
    public ResponseEntity<ApiResponseDto<DeliveryResponseDto>> getDelivery(@PathVariable("deliveryId") UUID deliveryId) {
        Delivery delivery = deliveryService.getDeliveryById(deliveryId);
        return createResponse(HttpStatus.OK, "배송 상세 정보 조회", DeliveryResponseDto.fromEntity(delivery));
    }

    // 배송 수정
    @PutMapping("/{deliveryId}")

    public ResponseEntity<ApiResponseDto<DeliveryResponseDto>> editDelivery(
            @PathVariable("deliveryId") UUID deliveryId,
            @RequestBody DeliveryEditRequestDto deliveryEditRequestDto) {
        DeliveryResponseDto updatedDelivery = deliveryService.editDelivery(deliveryId, deliveryEditRequestDto);
        return createResponse(HttpStatus.OK, "배송 정보가 수정되었습니다.", updatedDelivery);
    }

    // 배송 삭제
    @DeleteMapping("/{deliveryId}")
    public ResponseEntity<ApiResponseDto<String>> deleteDelivery(@PathVariable("deliveryId") UUID deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return createResponse(HttpStatus.OK, "배송이 삭제되었습니다.", deliveryId.toString());
    }

    // 배송 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<Page<DeliveryResponseDto>>> getDeliveryList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sort", defaultValue = "false") Boolean sort) {
        Page<DeliveryResponseDto> deliveries = deliveryService.getAllDeliveries(page - 1, limit, sort);
        return createResponse(HttpStatus.OK, "배송 목록 조회", deliveries);
    }

    // 헬퍼 메서드
    private <T> ResponseEntity<ApiResponseDto<T>> createResponse(HttpStatus status, String message, T data) {
        return ResponseEntity.ok(new ApiResponseDto<>(status, message, data));
    }
}
