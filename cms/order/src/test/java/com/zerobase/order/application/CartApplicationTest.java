package com.zerobase.order.application;

import com.zerobase.order.config.RedisConfigTest;
import com.zerobase.order.domain.entity.Product;
import com.zerobase.order.domain.product.AddProductCartForm;
import com.zerobase.order.domain.product.AddProductForm;
import com.zerobase.order.domain.product.AddProductItemForm;
import com.zerobase.order.domain.redis.Cart;
import com.zerobase.order.domain.repository.ProductRepository;
import com.zerobase.order.service.ProductService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RedisConfigTest.class)
class CartApplicationTest {
    @Autowired
    private CartApplication cartApplication;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Test
    void AddTest(){

        long customerId = 100L;
        cartApplication.clearCart(customerId);

        Product p = add_product();
        Product result =
                productRepository.findWithProductItemsById(p.getId()).get();
        assertNotNull(result);

        assertEquals(result.getName(), "나이키 에어포스");
        assertEquals(result.getDescription(), "신발");
        assertEquals(result.getProductItems().size(), 3);
        assertEquals(result.getProductItems().get(0).getName(), "나이키 에어포스0");
        assertEquals(result.getProductItems().get(0).getPrice(), 10000);
        assertEquals(result.getProductItems().get(0).getCount(), 10);


        Cart cart = cartApplication.addCart(customerId, makeAddForm(result));
        // 데이터가 잘 들어 갔는지
        assertEquals(cart.getMessages().size(), 0);

        cart = cartApplication.getCart(customerId);
        assertEquals(cart.getMessages().size(), 1);
    }
    AddProductCartForm makeAddForm(Product p) {
        AddProductCartForm.ProductItem productItem =
                AddProductCartForm.ProductItem.builder()
                        .id(p.getProductItems().get(0).getId())
                        .name(p.getProductItems().get(0).getName())
                        .count(5)
                        .price(20000)
                        .build();
        return AddProductCartForm.builder()
                .id(p.getId())
                .sellerId(p.getSellerId())
                .name(p.getName())
                .description(p.getDescription())
                .items(List.of(productItem))
                .build();
    }

    Product add_product() {

        Long sellerId = 1L;
        AddProductForm form = makeProductForm(
                "나이키 에어포스",
                "신발",
                3);
        return productService.addProduct(sellerId, form);
    }
    private static AddProductForm makeProductForm(String name,
                                                  String description,
                                                  int itemCount) {
        List<AddProductItemForm> itemForms = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            itemForms.add(makeProductItemForm(null, name + i));
        }
        return AddProductForm.builder()
                .name(name)
                .description(description)
                .items(itemForms)
                .build();

    }

    private static AddProductItemForm makeProductItemForm(Long productId,
                                                          String name) {
        return AddProductItemForm.builder()
                .productId(productId)
                .name(name)
                .price(10000)
                .count(10)
                .build();
    }
    @Test
    void addCart() {
    }

    @Test
    void updateCart() {
    }

    @Test
    void getCart() {
    }

    @Test
    void clearCart() {
    }

    @Test
    void refreshCart() {
    }
}