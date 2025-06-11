package com.kaiburr.taskmanager.controller;

import com.kaiburr.taskmanager.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler 
{

    @ExceptionHandler(TaskService.NotFound.class)
    public ResponseEntity<String> handleNotFound(TaskService.NotFound ex) 
    {
        return ResponseEntity.status(404).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) 
    {
        return ResponseEntity.badRequest().body("Invalid input: " + ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(Exception ex) 
    {
        ex.printStackTrace();
        return ResponseEntity.status(500).body("Server error: " + ex.getMessage());
    }
}
