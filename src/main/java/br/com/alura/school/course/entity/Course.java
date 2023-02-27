package br.com.alura.school.course.entity;

import br.com.alura.school.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Course {

    @Id

    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(max = 10)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String code;

    @Size(max = 20)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "courses")
    private List<User> usersEnrolled = new ArrayList<>();

    public Course(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Course(String code, String name, String description, List<User> usersEnrolled) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.usersEnrolled = usersEnrolled;
    }

    public Course(Long id, String code, String name, String description) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public void setUsersEnrolled(User usersEnrolled) {
        this.usersEnrolled.add(usersEnrolled);
    }
}
