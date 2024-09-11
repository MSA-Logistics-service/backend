package msa.logistics.service.logistics.delivery.service;

import msa.logistics.service.logistics.delivery.dto.DeliveryRouteResponseDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryRouteCreateRequestDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryRouteEditRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeliveryRouteService {


    public UUID createDeliveryRoute(DeliveryRouteCreateRequestDto routeCreateRequestDto) {
    return null;
    }

    public DeliveryRouteResponseDto getDeliveryRouteById(UUID deliveryRouteId) {
    return null;
    }

    public DeliveryRouteEditRequestDto editDelivery(UUID deliveryRouteId, DeliveryRouteEditRequestDto deliveryRouteEditRequestDto) {
    return null;
    }

    public DeliveryRouteResponseDto editDeliveryRoute(UUID deliveryRouteId, DeliveryRouteEditRequestDto deliveryRouteEditRequestDto) {
    return null;
    }

    public void deleteDeliveryRoute(UUID deliveryRouteId) {

    }

    public List<DeliveryRouteResponseDto> searchDeliveryRoutes(String filter, int page, int size) {
    return null;
    }
}
