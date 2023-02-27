package br.com.alura.school.course.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseReportResponse {

    @JsonProperty
    private String email;

    @JsonProperty
    private Long enrolls;
}
