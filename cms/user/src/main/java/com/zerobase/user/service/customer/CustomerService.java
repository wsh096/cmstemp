package com.zerobase.user.service.customer;

import java.util.Optional;

import com.zerobase.user.domain.model.Customer;
import com.zerobase.user.domain.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    public Optional<Customer> findByIdAndEmail(Long id, String email) {//now
        return customerRepository.findById(id)
                .stream().filter(customer -> customer.getEmail().equals(email))
                .findFirst();
    }
    public Optional<Customer> findValidCustomer(String email, String password) {
        return customerRepository.findByEmail(email).stream()
                .filter(customer ->
                        customer.getPassword().equals(password)
                        && customer.isVerify())
                .findFirst();
    }
}
