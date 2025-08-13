package com.cdac.dao;

import com.cdac.Entity.Medicine;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {


	List<Medicine> findByStockLessThan(int i);

	List<Medicine> findByExpiryDateBefore(LocalDate today);

	List<Medicine> findAllByExpiryDateAfter(LocalDate now);
	
}