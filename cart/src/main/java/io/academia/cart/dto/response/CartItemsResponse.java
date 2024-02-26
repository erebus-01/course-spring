package io.academia.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemsResponse {
    private Long id;
    private UUID courseId;
    private String courseName;
    private Double price;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
