package br.com.alura.school.course.service;

import br.com.alura.school.course.entity.Course;
import br.com.alura.school.course.json.CourseResponse;
import br.com.alura.school.course.json.NewCourseRequest;
import br.com.alura.school.course.persistence.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CourseService {

    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        this.repository = repository;
    }

    public List<CourseResponse> allCourses() {
        List<Course> courses = repository.findAll();
        return courses.stream().map(CourseResponse::new).collect(Collectors.toList());
    }

    public CourseResponse courseByCode(String code) {
        Course course = repository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        return new CourseResponse(course);
    }

    public URI newCourse(NewCourseRequest newCourseRequest) {
        repository.save(newCourseRequest.toEntity());
        return URI.create(format("/courses/%s", newCourseRequest.getCode()));
    }
}
