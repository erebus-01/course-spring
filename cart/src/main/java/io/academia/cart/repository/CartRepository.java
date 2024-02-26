package io.academia.cart.repository;

import io.academia.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByStudentId(UUID studentId);
}
