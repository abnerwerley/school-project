package br.com.alura.school.user.entity;

import br.com.alura.school.course.entity.Course;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public
class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Size(max = 20)
    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany
    @JoinTable(name = "user_course",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses = new ArrayList<>();

    @Deprecated
    protected User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public User(String username, String email, List<Course> courses) {
        this.username = username;
        this.email = email;
        this.courses = courses;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(Course course) {
        this.courses.add(course);
    }
}
