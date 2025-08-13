package com.cdac.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cdac.Entity.Cart;
import com.cdac.Entity.CartItem;
import com.cdac.Entity.Category;
import com.cdac.Entity.Medicine;
import com.cdac.Entity.Order;
import com.cdac.Entity.OrderStatusHistory;
import com.cdac.Entity.Payment;
import com.cdac.Entity.PaymentMethod;
import com.cdac.Entity.PaymentStatus;
import com.cdac.Entity.Status;
import com.cdac.Entity.User;
import com.cdac.Exception.ResourceNotFoundException;
import com.cdac.Security.JwtTokenUtil;
import com.cdac.dao.CartItemRepository;
import com.cdac.dao.CartRepository;
import com.cdac.dao.MedicineRepository;
import com.cdac.dao.OrderRepository;
import com.cdac.dao.OrderStatusHistoryRepository;
import com.cdac.dao.PaymentRepository;
import com.cdac.dao.UserRepository;
import com.cdac.dto.AddToCartRequestDTO;
import com.cdac.dto.CartDTO;
import com.cdac.dto.CartItemDTO;
import com.cdac.dto.LoginRequestDTO;
import com.cdac.dto.LoginResponseDTO;
import com.cdac.dto.MedicineDTO;
import com.cdac.dto.OrderDTO;
import com.cdac.dto.OrderStatusHistoryDTO;
import com.cdac.dto.PaymentDTO;
import com.cdac.dto.PaymentRequestDTO;
import com.cdac.dto.PlaceOrderRequestDTO;
import com.cdac.dto.UpdateCartItemRequestDTO;
import com.cdac.dto.UserDTO;
import com.cdac.dto.UserRequestDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final MedicineRepository medicineRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderStatusHistoryRepository historyRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO registerUser(UserRequestDTO dto) {
        boolean adminExists = userRepo.existsByCategory(Category.ADMIN);

        if (adminExists) {
            dto.setCategory(Category.USER);
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setCategory(dto.getCategory());
        user.setAddress(dto.getAddress());
        user.setContactNo(dto.getContactNo());

        user = userRepo.save(user);

        return new UserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getCategory(), user.getAddress(), user.getContactNo());
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtTokenUtil.generateToken(user.getEmail(), user.getCategory().name());

        return new LoginResponseDTO(token, user.getCategory().name());
    }

    @Override
    public List<UserDTO> getAllNonAdminUsers() {
        List<User> users = userRepo.findAll();

        return users.stream()
                .filter(user -> user.getCategory() != Category.ADMIN)
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCategory(),
                        user.getAddress(),
                        user.getContactNo()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicineDTO> getAllAvailableMedicines() {
        List<Medicine> meds = medicineRepository.findAllByExpiryDateAfter(LocalDate.now());
        return meds.stream().map(med -> modelMapper.map(med, MedicineDTO.class)).collect(Collectors.toList());
    }

    @Override
    public CartDTO addToCart(AddToCartRequestDTO request, String userEmail) {
        User user = userRepo.findByEmail(userEmail).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElse(new Cart(user));
        Medicine med = medicineRepository.findById(request.getMedicineId()).orElseThrow();

        CartItem item = new CartItem(cart, med, request.getQuantity());
        cart.getCartItems().add(item);
        cartRepository.save(cart);

        return modelMapper.map(cart, CartDTO.class);
    }
    
    
    
    @Override
    public CartDTO getCart(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        Cart cart = user.getCart();
        if (cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            // return empty cart structure
            CartDTO emptyCart = new CartDTO();
            emptyCart.setCartId(null);
            emptyCart.setUserId(user.getUserId());
            emptyCart.setTotalAmount(0);
            emptyCart.setItems(List.of());
            return emptyCart;
        }

        List<CartItemDTO> items = cart.getCartItems().stream().map(cartItem -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setCartItemId(cartItem.getCartItemId());
            dto.setMedicineId(cartItem.getMedicine().getMedicineId());
            dto.setMedicineName(cartItem.getMedicine().getName());
            dto.setUnitPrice(cartItem.getMedicine().getPrice());
            dto.setQuantity(cartItem.getQuantity());
            dto.setTotalPrice(cartItem.getMedicine().getPrice() * cartItem.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        // ✅ Correct place to declare and calculate totalAmount
        double totalAmount = items.stream()
                .mapToDouble(CartItemDTO::getTotalPrice)
                .sum();

        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getCartId());
        cartDTO.setUserId(user.getUserId());
        cartDTO.setItems(items);
        cartDTO.setTotalAmount(totalAmount);

        return cartDTO;
    }

    
    @Override
    public CartDTO updateCartItem(UpdateCartItemRequestDTO request, String userEmail) {
        CartItem item = cartItemRepository.findById(request.getCartItemId()).orElseThrow();
        item.setQuantity(request.getNewQuantity());
        cartItemRepository.save(item);
        return getCart(userEmail);
    }

    @Override
    public void removeCartItem(Long cartItemId, String userEmail) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public OrderDTO placeOrder(String userEmail, PlaceOrderRequestDTO request) {
        // 🔹 Fetch user by email
        User user = userRepo.findByEmail(userEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔹 Fetch user's cart
        Cart cart = cartRepository.findByUser(user)
            .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty. Cannot place order.");
        }

        // 🔹 Calculate total amount from cart
        double total = cart.getCartItems().stream()
            .mapToDouble(item -> item.getMedicine().getPrice() * item.getQuantity())
            .sum();

        // 🔹 Create order entity
        Order order = new Order(user, LocalDate.now(), total, Status.ORDERED);

        // 🔹 Determine payment status based on method
        PaymentStatus status = (request.getMethod() == PaymentMethod.COD)
            ? PaymentStatus.PENDING  // Will be marked PAID after delivery
            : PaymentStatus.PENDING; // For ONLINE, still pending until payment gateway confirms

        // 🔹 Create payment entity
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(total);
        payment.setPaymentDate(LocalDate.now());
        payment.setMethod(request.getMethod());
        payment.setPaystatus(status);

        // 🔹 Set payment in order (bidirectional)
        order.setPayment(payment);

        // 🔹 Save order (cascades and saves payment)
        Order savedOrder = orderRepository.save(order);

        // 🔹 Save order status history
        OrderStatusHistory history = OrderStatusHistory.builder()
            .order(savedOrder)
            .oldStatus(Status.NONE)
            .newStatus(Status.ORDERED)
            .changedAt(LocalDateTime.now())
            .build();
        historyRepository.save(history);

        // 🔹 Clear cart
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.getCartItems().clear(); // Important: clear the in-memory list as well
        cartRepository.save(cart); 

        // 🔹 Return response DTO
        return modelMapper.map(savedOrder, OrderDTO.class);
    }


    @Transactional
    @Override
    public PaymentDTO makePayment(Long orderId, PaymentRequestDTO dto) {

        if (!orderId.equals(dto.getOrderId())) {
            throw new IllegalArgumentException("Order ID in path and body do not match");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Double orderAmount = order.getTotalAmount();

        // Check if payment already exists
        Optional<Payment> existingPaymentOpt = paymentRepository.findByOrder(order);
        if (existingPaymentOpt.isPresent()) {
            Payment existingPayment = existingPaymentOpt.get();

            // Already paid
            if (existingPayment.getPaystatus() == PaymentStatus.PAID) {
                throw new RuntimeException("Payment already completed for this order");
            }

            // Handle PENDING payment
            if (dto.getMethod() != PaymentMethod.COD) {
                // User is now paying online for a previously COD-pending order
                existingPayment.setMethod(dto.getMethod());
                existingPayment.setPaystatus(PaymentStatus.PAID);
                existingPayment.setPaymentDate(LocalDate.now());
                existingPayment.setAmount(orderAmount); // optional update
                paymentRepository.save(existingPayment);
                return modelMapper.map(existingPayment, PaymentDTO.class);
            }

            // Still trying to use COD again while it's already pending
            throw new RuntimeException("COD payment is already pending for this order. Cannot reinitiate.");
        }

        // If no previous payment exists, create new
        Payment payment = Payment.builder()
                .order(order)
                .amount(orderAmount)
                .method(dto.getMethod())
                .paymentDate(LocalDate.now())
                .paystatus(dto.getMethod() == PaymentMethod.COD ? PaymentStatus.PENDING : PaymentStatus.PAID)
                .build();

        paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentDTO.class);
    }




    @Override
    public List<OrderDTO> getUserOrders(String userEmail) {
        User user = userRepo.findByEmail(userEmail).orElseThrow();
        return orderRepository.findByUser(user)
                .stream().map(o -> modelMapper.map(o, OrderDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<OrderStatusHistoryDTO> getOrderStatusHistory(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        return historyRepository.findByOrder(order)
                .stream()
                .map(h -> modelMapper.map(h, OrderStatusHistoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO cancelOrder(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        if (order.getStatus() == Status.NONE) {
            Status old = order.getStatus();
            order.setStatus(Status.CANCELLED);
            orderRepository.save(order);

            historyRepository.save(OrderStatusHistory.builder()
                    .order(order)
                    .oldStatus(old)
                    .newStatus(Status.CANCELLED)
                    .changedAt(LocalDateTime.now())
                    .build());
        }
        return modelMapper.map(order, OrderDTO.class);
    }
    
   

}
