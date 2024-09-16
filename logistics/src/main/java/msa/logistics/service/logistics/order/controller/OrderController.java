package msa.logistics.service.logistics.order.controller;

import lombok.RequiredArgsConstructor;
import msa.logistics.service.logistics.order.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


}
