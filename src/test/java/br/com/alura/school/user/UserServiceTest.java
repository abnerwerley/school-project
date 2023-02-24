package br.com.alura.school.user;

import br.com.alura.school.exception.RequestException;
import br.com.alura.school.exception.ResourceNotFoundException;
import br.com.alura.school.user.entity.User;
import br.com.alura.school.user.json.NewUserRequest;
import br.com.alura.school.user.json.UserResponse;
import br.com.alura.school.user.persistence.UserRepository;
import br.com.alura.school.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    public static final String USERNAME = "anyname";
    public static final String EMAIL = "any@email.com";
    public static final User USER = new User(USERNAME, EMAIL);

    @Test
    void user_by_username() {
        when(repository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        UserResponse user = service.userByUsername(USERNAME);
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    void user_by_name_not_found() {
        when(repository.findByUsername(USERNAME)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.userByUsername(USERNAME));
        assertNotNull(exception);
        assertEquals("User anyname not found", exception.getMessage());
    }

    @Test
    void new_user() {
        NewUserRequest form = new NewUserRequest(USERNAME, EMAIL);
        doReturn(USER).when(repository).save(any(User.class));
        URI uri = service.newUser(form);
        assertNotNull(uri);
        assertEquals("/users/" + USERNAME, uri.getPath());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void new_user_exception() {
        NewUserRequest form = new NewUserRequest("Any name", EMAIL);
        when(repository.save(any(User.class))).thenThrow(new IllegalArgumentException());
        Exception exception = assertThrows(RequestException.class, () -> service.newUser(form));
        assertNotNull(exception);
        assertEquals("Username must not have any space.", exception.getMessage());
    }

    @Test
    void all_users() {
        when(repository.findAll()).thenReturn(List.of(USER));
        List<UserResponse> response = service.allUsers();
        assertNotNull(response);
        assertNotEquals(0, response.size());
    }

    @Test
    void all_users_empty_list() {
        when(repository.findAll()).thenReturn(List.of());
        List<UserResponse> response = service.allUsers();
        assertNotNull(response);
        assertEquals(0, response.size());
    }

}
