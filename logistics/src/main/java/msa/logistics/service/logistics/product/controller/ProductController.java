package msa.logistics.service.logistics.product.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.product.service.ProductService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

}
