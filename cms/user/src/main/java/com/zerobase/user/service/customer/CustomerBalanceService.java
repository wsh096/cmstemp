package com.zerobase.user.service.customer;

import com.zerobase.user.domain.form.ChangeBalanceForm;
import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.model.CustomerBalanceHistory;
import com.zerobase.user.domain.repository.CustomerBalanceHistoryRepository;
import com.zerobase.user.domain.repository.CustomerRepository;
import com.zerobase.user.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.user.exception.ErrorCode.NOT_ENOUGH_BALANCE;
import static com.zerobase.user.exception.ErrorCode.NOT_FOUND_USER;

@Service
@RequiredArgsConstructor
public class CustomerBalanceService {
    private static final Integer ZERO = 0;

    private final CustomerBalanceHistoryRepository customerBalanceHistoryRepository;
    private final CustomerRepository customerRepository;

    @Transactional(noRollbackFor = {CustomException.class})
    public CustomerBalanceHistory changeBalance(Long customerId,
                                                ChangeBalanceForm form)
                                                throws CustomException {
        CustomerBalanceHistory customerBalanceHistory =
                customerBalanceHistoryRepository
                        .findFirstByCustomer_IdOrderByIdDesc(customerId)
                        .orElse(defaultCustomerBalanceHistory(customerId));

        isEnoughBalance(customerBalanceHistory, form);

        customerBalanceHistory = CustomerBalanceHistory.builder()
                .changeMoney(form.getMoney())
                .currentMoney(customerBalanceHistory
                        .getCurrentMoney() + form.getMoney())
                .description(form.getMessage())
                .fromMessage(form.getFrom())
                .customer(customerBalanceHistory.getCustomer())
                .build();
        customerBalanceHistory
                .getCustomer()
                .setBalance(customerBalanceHistory.getCurrentMoney());

        return customerBalanceHistoryRepository.save(customerBalanceHistory);
    }
    private CustomerBalanceHistory defaultCustomerBalanceHistory(Long customerId) {
        return CustomerBalanceHistory.builder()
                .changeMoney(ZERO)
                .currentMoney(ZERO)
                .customer(findCustomerById(customerId))
                .build();
    }
    private Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_USER));
    }
    private void isEnoughBalance(CustomerBalanceHistory customerBalanceHistory,
                                 ChangeBalanceForm form) {
        if(customerBalanceHistory.getCurrentMoney()
                + form.getMoney() < ZERO) {
            throw new CustomException(NOT_ENOUGH_BALANCE);
        }
    }


}
