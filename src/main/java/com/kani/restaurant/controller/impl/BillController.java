package com.kani.restaurant.controller.impl;

import com.kani.restaurant.controller.IBillController;
import com.kani.restaurant.dto_two_way.CafeResponse;
import com.kani.restaurant.entity.Bill;
import com.kani.restaurant.service.IBillService;
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
public class BillController implements IBillController {
    private final IBillService billService;

    @Override
    public ResponseEntity<String> generateBill(Map<String, String> requestMap) {
        try{
            billService.generateBill(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try{
            billService.getBills();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, String> requestMap) {
        try{
            billService.getPdf(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Long id) {
        try{
            billService.deleteBill(id);

        }catch (Exception ex ){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
