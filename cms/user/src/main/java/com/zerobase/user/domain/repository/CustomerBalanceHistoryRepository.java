package com.zerobase.user.domain.repository;

import java.util.Optional;

import com.zerobase.user.domain.model.CustomerBalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestParam;

public interface CustomerBalanceHistoryRepository extends JpaRepository<CustomerBalanceHistory, Long> {
    Optional<CustomerBalanceHistory> findFirstByCustomer_IdOrderByIdDesc(@RequestParam("customer_id") Long customerId);
}
