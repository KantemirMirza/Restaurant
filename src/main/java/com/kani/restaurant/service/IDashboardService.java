package com.kani.restaurant.service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface IDashboardService {
    ResponseEntity<Map<String, Object>> getCount();

}
