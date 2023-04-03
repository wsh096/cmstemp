package com.zerobase.order.service;

import com.zerobase.order.domain.entity.Product;
import com.zerobase.order.domain.repository.ProductRepository;
import com.zerobase.order.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zerobase.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

@Service
@RequiredArgsConstructor
public class ProductSearchService {
    private final ProductRepository productRepository;

    public List<Product> searchByName(String name) {
        return productRepository.searchByName(name);
    }

    public Product getByProductId(long productId) {
        return productRepository.findWithProductItemsById(productId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
    }

    public List<Product> getListByProductIds(List<Long> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }

}
