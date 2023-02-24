package br.com.alura.school.course.entity;

import br.com.alura.school.user.entity.UserCourseId;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_course")
@Getter
@Setter
public class UserCourse {

    @EmbeddedId
    private UserCourseId id;
}
