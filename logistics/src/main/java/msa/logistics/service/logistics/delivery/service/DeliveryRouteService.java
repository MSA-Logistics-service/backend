package msa.logistics.service.logistics.delivery.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import msa.logistics.service.logistics.delivery.domain.Delivery;
import msa.logistics.service.logistics.delivery.domain.DeliveryRoute;
import msa.logistics.service.logistics.delivery.dto.request.DeliveryRouteEditRequestDto;
import msa.logistics.service.logistics.delivery.dto.HubPathData;
import msa.logistics.service.logistics.delivery.dto.response.DeliveryRouteResponseDto;
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
public class DeliveryRouteService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryRouteRepository deliveryRouteRepository;
    private final NaverApiService naverApiService;


    // 하드코딩된 데이터를 사용한 createDeliveryRoute 메서드
    @Transactional
    public void createDeliveryRoute(Delivery delivery, UUID deliveryId) {


        // 예시 사용
        String start = "127.1059968,37.3597093";// 출발 허브의 위도, 경도
        String end = "129.0764276,35.1795108"; // 도착 허브의 위도, 경도
        //startHub 위도,경도  destination 위도,경도 받아서 naver 서비스 전달

        NaverDto response = naverApiService.getDirection(start,end);

        // Naver API 응답 검증
        if (response == null || response.getDistance() == 0 || response.getDuration() == 0) {
            throw new IllegalStateException("Naver API로부터 유효한 경로 정보를 받아오지 못했습니다.");
        }

        System.out.println("소요 시간: " + response.getDuration() + "초");
        System.out.println("거리: " + response.getDistance() + "미터");




        UUID startHubId = deliveryRepository.findById(deliveryId).get().getStartHubId();
        UUID destinationHubId = deliveryRepository.findById(deliveryId).get().getDestinationHubId();
        System.out.println("startHubId: " + startHubId + ", destinationHubId: " + destinationHubId);
        System.out.println("destinationHubId: " + destinationHubId);

        // 예시 데이터 설정: 하드 코딩된 경로 리스트 생성
        List<HubPathData> mockedRoutes = new ArrayList<>();
        mockedRoutes.add(new HubPathData(60, 100.0, startHubId, UUID.fromString("00000000-0000-0000-0000-000000000002"))); // 서울 -> 경기
        mockedRoutes.add(new HubPathData(90, 150.0, UUID.fromString("00000000-0000-0000-0000-000000000002"), UUID.fromString("00000000-0000-0000-0000-000000000003"))); // 경기 -> 충청
        mockedRoutes.add(new HubPathData(120, 200.0, UUID.fromString("00000000-0000-0000-0000-000000000003"), destinationHubId)); // 충청 -> 부산

        int sequence = 1;

        // HubPathData에서 DeliveryRoute 엔티티 생성 및 저장
        for (HubPathData hubPathData : mockedRoutes) {
            // DeliveryRoute 엔티티 생성 (이 엔티티는 실제로 JPA로 저장 가능한 엔티티여야 합니다)
            DeliveryRoute deliveryRoute = new DeliveryRoute();
            deliveryRoute.setDelivery(delivery);
            deliveryRoute.setSequence(sequence++);
            deliveryRoute.setStartHubId(hubPathData.getStartHubId());
            deliveryRoute.setDestinationHubId(hubPathData.getDestinationHubId());
            System.out.println("deliveryRoute: " + deliveryRoute);
            // 추가로 필요한 정보를 설정
            // deliveryRoute.setEstimatedDistance(hubPathData.getEstimatedDistance());
            // deliveryRoute.setDuration(hubPathData.getDuration());

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