package io.academia.course.repository;

import io.academia.course.model.SlideImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SlideImageRepository extends JpaRepository<SlideImage, UUID> {
    SlideImage findByImageName(String nameImage);
}
