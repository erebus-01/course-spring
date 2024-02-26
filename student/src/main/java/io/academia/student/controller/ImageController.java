package io.academia.student.controller;

import io.academia.student.model.Student;
import io.academia.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class ImageController {

    private final StudentService service;

    @GetMapping("/files/{imageName}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("imageName") String imageName) {
        Student imageData = service.findByNamePhoto(imageName);

        if (imageData != null && imageData.getPhoto() != null) {
            MediaType contentType = MediaType.parseMediaType(imageData.getTypePhoto());

            return ResponseEntity.ok()
                    .contentType(contentType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "/attachment; filename=\"" + imageData.getNamePhoto() + "\"")
                    .body(new ByteArrayResource(imageData.getPhoto()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
