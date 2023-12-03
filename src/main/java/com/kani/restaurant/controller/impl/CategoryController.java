package com.kani.restaurant.controller.impl;

import com.kani.restaurant.controller.ICategoryController;
import com.kani.restaurant.dto_two_way.CafeResponse;
import com.kani.restaurant.entity.Category;
import com.kani.restaurant.service.ICategoryService;
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
public class CategoryController implements ICategoryController {
    private final ICategoryService categoryService;

    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
        try{
           return categoryService.addCategory(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            return categoryService.getAllCategory(filterValue);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            return categoryService.updateCategory(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
