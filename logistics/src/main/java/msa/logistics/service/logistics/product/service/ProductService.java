package msa.logistics.service.logistics.product.service;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


}
