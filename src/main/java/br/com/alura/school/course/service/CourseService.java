package br.com.alura.school.course.service;

import br.com.alura.school.course.entity.Course;
import br.com.alura.school.course.entity.UserCourse;
import br.com.alura.school.course.json.CourseReportResponse;
import br.com.alura.school.course.json.CourseResponse;
import br.com.alura.school.course.json.NewCourseRequest;
import br.com.alura.school.course.persistence.CourseRepository;
import br.com.alura.school.course.persistence.UserCourseRepository;
import br.com.alura.school.exception.RequestException;
import br.com.alura.school.exception.ResourceNotFoundException;
import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Slf4j
public class CourseService {

    private final CourseRepository repository;
    private final UserRepository userRepository;
    private final UserCourseRepository userCourseRepository;

    public CourseService(CourseRepository repository, UserRepository userRepository, UserCourseRepository userCourseRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.userCourseRepository = userCourseRepository;
    }

    public List<CourseResponse> allCourses() {
        try {
            return repository.findAll().stream().map(CourseResponse::new).collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Could not get All courses.");
            throw new RequestException("Could not get all courses.");
        }
    }

    public CourseResponse courseByCode(String code) {
        try {
            Course course = repository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException(format("Course with code %s not found", code)));
            return new CourseResponse(course);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Could not get course with code: " + code);
            throw new RequestException("Could not get course with code: " + code);
        }
    }

    public URI newCourse(NewCourseRequest newCourseRequest) {
        try {
            repository.save(newCourseRequest.toEntity());
            return URI.create(format("/courses/%s", newCourseRequest.getCode()));

        } catch (Exception e) {
            log.error("Could not create new course. " + e.getMessage());
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
            List<UserCourse> coursesEnrolled = userCourseRepository.fingCoursesByUserId(user.getId());

            boolean courseAlreadyEnrolled = coursesEnrolled.stream()
                    .anyMatch(userCourse -> userCourse.getId().getCourse().equals(course));

            if (courseAlreadyEnrolled) {
                throw new RequestException("Already enrolled to course: " + course.getName());
            } else {
                UserCourse userCourse = new UserCourse();
                course.setUsersEnrolled(user);
                user.setCourses(course);
                userCourse.setUserCourseId(user, course);
                userCourse.setEnrolledAt(LocalDate.now());
                repository.save(course);
                userCourseRepository.save(userCourse);
                userRepository.save(user);
            }

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (RequestException e) {
            log.error(e.getMessage());
            throw new RequestException(e.getMessage());
        } catch (Exception e) {
            log.error("Could not enroll to course. " + e.getMessage());
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
            log.error("Could not get courses report. " + e.getMessage());
            throw new RequestException("Could not get courses report.");
        }
    }
}
