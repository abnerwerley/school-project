package br.com.alura.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestException extends RuntimeException {
    private final String message;

    public RequestException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}