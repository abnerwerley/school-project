package br.com.alura.school.course.controller;

import br.com.alura.school.course.json.CourseEnrollmentRequest;
import br.com.alura.school.course.json.CourseResponse;
import br.com.alura.school.course.json.NewCourseRequest;
import br.com.alura.school.course.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping("")
    ResponseEntity<List<CourseResponse>> allCourses() {
        List<CourseResponse> courses = service.allCourses();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{code}")
    ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
        CourseResponse course = service.courseByCode(code);
        return ResponseEntity.ok(course);
    }

    @PostMapping("")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        URI location = service.newCourse(newCourseRequest);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{code}/enroll")
    ResponseEntity<Void> enrollToCourse(@RequestBody CourseEnrollmentRequest username, @PathVariable("code") String code) {
        service.courseEnrollment(username.getUsername(), code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
