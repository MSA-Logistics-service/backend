//package msa.logistics.service.logistics.client.fallback;
//
//import feign.FeignException;
//import java.util.UUID;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import msa.logistics.service.logistics.client.hub.HubClient;
//import msa.logistics.service.logistics.client.hub.dto.HubResponseDto;
//import msa.logistics.service.logistics.client.vendor.domain.VendorType;
//import msa.logistics.service.logistics.client.vendor.dto.VendorResponseDto;
//
//@Slf4j
//@RequiredArgsConstructor
//public class HubFallback implements HubClient {
//
//    private final Throwable cause;
//
//    @Override
//    public HubResponseDto getHub(UUID hubId, String username, String roles) {
//        if (cause instanceof FeignException.Unauthorized) {
//            log.error("Not Found Error");
//        } else if (cause instanceof FeignException.NotFound) {
//            log.error("Not Found Error");
//        }
//        log.error("Failed to get Hub {}", hubId);
//        return new HubResponseDto(
//                hubId, // 요청받은 hubId를 그대로 반환
//                "Unavailable Hub", // 기본 Hub 이름
//                "No location available", // 기본 Hub 주소
//                0.0, // 기본 위도 값
//                0.0, // 기본 경도 값
//                0.0  // 기본 Hub 랭킹 값
//        );
//    }
//
//    @Override
//    public VendorResponseDto getVendor(UUID vendorId, String username, String roles) {
//        if (cause instanceof FeignException.Unauthorized) {
//            log.error("Not Found Error");
//        } else if (cause instanceof FeignException.NotFound) {
//            log.error("Not Found Error");
//        }
//        log.error("Failed to get Vendor {}", vendorId);
//        return new VendorResponseDto(
//                vendorId, // 요청받은 vendorId를 그대로 반환
//                "Unavailable Vendor", // 기본 Vendor 이름
//                VendorType.PRODUCER, // 기본 Vendor 타입
//                "No vendor address", // 기본 Vendor 주소
//                UUID.randomUUID(), // 기본 Hub ID
//                0L // 기본 userId
//        );
//    }
//
//}
