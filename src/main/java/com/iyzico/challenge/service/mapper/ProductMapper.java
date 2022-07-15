package com.iyzico.challenge.service.mapper;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.model.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductDto toDto(Product product){
        return ProductDto.builder()
                  .id(product.getId())
                  .name(product.getName())
                  .description(product.getDescription())
                  .stock(product.getStock())
                  .price(product.getPrice()).build();
    }
}
