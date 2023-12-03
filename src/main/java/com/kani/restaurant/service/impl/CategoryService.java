package com.kani.restaurant.service.impl;

import com.kani.restaurant.dto_two_way.CafeResponse;
import com.kani.restaurant.entity.Category;
import com.kani.restaurant.jwt.JwtAuthenticationFilter;
import com.kani.restaurant.repository.ICategoryRepository;
import com.kani.restaurant.service.ICategoryService;
import com.kani.restaurant.util.CafeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final ICategoryRepository categoryRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
        try{
            if (jwtAuthenticationFilter.isAdmin()) {
                if(validateCategoryMap(requestMap, false)){
                    categoryRepository.save(getCategoryFromMap(requestMap, false));
                    return CafeUtil.getResponseEntity("Category has been Added Successfully", HttpStatus.OK);
                }

            } else {
                return CafeUtil.getResponseEntity(CafeResponse.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, Boolean validatedId) {

        if(requestMap.containsKey("categoryName")){
            if(requestMap.containsKey("id") && validatedId){
                return true;
            }else if(!validatedId){
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd){
        Category category = new Category();
        if(isAdd){
            category.setId(Long.parseLong(requestMap.get("id")));
        }else{
            category.setCategoryName(requestMap.get("categoryName"));
        }
        return category;
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            if(!Strings.isNotBlank(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("Ok Ok");
                return new ResponseEntity<>(categoryRepository.getAllCategory(), HttpStatus.OK);
            }
            return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
       }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            if(jwtAuthenticationFilter.isAdmin()){
               if(validateCategoryMap(requestMap, true)){
                   Optional<Category> optional = categoryRepository.findById(Long.parseLong(requestMap.get("id")));
                   if(optional.isPresent()){
                       categoryRepository.save(getCategoryFromMap(requestMap, true));
                       return CafeUtil.getResponseEntity("Category has been updated successfully", HttpStatus.OK);
                    }else{
                      return CafeUtil.getResponseEntity("Category id does not exist", HttpStatus.OK);
                   }
               }
                return CafeUtil.getResponseEntity(CafeResponse.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeUtil.getResponseEntity(CafeResponse.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
