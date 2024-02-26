package io.academia.course.dto.request;

import io.academia.course.model.Course;
import io.academia.course.model.Lesson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionRequest {
    private String title;
    private List<Lesson> lessons;
    private Course course;
}
