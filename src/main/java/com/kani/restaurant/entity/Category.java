package com.kani.restaurant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "Category.getAllCategory", query = "SELECT c FROM Category c WHERE c.id IN(SELECT p.category FROM Product p WHERE p.status='true')")
@Data
@DynamicInsert @DynamicUpdate
@NoArgsConstructor @AllArgsConstructor
@Entity
public class Category {
    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 24)
    private String categoryName;
}
