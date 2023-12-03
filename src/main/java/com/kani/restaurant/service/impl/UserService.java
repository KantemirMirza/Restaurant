package com.kani.restaurant.service.impl;

import com.kani.restaurant.dto_two_way.CafeResponse;
import com.kani.restaurant.dto_two_way.UserResponseDto;
import com.kani.restaurant.entity.User;
import com.kani.restaurant.jwt.CustomerUserDetailsService;
import com.kani.restaurant.jwt.JwtAuthenticationFilter;
import com.kani.restaurant.jwt.JwtUtil;
import com.kani.restaurant.repository.IUserRepository;
import com.kani.restaurant.service.IUserService;
import com.kani.restaurant.util.CafeUtil;
import com.kani.restaurant.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final EmailUtil emailUtil;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userRepository.save(createUser(requestMap));
                    return CafeUtil.getResponseEntity(CafeResponse.CREATED_SUCCESSFULLY, HttpStatus.OK);
                } else {
                    return CafeUtil.getResponseEntity(CafeResponse.EMAIL_ALREADY_EXIST, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtil.getResponseEntity(CafeResponse.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    private boolean validateSignUpMap(Map<String, String> requestMap){
        return requestMap.containsKey("firstName") && requestMap.containsKey("lastName")
                && requestMap.containsKey("phone") && requestMap.containsKey("email") && requestMap.containsKey("password");
    }

    private User createUser(Map<String, String> requestMap){
        User user = new User();
        user.setFirstName(requestMap.get("firstName"));
        user.setLastName(requestMap.get("lastName"));
        user.setPhone(requestMap.get("phone"));
        user.setEmail("email");
        user.setPassword("password");
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if (auth.isAuthenticated()) {
                if (customerUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<>("{\"token\":\"" +
                           jwtUtil.generateToken(customerUserDetailsService.getUserDetails().getEmail(),
                                   customerUserDetailsService.getUserDetails().getRole()) + "\"}",
                        HttpStatus.OK );
                } else {
                    return new ResponseEntity<>("{\"message\":\"" + "Wait For The Admin Approval" + "\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
           log.error("", ex);
        }
        return new ResponseEntity<>("{\"message\":\"" + "Bad Credentials" + "\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserResponseDto>> getAllUser() {
        try{
            if(jwtAuthenticationFilter.isAdmin()){
                return new ResponseEntity<>( userRepository.getAllUser(), HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if(jwtAuthenticationFilter.isUser()){
               Optional<User> optionalUser = userRepository.findById(Long.parseLong(requestMap.get("id")));
               if(optionalUser.isPresent()){
                   userRepository.update(requestMap.get("status"), Long.parseLong(requestMap.get("id")));
               sendMailToAllAdmin(requestMap.get("status"), optionalUser.get().getEmail(), userRepository.getAllAdmin());
                   return CafeUtil.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
               }else{
                   return CafeUtil.getResponseEntity("User Not Found", HttpStatus.OK);
               }
            }else{
                return CafeUtil.getResponseEntity(CafeResponse.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtAuthenticationFilter.currentUsername());
        if(status != null && status.equalsIgnoreCase("true")){
            emailUtil.sendSimpleMailMessage(jwtAuthenticationFilter.currentUsername(), "Account Approved", "USER-" + user + "\nis Approved by \n ADMIN:-" + jwtAuthenticationFilter.currentUsername(), allAdmin);
        }else{
            emailUtil.sendSimpleMailMessage(jwtAuthenticationFilter.currentUsername(), "Account Disabled", "USER-" + user + "\nis Disabled by \n ADMIN:-" + jwtAuthenticationFilter.currentUsername(), allAdmin);
        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtil.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            User userObject = userRepository.findByEmail(jwtAuthenticationFilter.currentUsername());
          if(!userObject.equals(null)){
              if(userObject.getPassword().equals(requestMap.get("Old Password"))){
                  userObject.setPassword(requestMap.get("new Password"));
                  userRepository.save(userObject);
                  return CafeUtil.getResponseEntity("The Password Has Been Updated Successfully", HttpStatus.OK);
              }else{
                  return CafeUtil.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
              }
          }else{
              return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
          }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User userObject = userRepository.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(userObject) && !Strings.isNotEmpty(userObject.getEmail())){
                emailUtil.forgotPasswordEmail(userObject.getEmail(), "Credentials By Cafe Management System", userObject.getPassword());
                return CafeUtil.getResponseEntity("Check Your E-Mail For Credentials", HttpStatus.OK);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtil.getResponseEntity(CafeResponse.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
