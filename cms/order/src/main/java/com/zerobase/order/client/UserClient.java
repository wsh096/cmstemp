package com.zerobase.order.client;

import com.zerobase.order.client.order.OrderListMainForm;
import com.zerobase.order.client.user.ChangeBalanceForm;
import com.zerobase.order.client.user.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
@FeignClient(name = "user", url = "${feign.client.url.user}")
public interface UserClient {
    @GetMapping("/customer/getinfo")
    ResponseEntity<CustomerDto> getCustomerInfo(@RequestHeader(name = "X-AUTH-TOKEN")
                                                String token);
    @GetMapping("/customer/balance")
    ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X-AUTH-TOKEN")
                                  String token, @RequestBody ChangeBalanceForm form);
    @PostMapping("/mail/send/orderlist")
    ResponseEntity<Void> sendmailOfOrderList(@RequestHeader(name = "X-AUTH-TOKEN")
                                  String token, @RequestBody OrderListMainForm orderInfo);

}
