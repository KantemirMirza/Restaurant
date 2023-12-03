package com.kani.restaurant.controller.impl;

import com.kani.restaurant.controller.IProductController;
import com.kani.restaurant.dto_two_way.CafeResponse;
import com.kani.restaurant.dto_two_way.ProductResponseDto;
import com.kani.restaurant.service.IProductService;
import com.kani.restaurant.util.CafeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProductController implements IProductController {

    private final IProductService productService;

    @Override
    public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
        try{
            return productService.addProduct(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductResponseDto>> getAllProduct() {
        try{
            return productService.getAllProduct();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            return productService.updateProduct(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long id) {
        try{
            productService.deleteProduct(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
            productService.updateStatus(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductResponseDto>> getCategoryById(Long id) {
        try{
            productService.getCategoryById(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductResponseDto> getProductById(Long id) {
        try{
            productService.getProductById(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductResponseDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
