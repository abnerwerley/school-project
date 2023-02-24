package br.com.alura.school.course.controller;

import br.com.alura.school.course.entity.Course;
import br.com.alura.school.course.json.CourseResponse;
import br.com.alura.school.course.json.NewCourseRequest;
import br.com.alura.school.course.persistence.CourseRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> allCourses() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/courses/{code}")
    ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        return ResponseEntity.ok(new CourseResponse(course));
    }

    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        courseRepository.save(newCourseRequest.toEntity());
        URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
        return ResponseEntity.created(location).build();
    }
}
