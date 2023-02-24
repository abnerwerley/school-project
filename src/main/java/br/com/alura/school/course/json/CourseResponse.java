package br.com.alura.school.course.json;

import br.com.alura.school.course.entity.Course;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    @JsonProperty
    private String code;

    @JsonProperty
    private String name;

    @JsonProperty
    private String shortDescription;

    public CourseResponse(Course course) {
        this.code = course.getCode();
        this.name = course.getName();
        this.shortDescription = Optional.of(course.getDescription()).map(this::abbreviateDescription).orElse("");
    }

    private String abbreviateDescription(String description) {
        if (description.length() <= 13) return description;
        return description.substring(0, 10) + "...";
    }

}
