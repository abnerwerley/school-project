package br.com.alura.school.course.service;

import br.com.alura.school.course.entity.Course;
import br.com.alura.school.course.json.CourseReportResponse;
import br.com.alura.school.course.json.CourseResponse;
import br.com.alura.school.course.json.NewCourseRequest;
import br.com.alura.school.course.persistence.CourseRepository;
import br.com.alura.school.exception.RequestException;
import br.com.alura.school.exception.ResourceNotFoundException;
import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class CourseService {

    private final CourseRepository repository;

    private final UserRepository userRepository;

    public CourseService(CourseRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<CourseResponse> allCourses() {
        try {
            return repository.findAll().stream().map(CourseResponse::new).collect(Collectors.toList());

        } catch (Exception e) {
            throw new RequestException("Could not get All courses.");
        }
    }

    public CourseResponse courseByCode(String code) {
        try {
            Course course = repository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException(format("Course with code %s not found", code)));
            return new CourseResponse(course);

        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RequestException("Could not get course with code: " + code);
        }
    }

    public URI newCourse(NewCourseRequest newCourseRequest) {
        try {
            repository.save(newCourseRequest.toEntity());
            return URI.create(format("/courses/%s", newCourseRequest.getCode()));

        } catch (Exception e) {
            throw new RequestException("Could not create new course.");
        }
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

    public List<CourseReportResponse> report() {
        try {
            List<User> users = userRepository.findAll();
            List<CourseReportResponse> reportsPerUser = new ArrayList<>();

            for (User user : users) {
                Long coursesEnrolled = repository.findCourseQuantityByUser(user.getId());
                if (coursesEnrolled != 0L) {
                    CourseReportResponse report = new CourseReportResponse(user.getEmail(), coursesEnrolled);
                    reportsPerUser.add(report);
                }
            }
            return reportsPerUser;

        } catch (Exception e) {
            throw new RequestException("Could not get courses report.");
        }
    }
}
