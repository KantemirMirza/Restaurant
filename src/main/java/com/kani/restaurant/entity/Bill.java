package com.kani.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;

@NamedQuery(name = "Bill.getAllBills", query = "SELECT b FROM Bill b ORDER BY b.id DESC")
@NamedQuery(name = "Bill.getBillByUserName", query = "SELECT b FROM Bill b WHERE b.createdBy=:username ORDER BY b.id DESC")

@Data
@DynamicInsert @DynamicUpdate
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Bill implements Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 24)
    private String uuid;

    @Column(nullable = false, length = 24)
    private String billName;

    @Column(nullable = false, length = 32)
    private String email;

    @Column(nullable = false, length = 24)
    private String contactNumber;

    @Column(nullable = false, length = 24)
    private String paymentMethod;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Double totalAmount;

    @Column(nullable = false, length = 64)
    private String productDetail;

    @Column(nullable = false, length = 32)
    private String createdBy;
}
