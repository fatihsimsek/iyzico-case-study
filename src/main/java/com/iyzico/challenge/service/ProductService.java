package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.DataNotFoundException;
import com.iyzico.challenge.exception.InsufficientStockException;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.model.request.ProductCreateRequest;
import com.iyzico.challenge.model.request.ProductUpdateRequest;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private MessageSource messageSource;

    public ProductService(ProductRepository productRepository,
                          MessageSource messageSource,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.messageSource = messageSource;
        this.productMapper = productMapper;
    }

    public ProductDto create(ProductCreateRequest request) {
        Product product = new Product();

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        productRepository.saveAndFlush(product);
        return this.productMapper.toDto(product);
    }

    public ProductDto update(Long id, ProductUpdateRequest request) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new DataNotFoundException(messageSource.getMessage("exception.datanotfound", new Object[]{id}, Locale.getDefault())));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        productRepository.save(product);
        return this.productMapper.toDto(product);
    }

    public void delete(Long id) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new DataNotFoundException(messageSource.getMessage("exception.datanotfound", new Object[]{id}, Locale.getDefault())));
        productRepository.delete(product);
    }

    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new DataNotFoundException(messageSource.getMessage("exception.datanotfound", new Object[]{id}, Locale.getDefault())));
    }

    public List<ProductDto> list() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(p -> productMapper.toDto(p)).collect(Collectors.toList());
    }

    public void updateStock(Product product, Integer change) {
        Integer newStock = product.getStock() - change;

        if (newStock < 0) {
            throw new InsufficientStockException();
        }

        product.setStock(newStock);

        productRepository.save(product);
    }
}
