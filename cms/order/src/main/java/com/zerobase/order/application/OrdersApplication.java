package com.zerobase.order.application;

import com.zerobase.order.client.UserClient;
import com.zerobase.order.client.order.OrderListMainForm;
import com.zerobase.order.client.user.ChangeBalanceForm;
import com.zerobase.order.client.user.CustomerDto;
import com.zerobase.order.domain.entity.ProductItem;
import com.zerobase.order.domain.redis.Cart;
import com.zerobase.order.exception.CustomException;
import com.zerobase.order.service.ProductItemService;
import com.zerobase.order.success.CustomSuccess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.IntStream;

import static com.zerobase.order.exception.ErrorCode.ORDER_FAIL_BALANCE_PROBLEM;
import static com.zerobase.order.exception.ErrorCode.ORDER_FAIL_CHECK_CART;
import static com.zerobase.order.success.SuccessCode.THANK_YOU_FOR_YOUR_ORDER;

@Service
@RequiredArgsConstructor
public class OrdersApplication {
    private final CartApplication cartApplication;
    private final UserClient userClient;
    private final ProductItemService productItemService;
    // 결제를 위해 필요한 것
    // 1번 : 물건들이 전부 주문 가능한 상태인지 확인
    // 2번 : 가격 변돌이 있었는지에 대해 확인
    // 3번 : 고객의 돈이 충분한지
    // 4번 : 결제 & 상품의 재고 관리
    @Transactional
    public void order(String token, Cart cart) {
        Cart orderCart = cartApplication.refreshCart(cart);

        if(orderCart.getMessages().size() > 0) {
            // 문제가 있음
            throw new CustomException(ORDER_FAIL_CHECK_CART);
        }
        CustomerDto customerDto = userClient.getCustomerInfo(token).getBody();

        int totalPrice = getTotalPrice(cart);
        if(customerDto.getBalance() < totalPrice) {
            throw new CustomException(ORDER_FAIL_BALANCE_PROBLEM);
        }
        userClient.changeBalance(token,
                ChangeBalanceForm.builder()
                        .from("USER")
                        .message("Order")
                        .money(-totalPrice)
                        .build());

        for(Cart.Product product : orderCart.getProducts()) {
            for(Cart.ProductItem cartItem : product.getItems()) {
                ProductItem productItem = productItemService
                        .getProductItem(cartItem.getId());
                productItem.setCount(productItem.getCount()-cartItem.getCount());
            }
        }
        // 주문 후, 상품 내역 보내기
        userClient.sendmailOfOrderList(token,
                OrderListMainForm.of(customerDto.getEmail(), cart.getProducts(), totalPrice));
        // 이 위치에서도 먼저 실행되기 떄문에, db 변화를 못 시키는 것으로 보임
        // throw new CustomSuccess(THANK_YOU_FOR_YOUR_ORDER);
    }
    public Integer getTotalPrice(Cart cart) {
        return cart.getProducts().stream()
                .flatMapToInt(product -> product.getItems().stream()
                        .flatMapToInt(productItem -> IntStream.of(
                                productItem.getPrice()*productItem.getCount())))
                .sum();
    }
}
