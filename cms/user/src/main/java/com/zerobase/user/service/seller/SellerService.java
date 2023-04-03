package com.zerobase.user.service.seller;

import com.zerobase.user.domain.model.Seller;
import com.zerobase.user.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;
    public Optional<Seller> findByIdAndEmail(Long id, String email){
        return sellerRepository.findById(id)
                .stream()
                .filter(seller -> seller.getEmail().equals(email))
                .findFirst();
    }
    public Optional<Seller> findValidSeller(String email, String password){
        return sellerRepository
                .findByEmailAndPasswordAndVerifyIsTrue(email, password);
    }


}
