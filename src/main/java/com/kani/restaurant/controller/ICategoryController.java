package com.kani.restaurant.controller;

import com.kani.restaurant.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/category")
public interface ICategoryController {
    @PostMapping("/add")
    ResponseEntity<String> addCategory(@RequestBody() Map<String, String> requestMap);

    @GetMapping("/get")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

    @PostMapping("/update")
    ResponseEntity<String> updateCategory(@RequestBody() Map<String, String> requestMap);
}
