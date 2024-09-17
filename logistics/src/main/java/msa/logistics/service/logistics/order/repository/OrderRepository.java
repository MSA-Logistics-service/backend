package msa.logistics.service.logistics.order.repository;

import msa.logistics.service.logistics.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
