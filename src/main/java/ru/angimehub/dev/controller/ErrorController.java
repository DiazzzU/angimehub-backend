package ru.angimehub.dev.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.angimehub.dev.exceptions.ForbiddenException;
import ru.angimehub.dev.exceptions.NotFoundException;
import ru.angimehub.dev.exceptions.UnauthorizedException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class ErrorController {
    @ExceptionHandler({
            ForbiddenException.class
    })
    public void onForbidden(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler({
            NotFoundException.class
    })
    public void onNotFoundException(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({
            UnauthorizedException.class
    })
    public void onUnauthorizedException(HttpServletResponse response, Exception e) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    }
}
