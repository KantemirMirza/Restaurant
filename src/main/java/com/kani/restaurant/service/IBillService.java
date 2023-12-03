package com.kani.restaurant.service;

import com.kani.restaurant.entity.Bill;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface IBillService {
    ResponseEntity<String> generateBill(Map<String, String> requestMap);
    ResponseEntity<List<Bill>> getBills();
    ResponseEntity<byte[]> getPdf(Map<String, String> requestMap);
    ResponseEntity<String> deleteBill(Long id);

}
