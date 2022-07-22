package com.iyzico.challenge.controller;

import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.model.request.ProductCreateRequest;
import com.iyzico.challenge.model.request.ProductUpdateRequest;
import com.iyzico.challenge.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity list(){
        List<ProductDto> productDtos = productService.list();
        return ResponseEntity.ok().body(productDtos);
    }

    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@Valid @RequestBody ProductCreateRequest request) {
        ProductDto productDto = productService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @PutMapping("/{id}")
    public  ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequest request) {
        ProductDto productDto = productService.update(id, request);
        return ResponseEntity.ok().body(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }
}
