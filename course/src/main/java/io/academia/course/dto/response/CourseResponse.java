package io.academia.course.dto.response;

import io.academia.course.model.DifficultyLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private UUID id;
    private String name;
    private String description;
    private String nameImage;
    private String urlImage;
    private String typeImage;
    private long sizeImage;
    private List<SlideImageDto> slideImages;
    private Date startDate;
    private Date endDate;
    private DifficultyLevel difficultyLevel;
    private Double price;
    private UUID instructor;
    private List<CategoryResponse> categories;
    private List<SectionResponse> sections;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

}
