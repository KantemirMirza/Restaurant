package com.kani.restaurant.repository;

import com.kani.restaurant.dto_two_way.ProductResponseDto;
import com.kani.restaurant.entity.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    List<ProductResponseDto> getAllProduct();
    @Modifying
    @Transactional
    Long updateProductStatus(@Param("status")String status, @Param("id") Long id);

    List<ProductResponseDto> getProductByCategoryId(@Param("id")Long id);

    ProductResponseDto  getProductById(@Param("id")Long id);
}
