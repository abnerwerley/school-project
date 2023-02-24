package br.com.alura.school.user.service;

import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.json.NewUserRequest;
import br.com.alura.school.user.json.UserResponse;
import br.com.alura.school.user.persistence.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponse userByUsername(String username) {
        User user = repository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User %s not found", username)));
        return new UserResponse(user);
    }

    public URI newUser(NewUserRequest newUserRequest) {
        repository.save(newUserRequest.toEntity());
        return URI.create(format("/users/%s", newUserRequest.getUsername()));
    }

}
