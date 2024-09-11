package msa.logistics.service.logistics.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryRoute extends JpaRepository<DeliveryRoute, UUID> {
}
