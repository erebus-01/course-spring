package io.academia.course.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.academia.course.dto.request.CourseRequest;
import io.academia.course.dto.request.CourseRequestUpdate;
import io.academia.course.dto.response.CourseResponse;
import io.academia.course.dto.response.SlideImageDto;
import io.academia.course.model.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface CourseService {
    CourseResponse createCourse(CourseRequest courseRequest, MultipartFile mainImage, MultipartFile[] slideImage) throws IOException;
    CourseResponse getCourseById(UUID courseId);
    Course getOneById(UUID courseId);
    Page<CourseResponse> findAll(Pageable pageable);
    List<CourseResponse> importCoursesFromExcel(MultipartFile excel) throws IOException;
    List<CourseResponse> importCoursesFromCsv(MultipartFile csv) throws IOException;
    Page<CourseResponse> searchCourses(String keyword, List<Long> categoryIds, int page, int size) throws JsonProcessingException;
    Course findByNameImage(String nameImage);
    List<CourseResponse> getAllCourses();
    CourseResponse updateCourse(UUID courseId, CourseRequestUpdate updatedCourseRequest);
    CourseResponse updateImageCourse(UUID courseId, MultipartFile mainImage) throws IOException;
    List<SlideImageDto> updateImageSlide(UUID courseId, MultipartFile[] slideImage) throws IOException;
    List<CourseResponse> findCoursesByIds(List<UUID> courseIds);
    void deleteCourse(UUID courseId);
}
