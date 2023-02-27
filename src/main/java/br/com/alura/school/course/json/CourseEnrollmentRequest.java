package br.com.alura.school.course.json;

import br.com.alura.school.support.validation.Unique;
import br.com.alura.school.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollmentRequest {

    @Unique(entity = User.class, field = "username")
    private String username;
}
