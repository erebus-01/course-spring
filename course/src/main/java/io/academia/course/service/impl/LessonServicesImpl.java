package io.academia.course.service.impl;

import io.academia.course.dto.request.LessonRequest;
import io.academia.course.dto.response.LessonResponse;
import io.academia.course.model.Lesson;
import io.academia.course.repository.LessonRepository;
import io.academia.course.service.LessonServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServicesImpl implements LessonServices {

    private final LessonRepository lessonRepository;

    @Override
    public LessonResponse createLesson(LessonRequest lessonRequest) {
        Lesson lesson = mapRequestToLesson(lessonRequest);
        Lesson savedLesson = lessonRepository.save(lesson);
        return mapLessonToResponse(savedLesson);
    }

    @Override
    public LessonResponse getLessonById(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));
        return mapLessonToResponse(lesson);
    }

    @Override
    public List<LessonResponse> getAllLessons(UUID sessionId) {
        List<Lesson> lessons = lessonRepository.findBySessionId(sessionId);
        return lessons.stream()
                .map(this::mapLessonToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LessonResponse updateLesson(Long lessonId, LessonRequest updatedLessonRequest) {
        Lesson existingLesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));

        Lesson updatedLesson = mapRequestToExistingLesson(updatedLessonRequest, existingLesson);
        Lesson savedLesson = lessonRepository.save(updatedLesson);
        return mapLessonToResponse(savedLesson);
    }

    @Override
    public void deleteLesson(Long lessonId) {
        lessonRepository.deleteById(lessonId);
    }

    private Lesson mapRequestToLesson(LessonRequest lessonRequest) {
        return Lesson.builder()
                .title(lessonRequest.getTitle())
                .content(lessonRequest.getContent())
                .url(lessonRequest.getUrl())
                .session(lessonRequest.getSession())
                .build();
    }

    private LessonResponse mapLessonToResponse(Lesson lesson) {
        return LessonResponse.builder()
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .url(lesson.getUrl())
                .build();
    }

    private Lesson mapRequestToExistingLesson(LessonRequest updatedLessonRequest, Lesson existingLesson) {
        existingLesson.setTitle(updatedLessonRequest.getTitle());
        existingLesson.setContent(updatedLessonRequest.getContent());
        existingLesson.setUrl(updatedLessonRequest.getUrl());
        // Update other fields as needed
        return existingLesson;
    }
}
