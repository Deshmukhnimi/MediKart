package com.cdac.dao;

import com.cdac.Entity.Category;
import com.cdac.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByCategory(Category category);
	List<User> findByCategory(Category user);
}