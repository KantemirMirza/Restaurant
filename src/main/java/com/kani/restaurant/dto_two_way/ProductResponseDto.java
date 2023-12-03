package com.kani.restaurant.dto_two_way;

import com.kani.restaurant.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class ProductResponseDto {

    private Long id;

    private Long categoryId;

    private Category category;

    private String productName;

    private String brand;

    private String description;

    private float price;

    private String status;

    public ProductResponseDto(Long id, String productName) {
        this.id = id;
        this.productName = productName;
    }

    public ProductResponseDto(Long id, String productName, String brand, String description, float price) {
        this.id = id;
        this.productName = productName;
        this.brand = brand;
        this.description = description;
        this.price = price;
    }
}
