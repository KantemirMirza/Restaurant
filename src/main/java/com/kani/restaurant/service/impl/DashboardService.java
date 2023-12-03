package com.kani.restaurant.service.impl;

import com.kani.restaurant.repository.IBillRepository;
import com.kani.restaurant.repository.ICategoryRepository;
import com.kani.restaurant.repository.IProductRepository;
import com.kani.restaurant.service.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService implements IDashboardService {
    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;
    private final IBillRepository billRepository;
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map = new HashMap<>();
        map.put("category", categoryRepository.count());
        map.put("product", productRepository.count());
        map.put("bill", billRepository.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
