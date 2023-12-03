package com.kani.restaurant.controller;

import com.kani.restaurant.dto_two_way.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/product")
public interface IProductController {
    @PostMapping("/add")
    ResponseEntity<String> addProduct(@RequestBody() Map<String, String> requestMap);

    @GetMapping("/get")
    ResponseEntity<List<ProductResponseDto>> getAllProduct();

    @PostMapping("/update")
    ResponseEntity<String> updateProduct(@RequestBody() Map<String, String> requestMap);

    @PostMapping("/delete{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Long id);

    @PostMapping("/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody() Map<String, String> requestMap);

    @GetMapping("/getCategoryBy/{id}")
    ResponseEntity<List<ProductResponseDto>> getCategoryById(@PathVariable Long id);

    @GetMapping("/getProductBy/{id}")
    ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id);
}
