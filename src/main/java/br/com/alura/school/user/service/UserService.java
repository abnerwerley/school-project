package br.com.alura.school.user.service;

import br.com.alura.school.exception.RequestException;
import br.com.alura.school.exception.ResourceNotFoundException;
import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.json.NewUserRequest;
import br.com.alura.school.user.json.UserResponse;
import br.com.alura.school.user.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
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
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new RequestException("Could not create new course.");
        }

    }

    public URI newUser(NewUserRequest newUserRequest) {
        try {
            repository.save(newUserRequest.toEntity());
            return URI.create(format("/users/%s", newUserRequest.getUsername()));
        } catch (IllegalArgumentException e) {
            throw new RequestException("Username must not have any space.");
        }catch (Exception e) {
            throw new RequestException("Could not create user.");
        }
    }

    public List<UserResponse> allUsers() {
        return repository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
    }
}
