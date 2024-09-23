package msa.logistics.service.logistics.delivery.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import msa.logistics.service.logistics.client.hub.HubService;
import msa.logistics.service.logistics.client.hub.dto.HubPathResponseDto;
import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
import msa.logistics.service.logistics.common.exception.CustomException;
import msa.logistics.service.logistics.common.exception.ErrorCode;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.delivery.domain.DeliveryRoute;
import msa.logistics.service.logistics.delivery.dto.request.DeliveryRouteEditRequestDto;
import msa.logistics.service.logistics.delivery.dto.response.DeliveryRouteResponseDto;
//import msa.logistics.service.logistics.delivery.naver.NaverApiService;
import msa.logistics.service.logistics.delivery.naver.NaverApiService;
import msa.logistics.service.logistics.delivery.naver.NaverDto;
import msa.logistics.service.logistics.delivery.repository.DeliveryRepository;
import msa.logistics.service.logistics.delivery.repository.DeliveryRouteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
@Log4j2
public class DeliveryRouteService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteRepository deliveryRouteRepository;
    private final HubService hubService;
    private final NaverApiService naverApiService;


    @Transactional
    public void createDeliveryRoute(Delivery delivery, UUID deliveryId, String username, String roles) {


        String startHub = delivery.getStartHubId().toString();
        String endHub = delivery.getDestinationHubId().toString();
        log.info("startHubId: " + startHub + ", destinationHubId: " + endHub);

        // 예시 사용


        HubResponseDto startHub1 = hubService.getHub(delivery.getStartHubId(),username,roles);
        String start = startHub1.getHubLongitude() + "," + startHub1.getHubLatitude();

        HubResponseDto endHub1 = hubService.getHub(delivery.getDestinationHubId(),username,roles);
        String end = endHub1.getHubLongitude()+ "," + endHub1.getHubLatitude();

        log.info("start: " + start + ", end: " + end);
        NaverDto response = naverApiService.getDirection(start,end);

        // Naver API 응답 검증
        if (response == null || response.getDistance() == 0 || response.getDuration() == 0) {
            throw new IllegalStateException("Naver API로부터 유효한 경로 정보를 받아오지 못했습니다.");
        }

        System.out.println("소요 시간: " + response.getDuration() + "초");
        System.out.println("거리: " + response.getDistance() + "미터");


        List<HubPathResponseDto> hubPathData = Optional.ofNullable(
                        hubService.getHubPathsByStartAndEnd(startHub, endHub, username, roles))
                .orElseThrow(() -> new CustomException(ErrorCode.VENDOR_NOT_FOUND));


        int sequence = 1;

        // HubPathData에서 DeliveryRoute 엔티티 생성 및 저장
        for (HubPathResponseDto hubPath : hubPathData) {
            // DeliveryRoute 엔티티 생성 (이 엔티티는 실제로 JPA로 저장 가능한 엔티티여야 합니다)
            DeliveryRoute deliveryRoute = new DeliveryRoute();
            deliveryRoute.setDelivery(delivery);
            deliveryRoute.setSequence(sequence++);
            deliveryRoute.setStartHubId(hubPath.getStartHubId());
            deliveryRoute.setDestinationHubId(hubPath.getEndHubId());
            System.out.println("deliveryRoute: " + deliveryRoute);
            // 추가로 필요한 정보를 설정
            deliveryRoute.setActualDistance(response.getDistance());
            deliveryRoute.setActualDuration((int) response.getDuration());

            // 실제 저장
            deliveryRouteRepository.save(deliveryRoute);
        }

        System.out.println("Delivery ID: " + deliveryId + "에 대한 경로가 생성되었습니다.");
    }
    // 배송 상세 조회
    @Transactional
    public Optional<DeliveryRoute> getDeliveryRouteById(UUID deliveryRouteId) {
        Optional<DeliveryRoute> deliveryRoute = deliveryRouteRepository.findById(deliveryRouteId);
        return deliveryRoute;
    }

    // 수정
    @Transactional
    public DeliveryRouteResponseDto editDeliveryRoute(UUID deliveryRouteId, DeliveryRouteEditRequestDto deliveryRouteEditRequestDto) {
        DeliveryRoute deliveryRoute = deliveryRouteRepository.findById(deliveryRouteId).orElse(null);

        // 상품 수정 시 상품 상태 변경
        deliveryRoute.setCurrentStatus(deliveryRouteEditRequestDto.getRouteStatus());

        return DeliveryRouteResponseDto.fromEntityForUpdate(deliveryRoute);
    }

    // 배송 삭제
    @Transactional
    public void deleteDeliveryRoute(UUID deliveryRouteId) {
        deliveryRouteRepository.deleteById(deliveryRouteId);
    }

    // 배송 목록 조회
    @Transactional
    public Page<DeliveryRouteResponseDto> searchDeliveryRoutes(String filter, int page, int limit) {
        // 페이지 및 필터 설정
        Pageable pageable = PageRequest.of(page, limit);
        Page<DeliveryRoute> deliveryRoutePage = deliveryRouteRepository.findAll(pageable);
        return deliveryRoutePage.map(DeliveryRouteResponseDto::fromEntity);
    }


}