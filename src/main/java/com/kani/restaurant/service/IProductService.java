package com.kani.restaurant.service;

import com.kani.restaurant.dto_two_way.ProductResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IProductService {
    ResponseEntity<String> addProduct(Map<String, String> requestMap);
    ResponseEntity<List<ProductResponseDto>> getAllProduct();
    ResponseEntity<String> updateProduct(Map<String, String> requestMap);
    ResponseEntity<String> deleteProduct(Long id);
    ResponseEntity<String> updateStatus(Map<String, String> requestMap);
    ResponseEntity<List<ProductResponseDto>> getCategoryById(Long id);
    ResponseEntity<ProductResponseDto> getProductById(Long id);
}
