package msa.logistics.service.logistics.delivery.controllr;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.common.dto.ApiResponseDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryRouteResponseDto;
import msa.logistics.service.logistics.delivery.service.DeliveryRouteService;
import msa.logistics.service.logistics.delivery.dto.DeliveryRouteCreateRequestDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryRouteEditRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/delivery-routes")
public class DeliveryRouteController {

    private final DeliveryRouteService deliveryRouteService;

    // 배송 경로 등록
    @PostMapping
    public ResponseEntity<ApiResponseDto<String>> createDeliveryRoute(
            @RequestBody DeliveryRouteCreateRequestDto routeCreateRequestDto) {
        UUID deliveryRouteId = deliveryRouteService.createDeliveryRoute(routeCreateRequestDto);
        return ResponseEntity
                .created(URI.create("/api/v1/delivery-routes/" + deliveryRouteId))
                .body(new ApiResponseDto<>(HttpStatus.CREATED, "배송 경로가 생성되었습니다.", deliveryRouteId.toString()));
    }

    // 배송 경로 상세 조회
    @GetMapping("/{delivery_route_id}")
    public ResponseEntity<ApiResponseDto<DeliveryRouteResponseDto>> getDeliveryRoute(
            @PathVariable("delivery_route_id") UUID deliveryRouteId) {
        DeliveryRouteResponseDto deliveryRoute = deliveryRouteService.getDeliveryRouteById(deliveryRouteId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 경로 상세 정보 조회", deliveryRoute));
    }

    // 배송 경로 수정
    @PutMapping("/{delivery_route_id}")
    public ResponseEntity<ApiResponseDto<DeliveryRouteResponseDto>> editDeliveryRoute(
            @PathVariable("delivery_route_id") UUID deliveryRouteId,
            @RequestBody DeliveryRouteEditRequestDto deliveryRouteEditRequestDto) {
        DeliveryRouteResponseDto updatedDeliveryRoute = deliveryRouteService.editDeliveryRoute(deliveryRouteId, deliveryRouteEditRequestDto);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 경로가 수정되었습니다.", updatedDeliveryRoute));
    }

    // 배송 경로 삭제
    @DeleteMapping("/{delivery_route_id}")
    public ResponseEntity<ApiResponseDto<String>> deleteDeliveryRoute(@PathVariable("delivery_route_id") UUID deliveryRouteId) {
        deliveryRouteService.deleteDeliveryRoute(deliveryRouteId);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 경로가 삭제되었습니다.", null));
    }

    // 배송 경로 기록 검색
    @GetMapping
    public ResponseEntity<ApiResponseDto<List<DeliveryRouteResponseDto>>> searchDeliveryRoutes(
            @RequestParam(required = false) String filter, // 필터 조건 (예: 경로 상태 등)
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        List<DeliveryRouteResponseDto> deliveryRoutes = deliveryRouteService.searchDeliveryRoutes(filter, page, size);
        return ResponseEntity.ok(new ApiResponseDto<>(HttpStatus.OK, "배송 경로 검색 결과", deliveryRoutes));
    }
}
