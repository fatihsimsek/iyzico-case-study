package com.iyzico.challenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.model.request.ProductCreateRequest;
import com.iyzico.challenge.model.request.ProductUpdateRequest;
import com.iyzico.challenge.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    public void should_product_create_OK() throws Exception {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Product1");
        request.setDescription("Product1-Desc");
        request.setStock(10);
        request.setPrice(BigDecimal.valueOf(100));

        ProductDto givenValue = new ProductDto(1L, "Product1", "Product1-Desc", 100, BigDecimal.valueOf(1000));

        given(productService.create(any(ProductCreateRequest.class))).willReturn(givenValue);

        this.mockMvc.perform(post("/api/v1/product")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated())
         .andExpect(content()
                .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name",is("Product1")));
    }

    @Test
    public void should_product_update_OK() throws Exception {
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Product1");
        request.setDescription("Product1-Desc");
        request.setStock(10);
        request.setPrice(BigDecimal.valueOf(100));

        ProductDto givenValue = new ProductDto(1L, "Product1", "Product1-Desc", 100, BigDecimal.valueOf(1000));

        given(productService.update(any(Long.class), any(ProductUpdateRequest.class))).willReturn(givenValue);

        this.mockMvc.perform(put("/api/v1/product/1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name",is("Product1")));
    }

    @Test
    public void should_product_delete_OK() throws Exception {
        this.mockMvc.perform(delete("/api/v1/product/1")
                .contentType("application/json")
        ).andExpect(status().isOk());
    }

    @Test
    public void should_product_list_OK() throws Exception {
        List<ProductDto> productDtos = new ArrayList<>();
        ProductDto productDto1 = new ProductDto(1L, "Product1", "Product1-Desc", 100, BigDecimal.valueOf(1000));
        productDtos.add(productDto1);

        given(productService.list()).willReturn(productDtos);

        this.mockMvc.perform(get("/api/v1/product")
        ).andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name",is("Product1")));
    }
}
