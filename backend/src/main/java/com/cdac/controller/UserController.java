package com.cdac.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cdac.Service.UserService;
import com.cdac.dto.AddToCartRequestDTO;
import com.cdac.dto.CartDTO;
import com.cdac.dto.CartItemDTO;
import com.cdac.dto.MedicineDTO;
import com.cdac.dto.OrderDTO;
import com.cdac.dto.OrderStatusHistoryDTO;
import com.cdac.dto.PaymentDTO;
import com.cdac.dto.PaymentRequestDTO;
import com.cdac.dto.PlaceOrderRequestDTO;
import com.cdac.dto.UpdateCartItemRequestDTO;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/medicines")
    public List<MedicineDTO> getAvailableMedicines() {
        return userService.getAllAvailableMedicines();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/cart/add")
    public CartDTO addToCart(@RequestBody AddToCartRequestDTO request, Principal principal) {
        return userService.addToCart(request, principal.getName());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/cart")
    public CartDTO getCart(Principal principal) {
        return userService.getCart(principal.getName());
    }


    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart/update")
    public CartDTO updateCartItem(@RequestBody UpdateCartItemRequestDTO request, Principal principal) {
        return userService.updateCartItem(request, principal.getName());
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/cart/remove/{cartItemId}")
    public void removeCartItem(@PathVariable Long cartItemId, Principal principal) {
        userService.removeCartItem(cartItemId, principal.getName());
    }
    

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/place-order")
    public OrderDTO placeOrder(@RequestBody PlaceOrderRequestDTO request, Principal principal) {
        return userService.placeOrder(principal.getName(), request);
    }


    @PreAuthorize("hasRole('USER')")
    @PostMapping("/payment/{orderId}")
    public PaymentDTO makePayment(
            @PathVariable Long orderId,
            @Valid @RequestBody PaymentRequestDTO request
    ) {
        if (!orderId.equals(request.getOrderId())) {
            throw new IllegalArgumentException("Order ID in path and body do not match");
        }

        return userService.makePayment(orderId, request);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders")
    public List<OrderDTO> getUserOrders(Principal principal) {
        return userService.getUserOrders(principal.getName());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders/{orderId}/status")
    public List<OrderStatusHistoryDTO> getOrderStatusHistory(@PathVariable Long orderId, Principal principal) {
        return userService.getOrderStatusHistory(orderId, principal.getName());
    }
    

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/orders/{orderId}/cancel")
    public OrderDTO cancelOrder(@PathVariable Long orderId, Principal principal) {
        return userService.cancelOrder(orderId, principal.getName());
    }
}