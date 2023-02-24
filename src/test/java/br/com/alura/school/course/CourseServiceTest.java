package br.com.alura.school.course;

import br.com.alura.school.course.entity.Course;
import br.com.alura.school.course.persistence.CourseRepository;
import br.com.alura.school.course.service.CourseService;
import br.com.alura.school.exception.RequestException;
import br.com.alura.school.exception.ResourceNotFoundException;
import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.persistence.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService service;

    @Mock
    private CourseRepository repository;

    @Mock
    private UserRepository userRepository;

    public static final String ALEX = "alex";
    public static final String EMAIL_ALEX = "alex@gmail.com";
    public static final String COURSE_CODE = "mysql-1";
    public static final String COURSE_NAME = "Introdução a mysql";
    public static final String COURSE_DESCRIPTION = "Curso básico de banco de dados relacional mysql";
    public static final Course COURSE = new Course(COURSE_CODE, COURSE_NAME, COURSE_DESCRIPTION);
    public static final User USER = new User(ALEX, EMAIL_ALEX);
    public static final List<Course> COURSES = List.of(COURSE);
    public static final User USER_WITH_COURSES = new User(ALEX, EMAIL_ALEX, COURSES);

    @Test
    void course_enrollment_test() {
        Optional<User> optionalUser = Optional.of(USER);
        Optional<Course> optionalCourse = Optional.of(COURSE);

        doReturn(optionalUser).when(userRepository).findByUsername(ALEX);
        doReturn(optionalCourse).when(repository).findByCode(COURSE_CODE);

        service.courseEnrollment(ALEX, COURSE_CODE);

        verify(userRepository).findByUsername(ALEX);
        verify(repository).findByCode(COURSE_CODE);
    }

    @Test
    void course_enrollment_user_not_found() {

        Optional<User> optionalUser = Optional.of(USER);

        doReturn(optionalUser).when(userRepository).findByUsername(ALEX);
        when(userRepository.findByUsername(ALEX)).thenReturn(Optional.empty());

        Exception userNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.courseEnrollment(ALEX, COURSE_CODE));
        Assertions.assertNotNull(userNotFoundException);
        Assertions.assertEquals("User not found with username: " + USER.getUsername(), userNotFoundException.getMessage());
        verify(userRepository).findByUsername(ALEX);
        verify(repository).findByCode(COURSE_CODE);
    }

    @Test
    void course_enrollment_course_not_found() {
        Optional<User> optionalUser = Optional.of(USER);

        doReturn(optionalUser).when(userRepository).findByUsername(ALEX);
        when(repository.findByCode(COURSE.getCode())).thenReturn(Optional.empty());

        Exception courseNotFoundException = Assertions.assertThrows(ResourceNotFoundException.class, () -> service.courseEnrollment(ALEX, COURSE_CODE));
        Assertions.assertNotNull(courseNotFoundException);
        Assertions.assertEquals("Course not found with code: " + COURSE.getCode(), courseNotFoundException.getMessage());
        verify(userRepository).findByUsername(ALEX);
        verify(repository).findByCode(COURSE_CODE);
    }

    @Test
    void course_enrollment_user_already_enrolled() {
        Optional<User> optionalUser = Optional.of(USER_WITH_COURSES);
        Optional<Course> optionalCourse = Optional.of(COURSE);

        doReturn(optionalUser).when(userRepository).findByUsername(ALEX);
        doReturn(optionalCourse).when(repository).findByCode(COURSE_CODE);

        Exception exception = Assertions.assertThrows(RequestException.class, () -> service.courseEnrollment(ALEX, COURSE_CODE));
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Already enrolled to course: " + COURSE.getName(), exception.getMessage());
        verify(userRepository).findByUsername(ALEX);
        verify(repository).findByCode(COURSE_CODE);
    }
}
