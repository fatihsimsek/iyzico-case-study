package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.model.request.OrderRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @MockBean
    private PaymentService paymentService;
    @MockBean
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Test
    public void should_order_create_success() {
        OrderRequest request = new OrderRequest();
        request.setProductId(1L);
        request.setQuantity(10);

        Product givenValue = new Product();
        givenValue.setId(1L);
        givenValue.setName("Product1");
        givenValue.setDescription("Product1-Desc");
        givenValue.setStock(100);
        givenValue.setPrice(BigDecimal.valueOf(1000));

        given(productService.findById(any())).willReturn(givenValue);

        this.orderService.create(request);
    }
}
