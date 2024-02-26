package io.academia.course.dto.request;

import io.academia.course.model.Category;
import io.academia.course.model.DifficultyLevel;
import io.academia.course.model.Section;
import io.academia.course.model.SlideImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {

    private String name;
    private String description;
    private MultipartFile mainImage;
    private String nameImage;
    private String urlImage;
    private String typeImage;
    private long sizeImage;
    private List<MultipartFile> slideImages;
    private List<Section> sections;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private DifficultyLevel difficultyLevel;
    private Double price;
    private UUID instructor;
    private List<Category> categories;

}
