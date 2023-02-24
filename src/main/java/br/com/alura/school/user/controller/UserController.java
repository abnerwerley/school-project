package br.com.alura.school.user.controller;

import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.json.NewUserRequest;
import br.com.alura.school.user.json.UserResponse;
import br.com.alura.school.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{username}")
    ResponseEntity<UserResponse> userByUsername(@PathVariable("username") String username) {
        UserResponse userResponse = service.userByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("")
    ResponseEntity<Void> newUser(@RequestBody @Valid NewUserRequest newUserRequest) {
        URI location = service.newUser(newUserRequest);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("")
    ResponseEntity<List<UserResponse>> allUsers(){
        return ResponseEntity.ok(service.allUsers());
    }

}
