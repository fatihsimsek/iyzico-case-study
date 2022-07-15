package com.iyzico.challenge.service;

import com.iyzico.challenge.entity.Product;
import com.iyzico.challenge.exception.InsufficientStockException;
import com.iyzico.challenge.model.dto.ProductDto;
import com.iyzico.challenge.model.request.ProductCreateRequest;
import com.iyzico.challenge.model.request.ProductUpdateRequest;
import com.iyzico.challenge.repository.ProductRepository;
import com.iyzico.challenge.service.mapper.ProductMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@EnableAutoConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private ProductMapper productMapper;
    @Autowired
    private ProductService productService;

    @Test
    public void should_product_create_success() {
        ProductCreateRequest request = new ProductCreateRequest();
        request.setName("Product1");
        request.setDescription("Product1-Desc");
        request.setStock(100);
        request.setPrice(BigDecimal.valueOf(1000));

        ProductDto givenValue = new ProductDto(1L, "Product1", "Product1-Desc", 100, BigDecimal.valueOf(1000));

        given(productRepository.saveAndFlush(any(Product.class))).willReturn(null);
        given(productMapper.toDto(any(Product.class))).willReturn(givenValue);

        ProductDto expectedValue = productService.create(request);
        Assert.isTrue(expectedValue != null, "ProductDto result must not be null");
        Assert.isTrue(expectedValue.getId().equals(givenValue.getId()), "GivenProduct and ExpectedProduct Ids must be equals");
    }

    @Test
    public void should_product_update_success() {
        ProductUpdateRequest request = new ProductUpdateRequest();
        request.setName("Product1");
        request.setDescription("Product1-Desc");
        request.setStock(100);
        request.setPrice(BigDecimal.valueOf(1000));

        Product product = new Product();
        product.setId(1L);
        product.setName("Product1");
        product.setDescription("Product1-Desc");
        product.setStock(100);
        product.setPrice(BigDecimal.valueOf(1000));

        ProductDto givenValue = new ProductDto(1L, "Product1", "Product1-Desc", 100, BigDecimal.valueOf(1000));

        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(productRepository.save(any())).willReturn(product);
        given(productMapper.toDto(any(Product.class))).willReturn(givenValue);

        ProductDto expectedValue = productService.update(1L, request);
        Assert.isTrue(expectedValue != null, "ProductDto result must not be null");
        Assert.isTrue(expectedValue.getId().equals(givenValue.getId()), "GivenProduct and ExpectedProduct Ids must be equals");
    }

    @Test
    public void should_product_delete_success() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Product1");
        product.setDescription("Product1-Desc");
        product.setStock(100);
        product.setPrice(BigDecimal.valueOf(1000));

        given(productRepository.findById(any())).willReturn(Optional.of(product));

        productService.delete(1L);
    }

    @Test
    public void should_product_findById_success() {
        Product givenValue = new Product();
        givenValue.setId(1L);
        givenValue.setName("Product1");
        givenValue.setDescription("Product1-Desc");
        givenValue.setStock(100);
        givenValue.setPrice(BigDecimal.valueOf(1000));

        given(productRepository.findById(any())).willReturn(Optional.of(givenValue));

        Product expectedValue = productService.findById(1L);

        Assert.isTrue(expectedValue != null, "Product result must not be null");
        Assert.isTrue(expectedValue.getId().equals(givenValue.getId()), "GivenProduct and ExpectedProduct Ids must be equals");
    }

    @Test
    public void should_product_list_success() {
        List<Product> givenValue = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setDescription("Product1-Desc");
        product1.setStock(100);
        product1.setPrice(BigDecimal.valueOf(1000));
        givenValue.add(product1);

        ProductDto givenValueDto = new ProductDto(1L, "Product1", "Product1-Desc", 100, BigDecimal.valueOf(1000));

        given(productRepository.findAll()).willReturn(givenValue);
        given(productMapper.toDto(any(Product.class))).willReturn(givenValueDto);

        List<ProductDto> expectedValue = productService.list();

        Assert.isTrue(expectedValue != null, "Product result must not be null");
        Assert.isTrue(expectedValue.size() == 1, "GivenProduct and ExpectedProduct size must be equals");
    }

    @Test
    public void should_product_updateStock_success() {
        Product givenValue = new Product();
        givenValue.setId(1L);
        givenValue.setName("Product1");
        givenValue.setDescription("Product1-Desc");
        givenValue.setStock(10);
        givenValue.setPrice(BigDecimal.valueOf(1000));

        given(productRepository.save(any(Product.class))).willReturn(givenValue);

        productService.updateStock(givenValue, 5);
    }

    @Test(expected = InsufficientStockException.class)
    public void should_product_updateStock_failed() {
        Product givenValue = new Product();
        givenValue.setId(1L);
        givenValue.setName("Product1");
        givenValue.setDescription("Product1-Desc");
        givenValue.setStock(10);
        givenValue.setPrice(BigDecimal.valueOf(1000));

        given(productRepository.save(any(Product.class))).willReturn(givenValue);

        productService.updateStock(givenValue, 15);
    }
}
