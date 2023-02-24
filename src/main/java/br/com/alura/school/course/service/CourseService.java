package br.com.alura.school.course.service;

import br.com.alura.school.course.entity.Course;
import br.com.alura.school.course.json.CourseResponse;
import br.com.alura.school.course.json.NewCourseRequest;
import br.com.alura.school.course.persistence.CourseRepository;
import br.com.alura.school.exception.RequestException;
import br.com.alura.school.exception.ResourceNotFoundException;
import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.persistence.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CourseService {

    private final CourseRepository repository;

    private final UserRepository userRepository;

    public CourseService(CourseRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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

    public void courseEnrollment(String username, String code) {
        try {
            Optional<User> userOptional = userRepository.findByUsername(username);
            Optional<Course> courseOptional = repository.findByCode(code);
            if (userOptional.isEmpty()) {
                throw new ResourceNotFoundException("User not found with username: " + username);
            }
            if (courseOptional.isEmpty()) {
                throw new ResourceNotFoundException("Course not found with code: " + code);
            }
            Course course = courseOptional.get();
            User user = userOptional.get();
            if (!user.getCourses().contains(course)) {
                course.setUsersEnrolled(user);
                user.setCourses(course);
                repository.save(course);
                userRepository.save(user);
            } else {
                throw new RequestException("Already enrolled to course: " + course.getName());
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (RequestException e) {
            throw new RequestException(e.getMessage());
        } catch (Exception e) {
            throw new RequestException("Could not enroll to course.");
        }
    }
}
