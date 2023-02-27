package br.com.alura.school.user.service;

import br.com.alura.school.exception.RequestException;
import br.com.alura.school.exception.ResourceNotFoundException;
import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.json.NewUserRequest;
import br.com.alura.school.user.json.UserResponse;
import br.com.alura.school.user.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Slf4j
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponse userByUsername(String username) {
        try {
            User user = repository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException(format("User %s not found", username)));
            return new UserResponse(user);

        } catch (ResourceNotFoundException e) {
            log.error(e.getMessage());
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            log.error("Could not get user by username: " + username + e.getMessage());
            throw new RequestException("Could not get user by username: " + username);
        }
    }

    public URI newUser(NewUserRequest newUserRequest) {
        try {
            repository.save(newUserRequest.toEntity());
            return URI.create(format("/users/%s", newUserRequest.getUsername()));
        } catch (IllegalArgumentException e) {
            log.error("Username must not have any space.");
            throw new RequestException("Username must not have any space.");
        } catch (Exception e) {
            log.error("Could not create user. " + e.getMessage());
            throw new RequestException("Could not create user.");
        }
    }

    public List<UserResponse> allUsers() {
        try {
            return repository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
        }catch (Exception e) {
            log.error("Could not find all users. " + e.getMessage());
            throw new RequestException("Could not find all users.");
        }
    }
}
