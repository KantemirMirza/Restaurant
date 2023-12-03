package com.kani.restaurant.jwt;

import com.kani.restaurant.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;
    private com.kani.restaurant.entity.User user;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {} ", email);
        user = userRepository.findByEmailId(email);
        if(!Objects.isNull(user)){
            return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
        }else{
            throw new UsernameNotFoundException("User Not Found");
        }
    }

    public com.kani.restaurant.entity.User getUserDetails(){
        return user;
    }
}
