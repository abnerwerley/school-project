package br.com.alura.school.user.json;

import br.com.alura.school.support.validation.Unique;
import br.com.alura.school.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

    @Unique(entity = User.class, field = "username")
    @Size(max = 20)
    @NotBlank
    @JsonProperty
    private String username;

    @Unique(entity = User.class, field = "email")
    @NotBlank
    @Email
    @JsonProperty
    private String email;

    public User toEntity() {
        return new User(username, email);
    }
}
