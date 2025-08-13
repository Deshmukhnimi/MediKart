package com.cdac.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.cdac.Entity.Medicine;
import com.cdac.Entity.Status;
import com.cdac.Service.MedicineService;
import com.cdac.Service.OrderService;
import com.cdac.Service.PaymentService;
import com.cdac.Service.UserService;
import com.cdac.dto.MedicineDTO;
import com.cdac.dto.MedicineRequestDTO;
import com.cdac.dto.MedicineResponseDTO;
import com.cdac.dto.OrderDTO;
import com.cdac.dto.PaymentDTO;
import com.cdac.dto.UserDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final MedicineService medicineService;
    private final OrderService orderService;
    private final PaymentService paymentService;

   //USER MANAGEMENT

 // Only ADMINs can view users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllNonAdminUsers());
    }

    
    // MEDICINE MANAGEMENT

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addmedicine")
    public ResponseEntity<String> addMedicine(@RequestBody MedicineDTO medicineDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        String email = userDetails.getUsername();

        // This calls the service to save the medicine to DB
        String message = medicineService.addMedicine(medicineDTO, email);

        return ResponseEntity.ok(message);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/medicines/remove-expired")
    public ResponseEntity<String> removeExpiredMedicines() {
        int removedCount = medicineService.removeExpiredMedicines();
        return ResponseEntity.ok(removedCount + " expired medicines removed successfully.");
    }


    // And so on for:
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/medicines")
    public ResponseEntity<List<MedicineDTO>> getAllMedicines() {
        return ResponseEntity.ok(medicineService.getAllMedicines());
    }

    
    //ORDER MANAGEMENT
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestParam Status status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok("Order status updated to " + status.name());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok("Order has been cancelled.");
    }
    
    
    //PAYMENT MANAGEMENT

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/payments")
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/payments/{id}/status")
    public ResponseEntity<String> updatePaymentStatus(@PathVariable Long id, @RequestParam String status) {
        paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok("Payment status updated.");
    }
    
    
    
    //INVENTORY MANAGEMENT
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/medicines/low-stock")
    public ResponseEntity<List<MedicineDTO>> getLowStockMedicines() {
        return ResponseEntity.ok(medicineService.getLowStockMedicines());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/medicines/expired")
    public ResponseEntity<Object> getExpiredMedicines() {
        return ResponseEntity.ok(medicineService.getExpiredMedicines());
    }
}
