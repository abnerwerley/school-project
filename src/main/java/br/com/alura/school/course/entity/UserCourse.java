package br.com.alura.school.course.entity;

import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.entity.UserCourseId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity(name = "user_course")
@Table(name = "user_course")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCourse {

    @EmbeddedId
    private UserCourseId id;

    @Column(name = "endolledAt")
    @NotNull
    private LocalDate enrolledAt;

    public void setUserCourseId(User user, Course course) {
        this.id = new UserCourseId(user, course);
    }
}
