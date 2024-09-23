//package msa.logistics.service.logistics.client.fallback;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.openfeign.FallbackFactory;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class HubFallbackFactory implements FallbackFactory<HubFallback> {
//    @Override
//    public HubFallback create(Throwable cause) {
//        log.info(cause.toString());
//        return new HubFallback(cause);
//    }
//}
