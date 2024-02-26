package io.academia.course.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.academia.course.config.RestResponsePage;
import io.academia.course.dto.response.CourseResponse;
import io.academia.course.service.CourseRedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseRedisServiceImpl implements CourseRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;

    private String getKeyFrom(String keyword, List<Long> categories, PageRequest pageRequest) {

        int pageNumber = pageRequest.getPageNumber();
        int pageSize = pageRequest.getPageSize();
        Sort sort = pageRequest.getSort();
        String sortDirection = Objects.requireNonNull(sort.getOrderFor("id"))
                .getDirection() == Sort.Direction.ASC ? "asc" : "desc";

        return String.format("all_course:%s:%d:%d:%s", keyword, pageNumber, pageSize, sortDirection);
    }


    @Override
    public void clear() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Override
    public Page<CourseResponse> getAllCourse(String keyword, List<Long> categories, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categories, pageRequest);
        String json = (String) redisTemplate.opsForValue().get(key);
        return json != null ? mapper.readValue(json, new TypeReference<RestResponsePage<CourseResponse>>() {}) : null;
    }

    @Override
    public void saveAllCourse(Page<CourseResponse> courseResponses, String keyword, List<Long> categories, PageRequest pageRequest) throws JsonProcessingException {
        String key = this.getKeyFrom(keyword, categories, pageRequest);
        String json = mapper.writeValueAsString(courseResponses);
        redisTemplate.opsForValue().set(key, json);
    }
}
