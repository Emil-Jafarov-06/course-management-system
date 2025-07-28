package com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
