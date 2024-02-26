package io.academia.cart.service.impl;

import io.academia.cart.dto.request.CartItemsRequest;
import io.academia.cart.dto.request.CartRequest;
import io.academia.cart.dto.response.CartItemsResponse;
import io.academia.cart.dto.response.CartResponse;
import io.academia.cart.dto.response.CourseResponse;
import io.academia.cart.model.Cart;
import io.academia.cart.model.CartItem;
import io.academia.cart.repository.CartRepository;
import io.academia.cart.service.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final WebClient.Builder webClient;

    @Override
    public CartResponse createCart(CartRequest cartRequest) {

        Cart existingCart = cartRepository.findByStudentId(cartRequest.getStudentId());

        if (existingCart == null) {
            Cart newCart = new Cart();
            newCart.setStudentId(cartRequest.getStudentId());

            CartResponse updatedNewCart = getCartResponse(cartRequest, newCart);
            if (updatedNewCart != null) return updatedNewCart;

            cartRequest.getItems().forEach(item -> {
                CartItem cartItem = new CartItem();
                cartItem.setCourseId(item.getCourseId());
                newCart.addItem(cartItem);
            });

            Cart savedCart = cartRepository.save(newCart);
            return mapToCartResponse(savedCart);
        }

        CartResponse updatedCart = getCartResponse(cartRequest, existingCart);
        if (updatedCart != null) return updatedCart;

        return null;
    }

    private CartResponse getCartResponse(CartRequest cartRequest, Cart cart) {
        List<UUID> courseIds = cartRequest.getItems().stream()
                .map(CartItemsRequest::getCourseId)
                .toList();

        String courseIdsString = courseIds.stream()
                .map(UUID::toString)
                .collect(Collectors.joining(","));

        CourseResponse[] courseResponses = webClient.build()
                .get()
                .uri("http://localhost:8500/api/course/list-course-id?courseIds={courseIds}",
                        courseIdsString)
                .retrieve()
                .bodyToMono(CourseResponse[].class)
                .block();

        if (courseResponses != null && courseResponses.length > 0) {
            for (CourseResponse courseResponse : courseResponses) {
                boolean courseExists = cart.getItems().stream()
                        .anyMatch(item -> item.getCourseId().equals(courseResponse.getId()));

                if (!courseExists) {
                    CartItem cartItem = new CartItem();
                    cartItem.setCourseId(courseResponse.getId());
                    cartItem.setCourseName(courseResponse.getName());
                    cartItem.setPrice(courseResponse.getPrice());
                    cart.addItem(cartItem);
                }
            }

            Cart updatedCart = cartRepository.save(cart);
            return mapToCartResponse(updatedCart);
        }
        return null;
    }

    @Override
    public CartResponse getCart(Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            return mapToCartResponse(optionalCart.get());
        } else {
            throw new EntityNotFoundException("Cart not found with id: " + cartId);
        }
    }

    @Override
    public CartResponse getCartByStudent(UUID studentId) {
        Cart optionalCart = cartRepository.findByStudentId(studentId);
        if (optionalCart != null) {
            return mapToCartResponse(optionalCart);
        } else {
            throw new EntityNotFoundException("Cart not found with id: " + studentId);
        }       }

    @Override
    public CartResponse updateCart(Long cartId, CartRequest cartRequest) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.setItems(mapCartItemRequests(cartRequest.getItems()));
            Cart updatedCart = cartRepository.save(cart);
            return mapToCartResponse(updatedCart);
        } else {
            throw new EntityNotFoundException("Cart not found with id: " + cartId);
        }
    }

    @Override
    public Double calculateTotalPriceForStudent(UUID studentId) {
        Cart cart = cartRepository.findByStudentId(studentId);
        if (cart == null) {
            return 0.0;
        }

        double totalPrice = 0.0;
        for (CartItem item : cart.getItems()) {
            totalPrice += item.getPrice();
        }
        return totalPrice;
    }

    @Override
    public CartResponse addToCart(Long cartId, CartItemsRequest cartItemRequest) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = mapToCartItem(cartItemRequest);
        cart.addItem(cartItem);
        cartRepository.save(cart);
        return mapToCartResponse(cart);

    }

    @Override
    public CartResponse removeFromCart(Long cartId, UUID courseId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(EntityNotFoundException::new);
        cart.removeItem(courseId);
        cartRepository.save(cart);
        return mapToCartResponse(cart);
    }

    @Override
    public String removeAllItemsFromCart(UUID studentId) {
        Cart cart = cartRepository.findByStudentId(studentId);
        if (cart == null) {
            return "Cart empty!";
        }
        cart.getItems().clear();
        cartRepository.save(cart);
        return "Clear all items in cart!";
    }

    @Override
    public void deleteCart(Long cartId) {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            cartRepository.delete(optionalCart.get());
        } else {
            throw new EntityNotFoundException("Cart not found with id: " + cartId);
        }
    }

    private List<CartItem> mapCartItemRequests(List<CartItemsRequest> itemRequests) {
        return itemRequests.stream()
                .map(this::mapToCartItem)
                .collect(Collectors.toList());
    }

    private CartItem mapToCartItem(CartItemsRequest itemRequest) {
        CartItem cartItem = new CartItem();
        cartItem.setCourseId(itemRequest.getCourseId());
        return cartItem;
    }

    private CartResponse mapToCartResponse(Cart cart) {
        CartResponse cartResponse = new CartResponse();
        cartResponse.setId(cart.getId());
        cartResponse.setItems(mapToCartItemResponses(cart.getItems()));
        cartResponse.setCreateAt(cart.getCreateAt());
        cartResponse.setUpdateAt(cart.getUpdateAt());
        cartResponse.setMessage("Add items to cart successfully!");
        return cartResponse;
    }

    private List<CartItemsResponse> mapToCartItemResponses(List<CartItem> items) {
        return items.stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());
    }

    private CartItemsResponse mapToCartItemResponse(CartItem item) {
        CartItemsResponse itemResponse = new CartItemsResponse();
        itemResponse.setId(item.getId());
        itemResponse.setCourseId(item.getCourseId());
        itemResponse.setCourseName(item.getCourseName());
        itemResponse.setPrice(item.getPrice());
        itemResponse.setCreateAt(item.getCreateAt());
        itemResponse.setUpdateAt(item.getUpdateAt());
        return itemResponse;
    }
}
