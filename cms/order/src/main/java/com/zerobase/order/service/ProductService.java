package com.zerobase.order.service;

import com.zerobase.order.domain.entity.Product;
import com.zerobase.order.domain.entity.ProductItem;
import com.zerobase.order.domain.product.AddProductForm;
import com.zerobase.order.domain.product.UpdateProductForm;
import com.zerobase.order.domain.product.UpdateProductItemForm;
import com.zerobase.order.domain.repository.ProductRepository;
import com.zerobase.order.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.order.exception.ErrorCode.NOT_FOUND_ITEM;
import static com.zerobase.order.exception.ErrorCode.NOT_FOUND_PRODUCT;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    @Transactional
    public Product addProduct(Long sellerId, AddProductForm form) {
        return productRepository.save(Product.of(sellerId, form));
    }
    @Transactional
    public Product updateProduct(Long sellerId, UpdateProductForm form) {
        Product product = productRepository.findBySellerIdAndId(sellerId, form.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));

        product.setName(form.getName());
        product.setDescription(form.getDescription());

        for(UpdateProductItemForm itemForm : form.getItems()) {
            ProductItem item = product.getProductItems().stream()
                    .filter(pi -> pi.getId().equals(itemForm.getId()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(NOT_FOUND_ITEM));

            item.setName(itemForm.getName());
            item.setPrice(itemForm.getPrice());
            item.setCount(itemForm.getCount());
        }
        return product;
    }
    @Transactional
    public void deleteProduct(Long sellerId, Long productId) {
        Product product = productRepository.findBySellerIdAndId(sellerId, productId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_PRODUCT));
        productRepository.delete(product);
    }

}
