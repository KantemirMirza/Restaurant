package com.kani.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;
@NamedQuery(name = "User.findByEmailId", query = "SELECT u FROM User u WHERE u.email=:email")

@NamedQuery(name = "User.getAllUser",
        query = "SELECT NEW com.kani.restaurant.dto_two_way.UserResponseDto(u.id, u.firstName, u.lastName, u.phone, u.email,u.status, u.role) FROM User u WHERE u.role='user'")
@NamedQuery(name = "User.update", query = "UPDATE User u SET u.status=:status WHERE u.id=:id")

@NamedQuery(name = "User.getAllAdmin",
        query = "SELECT u.email FROM User u WHERE u.role='admin'")


@Data
@DynamicInsert @DynamicUpdate
@NoArgsConstructor @AllArgsConstructor
@Entity
public class User implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false, length = 24)
    private String firstName;
    @Column(nullable = false, length = 24)
    private String lastName;
    @Column(nullable = false, length = 16)
    private String phone;
    @Column(nullable = false, length = 64)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, length = 24)
    private String status;
    @Column(nullable = false, length = 24)
    private String role;
}
