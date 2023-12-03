package com.kani.restaurant.controller;

import com.kani.restaurant.entity.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping(path = "/bill")
public interface IBillController {

    @PostMapping("/generateBill")
    ResponseEntity<String> generateBill(@RequestBody() Map<String, String> requestMap);

    @GetMapping("/getBills")
    ResponseEntity<List<Bill>> getBills();

    @PostMapping("/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody() Map<String, String> requestMap);

    @PostMapping("/delete/{id}")
    ResponseEntity<String> deleteBill(@PathVariable() Long id);
}
