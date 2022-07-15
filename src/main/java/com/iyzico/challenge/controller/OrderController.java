package com.iyzico.challenge.controller;

import com.iyzico.challenge.model.request.OrderRequest;
import com.iyzico.challenge.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    @Transactional
    public ResponseEntity create(@Valid @RequestBody OrderRequest orderRequest){
        orderService.create(orderRequest);
        return ResponseEntity.ok().build();
    }
}
