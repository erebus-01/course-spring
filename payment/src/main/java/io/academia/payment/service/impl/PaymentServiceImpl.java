package io.academia.payment.service.impl;

import io.academia.payment.dto.PaymentRequest;
import io.academia.payment.dto.request.CreatePaymentRequest;
import io.academia.payment.dto.response.CartItemsResponse;
import io.academia.payment.dto.response.CartResponse;
import io.academia.payment.dto.response.PaymentResponse;
import io.academia.payment.model.Order;
import io.academia.payment.model.OrderItem;
import io.academia.payment.model.Payment;
import io.academia.payment.repository.OrderItemRepository;
import io.academia.payment.repository.OrderRepository;
import io.academia.payment.repository.PaymentRepository;
import io.academia.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final WebClient.Builder webClient;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public PaymentResponse createPayment(CreatePaymentRequest paymentRequest,
                                         String bank, String cardType,
                                         String orderInfo, String transactionStatus) {

        CartResponse cartResponse = webClient.build()
                .get()
                .uri("http://localhost:8900/api/cart/" + paymentRequest.getStudentId() + "/find-by-student")
                .retrieve()
                .bodyToMono(CartResponse.class)
                .block();

        Order order = Order.builder()
                .studentId(paymentRequest.getStudentId())
                .build();

        assert cartResponse != null;
        List<CartItemsResponse> cartItemsResponses = cartResponse.getItems();
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItemsResponse cartItem : cartItemsResponses) {
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .courseId(cartItem.getCourseId())
                    .courseName(cartItem.getCourseName())
                    .price(cartItem.getPrice())
                    .createAt(cartItem.getCreateAt())
                    .updateAt(cartItem.getUpdateAt())
                    .build();
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);
        order = orderRepository.save(order);

        double totalAmount = calculateTotalAmount(orderItems);
        Payment payment = Payment.builder()
                .order(order)
                .amount(totalAmount)
                .bank(bank)
                .cardType(cardType)
                .orderInfo(orderInfo)
                .transactionStatus(transactionStatus)
                .build();

        payment = paymentRepository.save(payment);

        webClient.build()
                .delete()
                .uri("http://localhost:8900/api/cart/" + paymentRequest.getStudentId() + "/remove-all-items")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .build();
    }

    private double calculateTotalAmount(List<OrderItem> orderItems) {
        // Calculate the total amount based on the order items
        return orderItems.stream().mapToDouble(OrderItem::getPrice).sum();
    }

}
