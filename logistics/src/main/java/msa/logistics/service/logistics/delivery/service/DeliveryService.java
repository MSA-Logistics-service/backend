package msa.logistics.service.logistics.delivery.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import msa.logistics.service.logistics.client.hub.HubService;
import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
import msa.logistics.service.logistics.common.exception.CustomException;
import msa.logistics.service.logistics.common.exception.ErrorCode;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.delivery.dto.request.DeliveryEditRequestDto;
import msa.logistics.service.logistics.delivery.domain.DeliveryStatus;
import msa.logistics.service.logistics.delivery.dto.response.DeliveryResponseDto;
import msa.logistics.service.logistics.delivery.repository.DeliveryRepository;
import msa.logistics.service.logistics.order.dto.request.OrderCreateRequestDto;
import msa.logistics.service.logistics.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;
@Log4j2
@AllArgsConstructor
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteService deliveryRouteService;
    private final HubService hubService;

    @Transactional
    public UUID createDelivery(String username, String roles, OrderCreateRequestDto orderCreateRequestDto, UUID orderId) {
        System.out.println("orderCreateRequestDto: " + orderCreateRequestDto);



        // 출발 업체
        VendorResponseDto startVendor = Optional.ofNullable(
                        hubService.getVendor(orderCreateRequestDto.getSupplierVendorId(), username, roles))
                .orElseThrow(() -> new CustomException(ErrorCode.VENDOR_NOT_FOUND));

        //도착업체
        VendorResponseDto DestinationVendor = Optional.ofNullable(
                        hubService.getVendor(orderCreateRequestDto.getReceiverVendorId(), username, roles))
                .orElseThrow(() -> new CustomException(ErrorCode.VENDOR_NOT_FOUND));



        String vendorAddress = DestinationVendor.getVendorAddress();


        // 배송 객체 생성
        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .deliveryAddress(vendorAddress)
                .deliveryStatus(DeliveryStatus.WAITING) // 기본값 설정
                .StartHubId(startVendor.getHubId())
                .destinationHubId(DestinationVendor.getHubId())
                .currentHubId(orderCreateRequestDto.getSupplierVendorId())
                .receiverSlackId(null)
                .receiverId(DestinationVendor.getVendorId())
                .build();

        // 엔티티 저장
        Delivery savedDelivery = deliveryRepository.save(delivery);

        // 배송 저장과 동시에 배송 경로 생성
        deliveryRouteService.createDeliveryRoute(delivery, savedDelivery.getDeliveryId(),username,roles);

        // 저장된 배송 객체 반환
        return savedDelivery.getDeliveryId();
    }

    // 배달 상세 조회
    @Transactional
    public Delivery getDeliveryById(UUID deliveryId) {
        // 엔티티 조회
        Delivery searchDelivery = deliveryRepository.findById(deliveryId).orElse(null);
        return searchDelivery;
    }

    // 배달 수정
    @Transactional
    public DeliveryResponseDto editDelivery(UUID deliveryId, DeliveryEditRequestDto deliveryEditRequestDto) {
        // 기존 배달 정보 조회
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "배송 정보를 찾을 수 없습니다."));

        Delivery updatedDelivery = Delivery.builder()
                .deliveryAddress(deliveryEditRequestDto.getDeliveryAddress() != null ? deliveryEditRequestDto.getDeliveryAddress() : delivery.getDeliveryAddress())
                .deliveryStatus(deliveryEditRequestDto.getDeliveryStatus() != null ? deliveryEditRequestDto.getDeliveryStatus() : delivery.getDeliveryStatus())
                .receiverSlackId(deliveryEditRequestDto.getReceiverSlackId() != null ? deliveryEditRequestDto.getReceiverSlackId() : delivery.getReceiverSlackId())
                .build();

        return DeliveryResponseDto.fromEntityForUpdate(updatedDelivery); // Delivery 객체 반환
    }

    // 배송 삭제
    @Transactional
    public void deleteDelivery(UUID deliveryId) {
        deliveryRepository.deleteById(deliveryId);
    }

    // 배송 목록 조회
    @Transactional
    public Page<DeliveryResponseDto> getAllDeliveries(int page, int limit, Boolean sort) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Delivery> deliveriePage = deliveryRepository.findAll(pageable);
        return deliveriePage.map(DeliveryResponseDto::fromEntity);
    }


}
