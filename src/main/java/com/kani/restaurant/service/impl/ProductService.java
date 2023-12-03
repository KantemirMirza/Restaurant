package com.kani.restaurant.service.impl;

import com.kani.restaurant.dto_two_way.CafeResponse;
import com.kani.restaurant.dto_two_way.ProductResponseDto;
import com.kani.restaurant.entity.Category;
import com.kani.restaurant.entity.Product;
import com.kani.restaurant.jwt.JwtAuthenticationFilter;
import com.kani.restaurant.repository.IProductRepository;
import com.kani.restaurant.service.IProductService;
import com.kani.restaurant.util.CafeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Override
    public ResponseEntity<String> addProduct(Map<String, String> requestMap) {
        try {
            if (jwtAuthenticationFilter.isAdmin()) {
                if(validateProductMap(requestMap, false)){
                    productRepository.save(getProductFromMap(requestMap, false));
                    return CafeUtil.getResponseEntity("Product has been Added Successfully", HttpStatus.OK);
                }else{
                    return CafeUtil.getResponseEntity(CafeResponse.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtil.getResponseEntity(CafeResponse.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductResponseDto>> getAllProduct() {
        try{
            return new ResponseEntity<>(productRepository.getAllProduct(), HttpStatus.OK);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try {
            if (jwtAuthenticationFilter.isAdmin()) {
                if(validateProductMap(requestMap, false)){
                    Optional<Product> optional = productRepository.findById(Long.parseLong(requestMap.get("id")));
                    if(optional.isPresent()){
                        Product product = getProductFromMap(requestMap, true);
                        product.setStatus(optional.get().getStatus());
                        productRepository.save(product);
                        return CafeUtil.getResponseEntity("Product has been updated successfully", HttpStatus.OK);
                    }else{
                        return  CafeUtil.getResponseEntity("Product Id Not Exist", HttpStatus.NOT_FOUND);
                    }
                }else{
                    return CafeUtil.getResponseEntity(CafeResponse.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtil.getResponseEntity(CafeResponse.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Long id) {
        try{
            if(jwtAuthenticationFilter.isAdmin()){
                Optional<Product> product = productRepository.findById(id);
                if(product.isPresent()){
                    productRepository.deleteById(id);
                    return CafeUtil.getResponseEntity("Product has been deleted successfully", HttpStatus.OK);
                }else{
                    return CafeUtil.getResponseEntity("Product Id Not Exist", HttpStatus.NOT_FOUND);
                }
            }else{
                return CafeUtil.getResponseEntity(CafeResponse.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
           if (jwtAuthenticationFilter.isAdmin()){
               Optional<Product> optional = productRepository.findById(Long.parseLong(requestMap.get("id")));
               if(optional.isPresent()){
                   productRepository.updateProductStatus(requestMap.get("status"), Long.parseLong(requestMap.get("id")));
                   return CafeUtil.getResponseEntity("Product Status has been updated successfully", HttpStatus.OK);
               }else {
                   return CafeUtil.getResponseEntity("Product Id Not Exist", HttpStatus.NOT_FOUND);
               }
           }else{
               return CafeUtil.getResponseEntity(CafeResponse.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
           }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductResponseDto>> getCategoryById(Long id) {
        try{
            return new ResponseEntity<>(productRepository.getProductByCategoryId(id), HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductResponseDto> getProductById(Long id) {
        try{
            return new ResponseEntity<>(productRepository.getProductById(id), HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ProductResponseDto(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateProductMap(Map<String, String> requestMap, Boolean validatedId) {

        if(requestMap.containsKey("productName")){
            if(requestMap.containsKey("id") && validatedId){
                return true;
            }else if(!validatedId){
                return true;
            }
        }
        return false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, Boolean isAdd){
        Category category = new Category();
        category.setId(Long.parseLong(requestMap.get("categoryId")));

        Product product = new Product();
        if(isAdd){
            product.setId(Long.parseLong(requestMap.get("id")));
        }else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setProductName(requestMap.get("productName"));
        product.setBrand(requestMap.get("brand"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Float.parseFloat(requestMap.get("price")));
        return product;
    }
}
