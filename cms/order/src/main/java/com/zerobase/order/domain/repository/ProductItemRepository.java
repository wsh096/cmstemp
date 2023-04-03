package com.zerobase.order.domain.repository;

import com.zerobase.order.domain.entity.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

}
