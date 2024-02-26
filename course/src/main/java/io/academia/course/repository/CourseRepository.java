package io.academia.course.repository;

import io.academia.course.model.Course;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    Course findByNameImage(String nameImage);
    Page<Course> findAll(Pageable pageable);
    Page<Course> findByCategoriesIdInAndNameContainingIgnoreCase(List<Long> categoryIds, String name, Pageable pageable);
    Page<Course> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Course> findAllByIdIn(List<UUID> courseIds);


}
