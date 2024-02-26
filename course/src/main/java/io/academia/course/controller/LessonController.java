package io.academia.course.controller;

import io.academia.course.dto.request.LessonRequest;
import io.academia.course.dto.response.LessonResponse;
import io.academia.course.service.LessonServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    private final LessonServices lessonServices;

    @PostMapping("/{sessionId}")
    public ResponseEntity<LessonResponse> createLesson(
            @PathVariable UUID sessionId,
            @RequestBody LessonRequest lessonRequest) {
        LessonResponse createdLesson = lessonServices.createLesson(lessonRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> getLessonById(@PathVariable Long lessonId) {
        LessonResponse lesson = lessonServices.getLessonById(lessonId);
        return ResponseEntity.ok(lesson);
    }

    @GetMapping("/all/{sessionId}")
    public ResponseEntity<List<LessonResponse>> getAllLessons(@PathVariable UUID sessionId) {
        List<LessonResponse> lessons = lessonServices.getAllLessons(sessionId);
        return ResponseEntity.ok(lessons);
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonResponse> updateLesson(
            @PathVariable Long lessonId,
            @RequestBody LessonRequest updatedLessonRequest) {
        LessonResponse updatedLesson = lessonServices.updateLesson(lessonId, updatedLessonRequest);
        return ResponseEntity.ok(updatedLesson);
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long lessonId) {
        lessonServices.deleteLesson(lessonId);
        return ResponseEntity.noContent().build();
    }

}
