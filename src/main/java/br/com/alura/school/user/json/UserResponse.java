package br.com.alura.school.user.json;

import br.com.alura.school.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponse {

    @JsonProperty
    private final String username;

    @JsonProperty
    private final String email;

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
