package msa.logistics.service.logistics.delivery.dto.response;

import lombok.Builder;
import lombok.Data;
import msa.logistics.service.logistics.delivery.domain.DeliveryRoute;
import msa.logistics.service.logistics.delivery.domain.RouteStatus;

import java.util.UUID;

@Data
@Builder

public class DeliveryRouteResponseDto {

    private int sequence;
    private UUID startHubId;
    private UUID destinationHubId;
    private Double estimatedDistance;
    private Integer estimatedDuration;
    private Double actualDistance;
    private Integer actualDuration;
    private RouteStatus currentStatus;
    private UUID deliveryRouteId;

    // 엔티티에서 DTO로 변환하는 메서드
    public static DeliveryRouteResponseDto fromEntity(DeliveryRoute deliveryRoute) {
        return DeliveryRouteResponseDto.builder()
                .deliveryRouteId(deliveryRoute.getDeliveryRouteId())
                .sequence(deliveryRoute.getSequence())
                .startHubId(deliveryRoute.getStartHubId())
                .destinationHubId(deliveryRoute.getDestinationHubId())
//                .estimatedDistance(deliveryRoute.getEstimatedDistance()) // 필드 사용
//                .estimatedDuration(deliveryRoute.getEstimatedDuration()) // 필드 사용
//                .actualDistance(deliveryRoute.getActualDistance()) // 필드 사용
//                .actualDuration(deliveryRoute.getActualDuration()) // 필드 사용
                .currentStatus(deliveryRoute.getCurrentStatus())
                .build();
    }

    public static DeliveryRouteResponseDto fromEntityForUpdate(DeliveryRoute deliveryRoute) {
        return DeliveryRouteResponseDto.builder()
                .currentStatus(deliveryRoute.getCurrentStatus())
                .build();
    }
}
