package msa.logistics.service.logistics.delivery.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryResponseDto;
import msa.logistics.service.logistics.delivery.service.DeliveryService;
import msa.logistics.service.logistics.delivery.dto.DeliveryCreateRequestDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryEditRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    // 배송 생성
    @PostMapping
    public ResponseEntity<ApiResponseDto<String>> createDelivery(
            @RequestBody DeliveryCreateRequestDto deliveryCreateRequestDto) {
        UUID deliveryId = deliveryService.createDelivery(deliveryCreateRequestDto);
        return ResponseEntity
                .created(URI.create("/api/v1/deliveries/" + deliveryId))
                .body(new ApiResponseDto<>(HttpStatus.CREATED, "배송이 생성되었습니다.", deliveryId.toString()));
    }

    // 배송 상세조회
    @GetMapping("/{delivery_id}")
    public ResponseEntity<ApiResponseDto<DeliveryResponseDto>> getDelivery(@PathVariable("delivery_id") UUID deliveryId) {
        DeliveryResponseDto delivery = deliveryService.getDeliveryById(deliveryId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 상세 정보 조회", delivery));
    }

    // 배송 수정
    @PutMapping("/{delivery_id}")
    public ResponseEntity<ApiResponseDto<DeliveryResponseDto>> editDelivery(
            @PathVariable("delivery_id") UUID deliveryId,
            @RequestBody DeliveryEditRequestDto deliveryEditRequestDto) {
        // 서비스에서 수정된 결과를 DeliveryResponseDto로 반환하도록 수정
        DeliveryResponseDto updatedDelivery = deliveryService.editDelivery(deliveryId, deliveryEditRequestDto);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 정보가 수정되었습니다.", updatedDelivery));
    }

    // 배송 삭제
    @DeleteMapping("/{delivery_id}")
    public ResponseEntity<ApiResponseDto<String>> deleteDelivery(@PathVariable("delivery_id") UUID deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송이 삭제되었습니다.", null));
    }

    // 배송 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<List<DeliveryResponseDto>>> getDeliveryList() {
        List<DeliveryResponseDto> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 목록 조회", deliveries));
    }
}
