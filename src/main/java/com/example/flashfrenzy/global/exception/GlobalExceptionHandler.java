package com.example.flashfrenzy.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String ExHandler(Exception ex) {
        log.error(ex.getMessage());
        log.error(ex.toString());
        ex.printStackTrace();
        return "redirect:/";
    }
}
