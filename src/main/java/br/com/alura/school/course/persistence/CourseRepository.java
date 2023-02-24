package br.com.alura.school.course.persistence;

import br.com.alura.school.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);

    @Query("select count(course_id) from user_course where user_id=:userId ")
    Long findCourseQuantityByUser(@Param("userId") Long userId);
}
