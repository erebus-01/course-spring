package io.academia.cart.service;

import io.academia.cart.dto.request.CartItemsRequest;
import io.academia.cart.dto.request.CartRequest;
import io.academia.cart.dto.response.CartResponse;

import java.util.UUID;

public interface CartService {
    CartResponse createCart(CartRequest cartRequest);
    CartResponse getCart(Long cartId);
    CartResponse getCartByStudent(UUID studentId);
    CartResponse updateCart(Long cartId, CartRequest cartRequest);
    Double calculateTotalPriceForStudent(UUID studentId);
    CartResponse addToCart(Long cartId, CartItemsRequest cartItemRequest);
    CartResponse removeFromCart(Long cartId, UUID courseId);
    String removeAllItemsFromCart(UUID studentId);
    void deleteCart(Long cartId);
}
