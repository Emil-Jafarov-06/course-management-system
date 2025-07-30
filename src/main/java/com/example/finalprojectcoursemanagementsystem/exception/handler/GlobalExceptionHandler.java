package com.example.finalprojectcoursemanagementsystem.exception.handler;

import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.AlreadyEnrolledException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.ForbiddenAccessException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.IncorrectUsernamePasswordException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.InsufficientBalanceException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Objects;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AlreadyEnrolledException.class)
    public ResponseEntity<String> handleAlreadyEnrolledException(AlreadyEnrolledException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<String> handleForbiddenAccessException(ForbiddenAccessException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IncorrectUsernamePasswordException.class)
    public ResponseEntity<String> handleIncorrectUsernamePasswordException(IncorrectUsernamePasswordException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<String> handleInsufficientBalanceException(InsufficientBalanceException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<String> handleCourseNotFoundException(CourseNotFoundException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<String> handleLessonNotFoundException(LessonNotFoundException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<String> handleQuestionNotFoundException(QuestionNotFoundException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuizNotFoundException.class)
    public ResponseEntity<String> handleQuizNotFoundException(QuizNotFoundException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception, Locale locale){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, Locale locale){
        return new ResponseEntity<>(Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

}
