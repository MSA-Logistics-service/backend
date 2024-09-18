package msa.logistics.service.logistics.delivery.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.delivery.dto.request.DeliveryEditRequestDto;
import msa.logistics.service.logistics.delivery.domain.DeliveryStatus;
import msa.logistics.service.logistics.delivery.dto.response.DeliveryResponseDto;
import msa.logistics.service.logistics.delivery.repository.DeliveryRepository;
import msa.logistics.service.logistics.order.dto.request.OrderCreateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@AllArgsConstructor
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteService deliveryRouteService;

    // 배달 생성
    @Transactional
    public UUID createDelivery(OrderCreateRequestDto orderCreateRequestDto, UUID orderId) {
        System.out.println("orderCreateRequestDto: " + orderCreateRequestDto);
        // 1. 배송지 주소 -> 업체 아이디를 통해 업체 주소 조회
        // String deliveryAddress = vendorService.getVendorAddressById(orderCreateRequestDto.getSupplierVendorId());
        String deliveryAddress = "123 Test St, Test City, Test Country"; // 하드코딩된 배송지 주소

        // 3. 출발 허브 아이디
        UUID startHubId = orderCreateRequestDto.getSupplierVendorId();
        // 4. 도착 허브 아이디
        UUID destinationHubId = orderCreateRequestDto.getReceiverVendorId();
        // 5. 수령자 아이디
        // UUID receiverId = vendorService.getReceiverIdByVendorId(destinationHubId);
        UUID receiverId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002"); // 하드코딩된 수령자 아이디

        // 배송 객체 생성
        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .deliveryAddress(deliveryAddress)
                .deliveryStatus(DeliveryStatus.WAITING) // 기본값 설정
                .StartHubId(startHubId)
                .destinationHubId(destinationHubId)
                .currentHubId(startHubId)
                .receiverSlackId(null)
                .receiverId(receiverId)
                .build();

        // 엔티티 저장
        Delivery savedDelivery = deliveryRepository.save(delivery);

        // 배송 저장과 동시에 배송 경로 생성
        deliveryRouteService.createDeliveryRoute(delivery, savedDelivery.getDeliveryId());

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
