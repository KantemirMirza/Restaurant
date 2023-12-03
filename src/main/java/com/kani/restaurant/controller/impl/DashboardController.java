package com.kani.restaurant.controller.impl;


import com.kani.restaurant.controller.IDashboardController;
import com.kani.restaurant.service.IDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DashboardController implements IDashboardController {
    private final IDashboardService dashboardService;

    @Override
    public ResponseEntity<Map<String, Object>> getBills() {
        try{
            dashboardService.getCount();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
