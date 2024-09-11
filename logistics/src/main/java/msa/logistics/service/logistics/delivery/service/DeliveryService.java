package msa.logistics.service.logistics.delivery.service;


import msa.logistics.service.logistics.delivery.dto.DeliveryResponseDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryCreateRequestDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryEditRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeliveryService {


    public UUID createDelivery(DeliveryCreateRequestDto deliveryCreateRequestDto) {
    return null;
    }

    public DeliveryResponseDto getDeliveryById(UUID deliveryId) {
        return null;
    }

    public DeliveryResponseDto editDelivery(UUID deliveryId, DeliveryEditRequestDto deliveryEditRequestDto) {

    return null;}

    public void deleteDelivery(UUID deliveryId) {
    }

    public List<DeliveryResponseDto> getAllDeliveries() {
        return null;}
}
