package com.kani.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.io.Serializable;

@NamedQuery(name = "Product.getAllProduct",
        query = "SELECT NEW com.kani.restaurant.dto_two_way.ProductResponseDto(p.id, p.category.id, p.category, p.productName, p.brand, p.description, p.price, p.status) FROM Product p")

@NamedQuery(name = "Product.updateProductStatus", query = "UPDATE Product p SET p.status=:status WHERE p.id=:id")

@NamedQuery(name = "Product.getProductByCategoryId",
        query = "SELECT NEW com.kani.restaurant.dto_two_way.ProductResponseDto(p.id, p.productName) FROM Product p WHERE p.category.id=:id AND p.status='true'")
@NamedQuery(name = "Product.getProductById", query = "SELECT NEW com.kani.restaurant.dto_two_way.ProductResponseDto(p.id,p.productName,p.brand,p.description,p.price) FROM Product p WHERE p.id=:id")

@Data
@DynamicInsert @DynamicUpdate
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Product implements Serializable {

    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk", nullable = false)
    private Category category;

    @Column(nullable = false, length = 24)
    private String productName;

    @Column(nullable = false, length = 24)
    private String brand;

    @Column(nullable = false, length = 24)
    private String description;

    @Column(nullable = false, length = 24)
    private float price;

    @Column(nullable = false, length = 24)
    private String status;
}
