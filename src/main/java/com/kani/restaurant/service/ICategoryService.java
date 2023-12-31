package com.kani.restaurant.service;


import com.kani.restaurant.entity.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ICategoryService {

    ResponseEntity<String> addCategory(Map<String, String> requestMap);
    ResponseEntity<List<Category>> getAllCategory(String filterValue);
    ResponseEntity<String> updateCategory(Map<String, String> requestMap);
}
