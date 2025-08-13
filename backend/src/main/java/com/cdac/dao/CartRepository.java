package com.cdac.dao;

import com.cdac.Entity.Cart;
import com.cdac.Entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

	Optional<Cart> findByUser(User user);

	
}
