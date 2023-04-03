package com.zerobase.user.domain.repository;

import java.util.Optional;

import com.zerobase.user.domain.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByIdAndEmail(Long id, String email);
    Optional<Seller> findByEmailAndPasswordAndVerifyIsTrue(String email, String password);
    Optional<Seller> findByEmail(String email);
}
