package br.com.alura.school.user.controller;

import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.json.NewUserRequest;
import br.com.alura.school.user.json.UserResponse;
import br.com.alura.school.user.persistence.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users/{username}")
    ResponseEntity<UserResponse> userByUsername(@PathVariable("username") String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User %s not found", username)));
        return ResponseEntity.ok(new UserResponse(user));
    }

    @PostMapping("/users")
    ResponseEntity<Void> newUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        userRepository.save(newUserRequest.toEntity());
        URI location = URI.create(format("/users/%s", newUserRequest.getUsername()));
        return ResponseEntity.created(location).build();
    }

}
