package com.zerobase.order.domain.repository;

import com.zerobase.order.domain.entity.Product;
import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> searchByName(String name);
}
