package com.example.finalprojectcoursemanagementsystem.exception.handler;

import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.AlreadyEnrolledException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.ForbiddenAccessException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.IncorrectUsernamePasswordException;
import com.example.finalprojectcoursemanagementsystem.exception.otherexceptions.InsufficientBalanceException;
import com.example.finalprojectcoursemanagementsystem.exception.resourseexceptions.*;
import com.example.finalprojectcoursemanagementsystem.model.response.InformationResponse;
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

    private ResponseEntity<InformationResponse<String>> buildResponse(String messageKey, HttpStatus status, Locale locale) {
        String localizedMessage = messageSource.getMessage(messageKey, null, locale);
        InformationResponse<String> response = new InformationResponse<>(false, localizedMessage, null);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<InformationResponse<String>> handleException(Exception exception, Locale locale) {
        return buildResponse("internal.error", HttpStatus.INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<InformationResponse<String>> handleRuntimeException(RuntimeException exception, Locale locale) {
        return buildResponse("internal.error", HttpStatus.INTERNAL_SERVER_ERROR, locale);
    }

    @ExceptionHandler(AlreadyEnrolledException.class)
    public ResponseEntity<InformationResponse<String>> handleAlreadyEnrolledException(AlreadyEnrolledException ex, Locale locale) {
        return buildResponse("already.enrolled", HttpStatus.CONFLICT, locale);
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<InformationResponse<String>> handleForbiddenAccessException(ForbiddenAccessException ex, Locale locale) {
        return buildResponse("forbidden.access", HttpStatus.FORBIDDEN, locale);
    }

    @ExceptionHandler(IncorrectUsernamePasswordException.class)
    public ResponseEntity<InformationResponse<String>> handleIncorrectUsernamePasswordException(IncorrectUsernamePasswordException ex, Locale locale) {
        return buildResponse("incorrect.username.password", HttpStatus.UNAUTHORIZED, locale);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<InformationResponse<String>> handleInsufficientBalanceException(InsufficientBalanceException ex, Locale locale) {
        return buildResponse("insufficient.balance", HttpStatus.PAYMENT_REQUIRED, locale);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<InformationResponse<String>> handleCourseNotFoundException(CourseNotFoundException ex, Locale locale) {
        return buildResponse("course.not.found", HttpStatus.NOT_FOUND, locale);
    }

    @ExceptionHandler(LessonNotFoundException.class)
    public ResponseEntity<InformationResponse<String>> handleLessonNotFoundException(LessonNotFoundException ex, Locale locale) {
        return buildResponse("lesson.not.found", HttpStatus.NOT_FOUND, locale);
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<InformationResponse<String>> handleQuestionNotFoundException(QuestionNotFoundException ex, Locale locale) {
        return buildResponse("question.not.found", HttpStatus.NOT_FOUND, locale);
    }

    @ExceptionHandler(QuizNotFoundException.class)
    public ResponseEntity<InformationResponse<String>> handleQuizNotFoundException(QuizNotFoundException ex, Locale locale) {
        return buildResponse("quiz.not.found", HttpStatus.NOT_FOUND, locale);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<InformationResponse<String>> handleUserNotFoundException(UserNotFoundException ex, Locale locale) {
        return buildResponse("user.not.found", HttpStatus.NOT_FOUND, locale);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<InformationResponse<String>> handleResourceNotFoundException(ResourceNotFoundException ex, Locale locale) {
        return buildResponse("resource.not.found", HttpStatus.NOT_FOUND, locale);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<InformationResponse<String>> handleValidationException(MethodArgumentNotValidException ex, Locale locale) {
        String errorMessage = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return new ResponseEntity<>(new InformationResponse<>(false, errorMessage, null), HttpStatus.BAD_REQUEST);
    }

}
