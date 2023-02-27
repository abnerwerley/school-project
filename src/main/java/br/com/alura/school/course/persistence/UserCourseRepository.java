package br.com.alura.school.course.persistence;

import br.com.alura.school.course.entity.UserCourse;
import br.com.alura.school.user.entity.UserCourseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, UserCourseId> {

    @Query("select c from user_course as c where user_id=:userId")
    List<UserCourse> fingCoursesByUserId(@Param("userId") Long userId);
}
