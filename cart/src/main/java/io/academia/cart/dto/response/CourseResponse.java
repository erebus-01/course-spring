package io.academia.cart.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private UUID id;
    private String name;
    private String description;
    private String urlImage;
    private Date startDate;
    private Date endDate;
    private Double price;
    private UUID instructor;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
