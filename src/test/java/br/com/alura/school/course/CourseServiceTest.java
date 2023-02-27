package br.com.alura.school.course;

import br.com.alura.school.course.entity.Course;
import br.com.alura.school.course.entity.UserCourse;
import br.com.alura.school.course.json.CourseReportResponse;
import br.com.alura.school.course.json.CourseResponse;
import br.com.alura.school.course.json.NewCourseRequest;
import br.com.alura.school.course.persistence.CourseRepository;
import br.com.alura.school.course.persistence.UserCourseRepository;
import br.com.alura.school.course.service.CourseService;
import br.com.alura.school.exception.RequestException;
import br.com.alura.school.exception.ResourceNotFoundException;
import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.entity.UserCourseId;
import br.com.alura.school.user.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService service;

    @Mock
    private CourseRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCourseRepository userCourseRepository;

    public static final String ALEX = "alex";
    public static final String JOANA = "joana";
    public static final String EMAIL_ALEX = "alex@gmail.com";
    public static final String EMAIL_JOANA = "joana@gmail.com";
    public static final String COURSE_CODE = "mysql-1";
    public static final String COURSE_NAME = "Introdução a mysql";
    public static final String COURSE_DESCRIPTION = "Curso básico de banco de dados relacional mysql";
    public static final Course MYSQL_COURSE = new Course(1L, COURSE_CODE, COURSE_NAME, COURSE_DESCRIPTION);
    public static final Course JAVA_COURSE = new Course("java-1", "Java jdk jre", "Curso para iniciar a ser Javeiro");
    public static final Course JAVASCRIPT_COURSE = new Course("javascript-1", "Lógica de programação javascript", "Curso iniciante dos que se acham superior só porque não precisam usar ponto e vírgula no final de tudo.");
    public static final User USER = new User(ALEX, EMAIL_ALEX);
    public static final List<Course> COURSES_ALEX = List.of(MYSQL_COURSE);
    public static final List<Course> COURSES_JOANA = List.of(MYSQL_COURSE, JAVA_COURSE, JAVASCRIPT_COURSE);
    public static final User USER_ALEX = new User(1L, ALEX, EMAIL_ALEX, COURSES_ALEX);
    public static final User USER_JOANA = new User(2L, JOANA, EMAIL_JOANA, COURSES_JOANA);
    public static final User USER_NO_COURSE = new User(3L, "Name", "Any email", List.of());
    public static final List<User> ALL_USERS = List.of(USER_ALEX, USER_JOANA);
    public static final List<UserCourse> USER_COURSES_EMPTY = List.of(new UserCourse(new UserCourseId(new User(), new Course()), LocalDate.now()));
    public static final UserCourseId USER_COURSE_ID = new UserCourseId(USER_ALEX, MYSQL_COURSE);
    public static final List<UserCourse> USER_COURSES_ALEX = List.of(new UserCourse(USER_COURSE_ID, LocalDate.now()));

    @Test
    void test_all_courses() {
        when(repository.findAll()).thenReturn(List.of());
        List<CourseResponse> response = service.allCourses();
        assertNotNull(response);
    }

    @Test
    void test_all_courses_exception() {
        when(repository.findAll()).thenThrow(new RuntimeException());
        Exception exception = assertThrows(RequestException.class, () -> service.allCourses());
        assertEquals("Could not get all courses.", exception.getMessage());
    }

    @Test
    void test_course_by_code() {
        when(repository.findByCode(MYSQL_COURSE.getCode())).thenReturn(Optional.of(MYSQL_COURSE));
        CourseResponse response = service.courseByCode(MYSQL_COURSE.getCode());
        assertNotNull(response);
    }

    @Test
    void test_course_by_code_not_found() {
        when(repository.findByCode(MYSQL_COURSE.getCode())).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.courseByCode(COURSE_CODE));
        assertEquals("Course with code mysql-1 not found", exception.getMessage());
    }

    @Test
    void test_course_by_code_exception() {
        when(repository.findByCode(MYSQL_COURSE.getCode())).thenThrow(new RuntimeException());
        Exception exception = assertThrows(RequestException.class, () -> service.courseByCode(COURSE_CODE));
        assertEquals("Could not get course with code: " + MYSQL_COURSE.getCode(), exception.getMessage());
    }

    @Test
    void test_new_course() {
        NewCourseRequest form = new NewCourseRequest(COURSE_CODE, COURSE_NAME, COURSE_DESCRIPTION);
        doReturn(MYSQL_COURSE).when(repository).save(any(Course.class));
        URI uri = service.newCourse(form);
        assertEquals("/courses/mysql-1", uri.getPath());
        verify(repository, times(1)).save(any(Course.class));
    }

    @Test
    void test_new_course_exception() {
        NewCourseRequest form = new NewCourseRequest(COURSE_CODE, COURSE_NAME, COURSE_DESCRIPTION);
        when(repository.save(any(Course.class))).thenThrow(new RuntimeException());
        Exception exception = assertThrows(RequestException.class, () -> service.newCourse(form));
        assertEquals("Could not create new course.", exception.getMessage());
    }

    @Test
    void test_course_enrollment() {
        Optional<User> optionalUser = Optional.of(USER);
        Optional<Course> optionalCourse = Optional.of(MYSQL_COURSE);

        doReturn(optionalUser).when(userRepository).findByUsername(ALEX);
        doReturn(optionalCourse).when(repository).findByCode(COURSE_CODE);
        when(userCourseRepository.fingCoursesByUserId(optionalUser.get().getId())).thenReturn(USER_COURSES_EMPTY);
        service.courseEnrollment(ALEX, COURSE_CODE);

        verify(userRepository).findByUsername(ALEX);
        verify(repository).findByCode(COURSE_CODE);
        verify(repository, times(1)).save(any(Course.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void test_course_enrollment_user_not_found() {
        when(userRepository.findByUsername(ALEX)).thenReturn(Optional.empty());

        Exception userNotFoundException = assertThrows(ResourceNotFoundException.class, () -> service.courseEnrollment(ALEX, COURSE_CODE));
        assertEquals("User not found with username: " + USER.getUsername(), userNotFoundException.getMessage());
        verify(userRepository).findByUsername(ALEX);
        verify(repository, times(0)).save(any(Course.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void test_course_enrollment_exception() {
        when(userRepository.findByUsername(ALEX)).thenThrow(new RuntimeException());

        Exception exception = assertThrows(RequestException.class, () -> service.courseEnrollment(ALEX, COURSE_CODE));
        assertEquals("Could not enroll to course.", exception.getMessage());
        verify(repository, times(0)).save(any(Course.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void test_course_enrollment_course_not_found() {
        Optional<User> optionalUser = Optional.of(USER);

        doReturn(optionalUser).when(userRepository).findByUsername(ALEX);
        when(repository.findByCode(MYSQL_COURSE.getCode())).thenReturn(Optional.empty());

        Exception courseNotFoundException = assertThrows(ResourceNotFoundException.class, () -> service.courseEnrollment(ALEX, COURSE_CODE));
        assertEquals("Course not found with code: " + MYSQL_COURSE.getCode(), courseNotFoundException.getMessage());
        verify(userRepository).findByUsername(ALEX);
        verify(repository).findByCode(COURSE_CODE);
        verify(repository, times(0)).save(any(Course.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void test_course_enrollment_user_already_enrolled() {
        Optional<User> optionalUser = Optional.of(USER_ALEX);
        Optional<Course> optionalCourse = Optional.of(MYSQL_COURSE);

        doReturn(optionalUser).when(userRepository).findByUsername(ALEX);
        doReturn(optionalCourse).when(repository).findByCode(COURSE_CODE);
        when(userCourseRepository.fingCoursesByUserId(optionalUser.get().getId())).thenReturn(USER_COURSES_ALEX);

        Exception exception = assertThrows(RequestException.class, () -> service.courseEnrollment(ALEX, COURSE_CODE));
        assertEquals("Already enrolled to course: " + MYSQL_COURSE.getName(), exception.getMessage());
        verify(userRepository).findByUsername(ALEX);
        verify(repository).findByCode(COURSE_CODE);
        verify(repository, times(0)).save(any(Course.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void test_report() {
        when(userRepository.findAll()).thenReturn(ALL_USERS);
        when(repository.findCourseQuantityByUser(USER_ALEX.getId())).thenReturn(1L);
        when(repository.findCourseQuantityByUser(USER_JOANA.getId())).thenReturn(3L);

        List<CourseReportResponse> response = service.report();
        assertNotEquals(0, response.size());
    }

    @Test
    void test_report_no_enrolled() {
        when(userRepository.findAll()).thenReturn(List.of(USER_NO_COURSE));
        when(repository.findCourseQuantityByUser(USER_NO_COURSE.getId())).thenReturn(0L);

        List<CourseReportResponse> response = service.report();
        assertEquals(0, response.size());
    }

    @Test
    void test_report_exception() {
        when(userRepository.findAll()).thenThrow(new RuntimeException());

        Exception exception = assertThrows(RequestException.class, () -> service.report());
        assertEquals("Could not get courses report.", exception.getMessage());
    }
}
