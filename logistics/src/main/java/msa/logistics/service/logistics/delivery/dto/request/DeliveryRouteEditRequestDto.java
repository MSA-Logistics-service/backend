package msa.logistics.service.logistics.delivery.dto.request;

import lombok.*;
import msa.logistics.service.logistics.delivery.domain.RouteStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DeliveryRouteEditRequestDto {


    private RouteStatus routeStatus;


}
