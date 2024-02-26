package io.academia.course.dto.response;

import io.academia.course.model.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlideImageDto {

    private UUID id;
    private String imageName;
    private String imageUri;

}
