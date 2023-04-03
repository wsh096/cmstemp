package com.zerobase.order.service;

import com.zerobase.order.domain.entity.Product;
import com.zerobase.order.domain.product.AddProductForm;
import com.zerobase.order.domain.product.AddProductItemForm;
import com.zerobase.order.domain.product.UpdateProductForm;
import com.zerobase.order.domain.product.UpdateProductItemForm;
import com.zerobase.order.domain.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@Transactional
@SpringBootTest
class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    void addProductTest() {

        Product p = add_product();
        Product result =
                productRepository.findWithProductItemsById(p.getId()).get();
        assertNotNull(result);

        assertEquals(result.getName(), "나이키 에어포스");
        assertEquals(result.getDescription(), "신발");
        assertEquals(result.getProductItems().size(), 3);
        assertEquals(result.getProductItems().get(0).getName(), "나이키 에어포스0");
        assertEquals(result.getProductItems().get(0).getPrice(), 10000);
        assertEquals(result.getProductItems().get(0).getCount(), 1);
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
                .count(1)
                .build();
    }

    @Test
        //CustomException: 상품을 찾을 수 없습니다. 기본을 추가해줘도 나오는 부분, 추후 수정
    void updateProductTest() {
        Long sellerId = 1L;

        UpdateProductForm form = changeProductForm(
                "나이키 에어포스 한정판",
                "신발 한정판",
                5);

        Product p = productService.updateProduct(sellerId, form);

        Product result = productRepository.findBySellerIdAndId(
                p.getSellerId(), p.getId()).get();

        assertNotNull(result);

        assertEquals(result.getName(), "나이키 에어포스 한정판");
        assertEquals(result.getDescription(), "신발 한정판");
        assertEquals(result.getProductItems().size(), 5);
        assertEquals(result.getProductItems().get(0).getName(), "나이키 에어포스0");
        assertEquals(result.getProductItems().get(0).getPrice(), 100001);
        assertEquals(result.getProductItems().get(0).getCount(), 10);
    }
//    Product change_product(){
//        Product p = add_product();
//        UpdateProductForm form = changeProductForm(p.getId(),p.getName());
//
//      return productService.updateProduct(p.getSellerId(), p);
//    }
    private static UpdateProductForm changeProductForm(String name,
                                                       String description,
                                                       int itemCount) {
        List<UpdateProductItemForm> itemForms = new ArrayList<>();
        for (int i = 0; i < itemCount; i++) {
            itemForms.add(changeProductItemForm(null, name + i));
        }
        return UpdateProductForm.builder()
                .name(name)
                .description(description)
                .items(itemForms)
                .build();
    }

    private static UpdateProductItemForm changeProductItemForm(Long id,
                                                               String name) {
        return UpdateProductItemForm.builder()
                .id(id)
                .name(name)
                .price(100001)
                .count(10)
                .build();
    }

    @Test
    void deleteProductTest() {
        Long sellerId = 1L;

        AddProductForm form = makeProductForm(
                "나이키 에어포스",
                "신발",
                3);

        Product p = productService.addProduct(sellerId, form);
        productService.deleteProduct(p.getSellerId(), p.getId());

        assertEquals(p.getName(), null);//비어야 하는데 ㅠ
        assertEquals(p.getDescription(), null);
        assertEquals(p.getProductItems().size(), 0);
    }
}

