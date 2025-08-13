package com.cdac.Service;

import com.cdac.Entity.Payment;
import com.cdac.Entity.PaymentStatus;
import com.cdac.Entity.Status;
import com.cdac.Exception.ResourceNotFoundException;
import com.cdac.dao.PaymentRepository;
import com.cdac.dto.PaymentDTO;
import com.cdac.Service.PaymentService;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment makePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id).orElse(null);
    }
    @Override
    public void updatePaymentStatus(Long id, String status) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with ID: " + id));

        try {
            PaymentStatus enumStatus = PaymentStatus.valueOf(status.toUpperCase()); // Use PaymentStatus here
            payment.setPaystatus(enumStatus); // Use correct setter
            paymentRepository.save(payment);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid payment status: " + status);
        }
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();

        return payments.stream()
                .map(payment -> new PaymentDTO(
                        payment.getPaymentId(),
                        payment.getOrder().getOrderId(),
                        payment.getAmount(),
                        payment.getPaymentDate(),
                        payment.getMethod(),     // Now of type PaymentMethod enum
                        payment.getPaystatus()   // Enum: PaymentStatus
                ))
                .collect(Collectors.toList());
    }


}
