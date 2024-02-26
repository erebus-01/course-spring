package io.academia.course.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.academia.course.dto.request.CourseRequest;
import io.academia.course.dto.request.CourseRequestUpdate;
import io.academia.course.dto.response.CourseResponse;
import io.academia.course.dto.response.SlideImageDto;
import io.academia.course.model.Course;
import io.academia.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/course")
@Slf4j
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseServices;

    @PostMapping
    public ResponseEntity<CourseResponse> createCourse(@ModelAttribute CourseRequest courseRequest,
                                                       @RequestParam("mainImage") MultipartFile mainImage,
                                                       @RequestParam("slideImage") MultipartFile[] slideImage) throws IOException {
        CourseResponse createdCourse = courseServices.createCourse(courseRequest, mainImage, slideImage);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    @PostMapping("/slide-image/{courseId}")
    public ResponseEntity<List<SlideImageDto>> createSlideImage(@PathVariable("courseId") UUID courseId,
                                                       @RequestParam("slideImage") MultipartFile[] slideImage) throws IOException {
        List<SlideImageDto> createdSlideImage = courseServices.updateImageSlide(courseId, slideImage);
        return new ResponseEntity<>(createdSlideImage, HttpStatus.CREATED);
    }

    @PostMapping("/import-from-excel")
    public ResponseEntity<List<CourseResponse>> importDataFromExcel(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            List<CourseResponse> courses = null;
            if (file.getOriginalFilename().endsWith(".csv")) {
                courses = courseServices.importCoursesFromCsv(file);
            } else if (file.getOriginalFilename().endsWith(".xlsx")) {
                courses = courseServices.importCoursesFromExcel(file);
            } else {
                return ResponseEntity.badRequest().body(null);
            }
            return new ResponseEntity<>(courses, HttpStatus.CREATED);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }


    @GetMapping("/{courseId}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable UUID courseId) {
        CourseResponse course = courseServices.getCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/list-course-id")
    public ResponseEntity<List<CourseResponse>> getCoursesByIds(@RequestParam List<UUID> courseIds) {
        List<CourseResponse> courseResponses = courseServices.findCoursesByIds(courseIds);
        return ResponseEntity.ok(courseResponses);
    }

    @GetMapping
    public ResponseEntity<Page<CourseResponse>> getAllCourses(Pageable pageable) {
        Page<CourseResponse> courseResponses = courseServices.findAll(pageable);
        return ResponseEntity.ok(courseResponses);
    }

    @GetMapping("/all-course")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> courses = courseServices.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/main-image/{fileId}")
    public ResponseEntity downloadFile(@PathVariable UUID fileId) {
        Course dbFile = courseServices.getOneById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getTypeImage()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "/attachment; filename=\"" + dbFile.getName() + "\"")
                .body(new ByteArrayResource(dbFile.getMainImage()));
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("imageName") String imageName) {
        Course imageData = courseServices.findByNameImage(imageName);

        if (imageData != null && imageData.getMainImage() != null) {
            MediaType contentType = MediaType.parseMediaType(imageData.getTypeImage());

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "/attachment; filename=\"" + imageData.getName() + "\"")
                    .body(new ByteArrayResource(imageData.getMainImage()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<Page<CourseResponse>> searchCourses(
            @RequestParam(required = false, name = "keyword") String keyword,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        Page<CourseResponse> courses = courseServices.searchCourses(keyword, categories, page, size);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable("courseId") UUID courseId,
            @RequestBody CourseRequestUpdate updatedCourseRequest) {
        CourseResponse updatedCourse = courseServices.updateCourse(courseId, updatedCourseRequest);
        return ResponseEntity.ok(updatedCourse);
    }

    @PutMapping("/update-image/{courseId}")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable UUID courseId,
            @RequestParam("mainImage") MultipartFile mainImage) throws IOException {
        CourseResponse updatedCourse = courseServices.updateImageCourse(courseId, mainImage);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable UUID courseId) {
        courseServices.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
