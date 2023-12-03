package com.kani.restaurant.repository;

import com.kani.restaurant.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IBillRepository extends JpaRepository<Bill,Long> {
    List<Bill> getAllBills();
    List<Bill> getBillByUserName(@Param("username")String username);

}
