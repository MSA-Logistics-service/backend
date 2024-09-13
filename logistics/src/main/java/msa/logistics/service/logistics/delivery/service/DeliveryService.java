package msa.logistics.service.logistics.delivery.service;


import jakarta.transaction.Transactional;
import msa.logistics.service.logistics.delivery.domain.Delivery;

import msa.logistics.service.logistics.delivery.dto.DeliveryCreateRequestDto;
import msa.logistics.service.logistics.delivery.dto.DeliveryEditRequestDto;
import msa.logistics.service.logistics.delivery.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.UUID;


@Service
public class DeliveryService {

    //    private final VendorService vendorService;
    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

//    public DeliveryService(@Qualifier("vendorService") VendorService vendorService, DeliveryRepository deliveryRepository) {
//        this.vendorService = vendorService;
//        this.deliveryRepository = deliveryRepository;
//    }

    //배달 생성
    @Transactional
    public UUID createDelivery(DeliveryCreateRequestDto deliveryCreateRequestDto) {

        // 1. 배송지 주소 -> 업체 아이디를 통해 업체 주소 조회
//        String deliveryAddress = vendorService.getVendorAddressById(deliveryCreateRequestDto.getReceiverVenderId());
        String deliveryAddress = "123 Test St, Test City, Test Country"; // 하드코딩된 배송지 주소
        // 2. 주문 아이디
//        UUID orderId = deliveryCreateRequestDto.getOrderId();
        UUID orderId = UUID.fromString("123e4567-e89b-12d3-a456-426614174003"); // 하드코딩된 주문 아이디

        // 3. 출발 허브 아이디
//        UUID startHubId = vendorService.getHubIdByVendorId(deliveryCreateRequestDto.getSupplierVenderId());
        UUID startHubId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"); // 하드코딩된 출발 허브 아이디
        // 4. 도착 허브 아이디
//        UUID destinationHubId = vendorService.getHubIdByVendorId(deliveryCreateRequestDto.getReceiverVenderId());
        UUID destinationHubId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"); // 하드코딩된 도착 허브 아이디
        // 5. 수령자 아이디
//        UUID receiverId = vendorService.getReceiverIdByVendorId(deliveryCreateRequestDto.getReceiverVenderId());
        UUID receiverId = UUID.fromString("123e4567-e89b-12d3-a456-426614174002"); // 하드코딩된 수령자 아이디
        // 배송 객체 생성
        Delivery delivery = Delivery.builder()
                .orderId(orderId)
                .deliveryAddress(deliveryAddress)
                .deliveryStatus(Delivery.DeliveryStatus.WAITING) // 기본값 설정
                .StartHubId(startHubId)
                .destinationHubId(destinationHubId)
                .currentHubId(startHubId)
                .receiverSlackId(deliveryCreateRequestDto.getRecevierSlackId())
                .receiverId(receiverId)
                .build();


        // 엔티티 저장
        Delivery savedDelivery = deliveryRepository.save(delivery);

        // 저장된 배송 객체 반환
        return savedDelivery.getDeliveryId();
    }

    //배달 조회
    @Transactional
    public Delivery getDeliveryById(UUID deliveryId) {
        // 엔티티 조회
        Delivery searchDelivery = deliveryRepository.findById(deliveryId).orElse(null);

        return searchDelivery;
    }

    //    //배달 수정
    @Transactional
    public Delivery editDelivery(UUID deliveryId, DeliveryEditRequestDto deliveryEditRequestDto) {
        //기존 배달 정보 조회
        Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);

        //상품 수정 시 주소 직접 입력
        delivery.setDeliveryAddress(deliveryEditRequestDto.getDeliveryAddress());
        delivery.setDeliveryStatus(deliveryEditRequestDto.getDeliveryStatus());
        delivery.setReceiverSlackId(deliveryEditRequestDto.getRecevierSlackId());

        return delivery;
    }

    //배송 삭제
    @Transactional
    public void deleteDelivery(UUID deliveryId) {

        deliveryRepository.deleteById(deliveryId);
    }


    public Page<Delivery> getAllDeliveries(int page, int limit, Boolean sort) {

        Pageable pageable = PageRequest.of(page,limit);

        Page<Delivery> deliveriePage = deliveryRepository.findAll(pageable);

        Page<Delivery> deliveryDtoPage = deliveriePage.map(delivery -> {
            Delivery dto = new Delivery();
            dto.setDeliveryId(delivery.getDeliveryId());
            dto.setOrderId(delivery.getOrderId());
            dto.setDeliveryStatus(delivery.getDeliveryStatus());
            dto.setStartHubId(delivery.getStartHubId());
            dto.setDestinationHubId(delivery.getDestinationHubId());
            dto.setReceiverSlackId(delivery.getReceiverSlackId());
            dto.setReceiverId(delivery.getReceiverId());
            dto.setDeliveryAddress(delivery.getDeliveryAddress());
            dto.setDeliveryStatus(delivery.getDeliveryStatus());

            return dto;
        });


        return deliveryDtoPage;

    }
}









