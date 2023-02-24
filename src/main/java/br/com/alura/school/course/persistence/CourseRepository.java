package br.com.alura.school.course.persistence;

import br.com.alura.school.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);
}
