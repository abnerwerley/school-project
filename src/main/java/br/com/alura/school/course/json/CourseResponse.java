package br.com.alura.school.course.json;

import br.com.alura.school.course.entity.Course;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Optional;

public class CourseResponse {

    @JsonProperty
    private final String code;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final String shortDescription;

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
