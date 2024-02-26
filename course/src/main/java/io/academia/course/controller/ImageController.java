package io.academia.course.controller;

import io.academia.course.model.Course;
import io.academia.course.model.SlideImage;
import io.academia.course.service.CourseService;
import io.academia.course.service.SlideImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class ImageController {

    private final CourseService courseServices;
    private final SlideImageService slideImageService;

    @GetMapping("/main-image/{fileId}")
    public ResponseEntity downloadFile(@PathVariable UUID fileId) {
        Course dbFile = courseServices.getOneById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getTypeImage()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "/attachment; filename=\"" + dbFile.getName() + "\"")
                .body(new ByteArrayResource(dbFile.getMainImage()));
    }

    @GetMapping("/files/{imageName}")
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

    @GetMapping("/file-image/{imageName}")
    public ResponseEntity<ByteArrayResource> getSlideImage(@PathVariable("imageName") String imageName) {
        SlideImage imageData = slideImageService.getSlideImage(imageName);

        if (imageData != null && imageData.getImageData() != null) {
            MediaType contentType = MediaType.parseMediaType(imageData.getImageType());

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "/attachment; filename=\"" + imageData.getImageName() + "\"")
                    .body(new ByteArrayResource(imageData.getImageData()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
