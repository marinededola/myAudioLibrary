
package com.myaudiolibrary.web.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return entityNotFoundException.getMessage();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }

    /**
     * Exception lancée notamment par la recherche paginée, lorsqu'on essaye de trier par une propriété qui n'existe pas.
     *
     * @param e
     * @return message d'erreur
     */
    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handlePropertyReferenceException(PropertyReferenceException e) {
        return "La propriété " + e.getPropertyName() + " n'existe pas !";
    }

    @ExceptionHandler(ArtistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleArtistException(ArtistException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleConflictException(ConflictException e) {
        return e.getMessage();
    }


    /**
     *
     * @param e
     * @return
     */
    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleEmptyResultDataAccessException(EmptyResultDataAccessException e) {
        return "La ressource à laquelle vous essayer d'accéder n'existe pas ou plus !";
    }

    /**
     * Gestion des problèmes de types sur les paramètres
     *
     * @param ex
     * @return message d'erreur
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected String handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return "Le type du paramètre " + ex.getName() + " est incorrect pour la valeur '" + ex.getValue() + "'";
    }


    /**
     * Gestion des problèmes de connection à la BDD
     *
     * @param ex
     * @return message d'erreur
     */
    @ExceptionHandler(CannotCreateTransactionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected String handleConnectException(CannotCreateTransactionException ex) {
        return "Problème de connexion à la base de données";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException e){
        return e.getConstraintViolations().stream().map(constraintViolation -> {
            String path = constraintViolation.getPropertyPath().toString();
            return path.substring(path.lastIndexOf(".")+1) + " : " + constraintViolation.getMessage();
        }).collect(Collectors.joining(", "));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<Object>(ex.getBindingResult().getAllErrors().stream()
                .map(objectError -> objectError.getDefaultMessage())
                .collect(Collectors.joining(", ")),
                HttpStatus.BAD_REQUEST);

    }
}
