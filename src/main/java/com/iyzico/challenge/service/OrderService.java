package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.InsufficientStockException;
import com.iyzico.challenge.model.request.OrderRequest;
import com.iyzico.challenge.service.lock.LockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private PaymentService paymentService;
    private ProductService productService;
    private MessageSource messageSource;
    private LockService lockService;

    @Autowired
    public OrderService(PaymentService paymentService,
                        ProductService productService,
                        LockService lockService,
                        MessageSource messageSource) {
        this.paymentService = paymentService;
        this.productService = productService;
        this.lockService = lockService;
        this.messageSource = messageSource;
    }

    public void create(OrderRequest orderRequest) {
        this.lockService.lock(orderRequest.getProductId());

        try {
            Product product = this.productService.findById(orderRequest.getProductId());

            if (product.getStock() < orderRequest.getQuantity()) {
                throw new InsufficientStockException();
            }

            BigDecimal price = product.getPrice().multiply(new BigDecimal(orderRequest.getQuantity()));

            this.paymentService.pay(price);

            this.productService.updateStock(product, orderRequest.getQuantity());

        } finally {
            this.lockService.release(orderRequest.getProductId());
        }

    }
}
