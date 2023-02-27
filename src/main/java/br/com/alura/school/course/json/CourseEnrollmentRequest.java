package br.com.alura.school.course.json;

import br.com.alura.school.support.validation.Unique;
import br.com.alura.school.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseEnrollmentRequest {

    @Unique(entity = User.class, field = "username")
    @JsonProperty
    @NotBlank
    private String username;
}
