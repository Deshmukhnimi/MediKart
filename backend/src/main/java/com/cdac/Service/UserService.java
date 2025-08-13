
package com.cdac.Service;

import java.util.List;

import com.cdac.dto.*;

public interface UserService {

    UserDTO registerUser(UserRequestDTO dto);

    LoginResponseDTO login(LoginRequestDTO dto);

    List<UserDTO> getAllNonAdminUsers();

    List<MedicineDTO> getAllAvailableMedicines();

    CartDTO addToCart(AddToCartRequestDTO request, String userEmail);

    CartDTO getCart(String email); 

    CartDTO updateCartItem(UpdateCartItemRequestDTO request, String userEmail);

    void removeCartItem(Long cartItemId, String userEmail);

    OrderDTO placeOrder(String userEmail, PlaceOrderRequestDTO request);


    PaymentDTO makePayment(Long orderId, PaymentRequestDTO request);


    List<OrderDTO> getUserOrders(String userEmail);

    List<OrderStatusHistoryDTO> getOrderStatusHistory(Long orderId, String userEmail);

    OrderDTO cancelOrder(Long orderId, String userEmail);
}
