package io.academia.course.service;

import io.academia.course.dto.request.LessonRequest;
import io.academia.course.dto.response.LessonResponse;

import java.util.List;
import java.util.UUID;

public interface LessonServices {
    LessonResponse createLesson(LessonRequest lessonRequest);
    LessonResponse getLessonById(Long lessonId);
    List<LessonResponse> getAllLessons(UUID sessionId);
    LessonResponse updateLesson(Long lessonId, LessonRequest updatedLessonRequest);
    void deleteLesson(Long lessonId);
}
