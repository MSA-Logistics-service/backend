package msa.logistics.service.logistics.product.repository;

import msa.logistics.service.logistics.product.domain.ProductAiChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductAiRepository extends JpaRepository<ProductAiChat, UUID> {
}
