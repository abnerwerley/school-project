package br.com.alura.school.course.json;

import br.com.alura.school.course.entity.Course;
import br.com.alura.school.support.validation.Unique;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewCourseRequest {

    @Unique(entity = Course.class, field = "code")
    @Size(max = 10)
    @NotBlank
    @JsonProperty
    private String code;

    @Unique(entity = Course.class, field = "name")
    @Size(max = 20)
    @NotBlank
    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    public Course toEntity() {
        return new Course(code, name, description);
    }
}
