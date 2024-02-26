package io.academia.course.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.academia.course.dto.response.CourseResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface CourseRedisService {
    void clear();
    Page<CourseResponse> getAllCourse(String keyword, List<Long> categories, PageRequest pageRequest) throws JsonProcessingException;
    void saveAllCourse(Page<CourseResponse> courseResponses, String keyword, List<Long> categories, PageRequest pageRequest) throws JsonProcessingException;
}
