package com.kani.restaurant.repository;

import com.kani.restaurant.dto_two_way.UserResponseDto;
import com.kani.restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface IUserRepository extends JpaRepository<User,Long> {
    User findByEmailId(@Param("email") String email);
    List<UserResponseDto> getAllUser();
    List<String> getAllAdmin();
    @Transactional
    @Modifying
    Long update(@Param("status")String status, @Param("id") Long id);
    User findByEmail(String email);
}
