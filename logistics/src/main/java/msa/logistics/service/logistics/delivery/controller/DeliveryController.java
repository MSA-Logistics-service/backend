package msa.logistics.service.logistics.delivery.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.delivery.domain.Delivery;


import msa.logistics.service.logistics.delivery.service.DeliveryService;
import msa.logistics.service.logistics.delivery.dto.DeliveryCreateRequestDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryEditRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {

    @Autowired
    private final DeliveryService deliveryService;

    // 배송 생성
    @PostMapping
    public ResponseEntity<ApiResponseDto<String>> createDelivery(
            @RequestBody DeliveryCreateRequestDto deliveryCreateRequestDto) {
        UUID deliveryId = deliveryService.createDelivery(deliveryCreateRequestDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponseDto<>(HttpStatus.CREATED, "배송이 생성되었습니다.", deliveryId.toString()));
    }


    // 배송 상세조회
    @GetMapping("/{delivery_id}")
    public ResponseEntity<ApiResponseDto<Delivery>> getDelivery(@PathVariable("delivery_id") UUID deliveryId) {
        Delivery delivery = deliveryService.getDeliveryById(deliveryId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 상세 정보 조회", delivery));
    }

    // 배송 수정
    @PutMapping("/{delivery_id}")
    public ResponseEntity<ApiResponseDto<Delivery>> editDelivery(
            @PathVariable("delivery_id") UUID deliveryId,
            @RequestBody DeliveryEditRequestDto deliveryEditRequestDto) {
        // 서비스에서 수정된 결과를 DeliveryResponseDto로 반환하도록 수정
        Delivery updatedDelivery = deliveryService.editDelivery(deliveryId, deliveryEditRequestDto);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 정보가 수정되었습니다.", updatedDelivery));
    }

    // 배송 삭제
    @DeleteMapping("/{delivery_id}")
    public ResponseEntity<ApiResponseDto<String>> deleteDelivery(@PathVariable("delivery_id") UUID deliveryId) {
        deliveryService.deleteDelivery(deliveryId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송이 삭제되었습니다.", deliveryId.toString()));
    }

    // 배송 검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto<Page<Delivery>>> getDeliveryList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "sort", defaultValue = "false") Boolean sort) {

        // 페이지 인덱스는 0부터 시작하므로 page - 1을 사용
        Page<Delivery> deliveries = deliveryService.getAllDeliveries(page - 1, limit, sort);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 목록 조회", deliveries));
    }

}
