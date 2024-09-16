package msa.logistics.service.logistics.order.service;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.delivery.repository.DeliveryRepository;
import msa.logistics.service.logistics.order.repository.OrderRepository;
import msa.logistics.service.logistics.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;

}
