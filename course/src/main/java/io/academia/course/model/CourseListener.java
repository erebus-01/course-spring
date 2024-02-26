package io.academia.course.model;

import io.academia.course.service.CourseRedisService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class CourseListener {

    private final CourseRedisService redisService;

    public CourseListener() {
        this.redisService = null;
    }

    @PrePersist
    public void prePersist(Course course) {
        log.info("prePersist");
    }

    @PreUpdate
    public void preUpdate(Course course) {
        log.info("preUpdate");
    }

    @PreRemove
    public void preRemove(Object o) {

    }

    @PostLoad
    public void postLoad(Object o) {

    }

    @PostRemove
    public void postRemove(Object o) {

    }

    @PostUpdate
    public void postUpdate(Object o) {
        log.info("postUpdate");
        redisService.clear();
    }

    @PostPersist
    public void postPersist(Object o) {
        log.info("postPersist");
        redisService.clear();
    }
}
