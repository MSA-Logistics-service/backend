package msa.logistics.service.logistics.product.repository;

import msa.logistics.service.logistics.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByVendorIdAndIsDeleteFalse(UUID vendorId);
    Optional<Product> findByProductIdAndIsDeleteFalse(UUID productId);
}
