package msa.logistics.service.logistics.delivery.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.delivery.dto.response.DeliveryRouteResponseDto;
import msa.logistics.service.logistics.delivery.service.DeliveryRouteService;
import msa.logistics.service.logistics.delivery.dto.request.DeliveryRouteEditRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/delivery-routes")
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;


    // 배송 경로 상세 조회
    @GetMapping("/{deliveryRouteId}")
    public ResponseEntity<ApiResponseDto<DeliveryRouteResponseDto>> getDeliveryRoute(
            @PathVariable("deliveryRouteId") UUID deliveryRouteId) {
        DeliveryRouteResponseDto deliveryRoute = deliveryRouteService.getDeliveryRouteById(deliveryRouteId)
                .map(DeliveryRouteResponseDto::fromEntity) // DTO로 변환
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "배송 경로를 찾을 수 없습니다."));
        return createResponse(HttpStatus.OK, "배송 경로 상세 정보 조회", deliveryRoute);
    }

    // 배송 경로 수정
    @PutMapping("/{deliveryRouteId}")
    public ResponseEntity<ApiResponseDto<DeliveryRouteResponseDto>> editDeliveryRoute(
            @PathVariable("deliveryRouteId") UUID deliveryRouteId,
            @RequestBody DeliveryRouteEditRequestDto deliveryRouteEditRequestDto) {
        DeliveryRouteResponseDto updatedDeliveryRoute = deliveryRouteService.editDeliveryRoute(deliveryRouteId, deliveryRouteEditRequestDto);
        return createResponse(HttpStatus.OK, "배송 경로가 수정되었습니다.", updatedDeliveryRoute);
    }

    // 배송 경로 삭제
    @DeleteMapping("/{deliveryRouteId}")
    public ResponseEntity<ApiResponseDto<String>> deleteDeliveryRoute(@PathVariable("deliveryRouteId") UUID deliveryRouteId) {
        deliveryRouteService.deleteDeliveryRoute(deliveryRouteId);
        return createResponse(HttpStatus.OK, "배송 경로가 삭제되었습니다.", null);
    }

    // 배송 경로 기록 조회
    @GetMapping
    public ResponseEntity<ApiResponseDto<Page<DeliveryRouteResponseDto>>> searchDeliveryRoutes(
            @RequestParam(required = false) String filter,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        Page<DeliveryRouteResponseDto> deliveryRoutes = deliveryRouteService.searchDeliveryRoutes(filter, page - 1, limit);
        return createResponse(HttpStatus.OK, "배송 경로 검색 결과", deliveryRoutes);
    }

    // 헬퍼 메서드
    private <T> ResponseEntity<ApiResponseDto<T>> createResponse(HttpStatus status, String message, T data) {
        return ResponseEntity.ok(new ApiResponseDto<>(status, message, data));
    }
}