package io.academia.cart.controller;

import io.academia.cart.dto.request.CartItemsRequest;
import io.academia.cart.dto.request.CartRequest;
import io.academia.cart.dto.response.CartResponse;
import io.academia.cart.service.CartService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    @CircuitBreaker(name = "course", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "course")
    @Retry(name = "course")
    public CompletableFuture<ResponseEntity<CartResponse>> createCart(@RequestBody CartRequest cartRequest) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.ok(cartService.createCart(cartRequest)));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long cartId) {
        CartResponse cartResponse = cartService.getCart(cartId);
        return ResponseEntity.ok(cartResponse);
    }

    @GetMapping("/{studentId}/find-by-student")
    public ResponseEntity<CartResponse> getCartByStudentId(@PathVariable("studentId") UUID studentId) {
        CartResponse cartResponse = cartService.getCartByStudent(studentId);
        return ResponseEntity.ok(cartResponse);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartResponse> addToCart(@PathVariable Long cartId,
                                                  @RequestBody CartItemsRequest cartItemRequest) {
        CartResponse cartResponse = cartService.addToCart(cartId, cartItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }

    @GetMapping("/{studentId}/total")
    public ResponseEntity<Double> getTotalPriceForStudent(@PathVariable UUID studentId) {
        Double totalPrice = cartService.calculateTotalPriceForStudent(studentId);
        return ResponseEntity.ok(totalPrice);
    }

    @DeleteMapping("/{cartId}/items/{courseId}")
    public ResponseEntity<CartResponse> removeFromCart(@PathVariable("cartId") Long cartId,
                                                       @PathVariable("courseId") UUID courseId) {
        CartResponse cartResponse = cartService.removeFromCart(cartId, courseId);
        return ResponseEntity.ok(cartResponse);
    }

    @DeleteMapping("/{studentId}/remove-all-items")
    public ResponseEntity<String> removeAllItemsFromCart(@PathVariable UUID studentId) {
        String message = cartService.removeAllItemsFromCart(studentId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    public CompletableFuture<ResponseEntity<CartResponse>> fallbackMethod(CartRequest cartRequest, RuntimeException runtimeException) {
        CartResponse fallbackResponse = new CartResponse();
        fallbackResponse.setMessage("Oops! An error occurred while trying to connect to course-service");
        fallbackResponse.setItems(Collections.emptyList());
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fallbackResponse));
    }
}
