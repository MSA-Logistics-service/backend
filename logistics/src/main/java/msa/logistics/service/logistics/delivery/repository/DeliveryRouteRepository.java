package msa.logistics.service.logistics.delivery.repository;

import msa.logistics.service.logistics.delivery.domain.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, UUID> {
}
