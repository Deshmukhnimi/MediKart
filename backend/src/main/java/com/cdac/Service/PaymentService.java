package com.cdac.Service;

import java.util.List;

import com.cdac.Entity.Payment;
import com.cdac.dto.PaymentDTO;

public interface PaymentService {
    Payment makePayment(Payment payment);
    Payment getPaymentById(Long id);
    
    //Correct
	void updatePaymentStatus(Long id, String status);
	List<PaymentDTO> getAllPayments();
}
