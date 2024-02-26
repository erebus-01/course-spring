package io.academia.course.service.impl;

import io.academia.course.model.SlideImage;
import io.academia.course.repository.SlideImageRepository;
import io.academia.course.service.SlideImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class SlideImageServiceImpl implements SlideImageService {

    private final SlideImageRepository repository;

    @Override
    public SlideImage getSlideImage(String id) {
        return repository.findByImageName(id);
    }
}
