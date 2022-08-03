package WebStoreGroup.WebStore.Handlers;

import WebStoreGroup.WebStore.Exceptions.ExceptionResponse;
import WebStoreGroup.WebStore.Exceptions.NotFoundException;
import WebStoreGroup.WebStore.Exceptions.UserAlreadyExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice

public class ExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExceptionResponse> ValidExceptionHandler(MethodArgumentNotValidException e){
        HttpStatus status=HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse=new ExceptionResponse(
                e.getBindingResult()
                        .getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.toList()),
                e.getMessage(),
                status,
                LocalDateTime.now()
        );
        return new ResponseEntity<>( exceptionResponse,status);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistException.class)
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExceptionResponse> UserAlreadyExistExceptionHandler(UserAlreadyExistException e){
        HttpStatus status=HttpStatus.BAD_REQUEST;
        ExceptionResponse exceptionResponse=new ExceptionResponse(
                e.getMessage(),
                status,
                LocalDateTime.now()
        );
        return new ResponseEntity<>( exceptionResponse,status);
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UsernameNotFoundException.class)
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExceptionResponse> UsernameNotFoundExceptionHandler(UsernameNotFoundException e){
        HttpStatus status=HttpStatus.UNAUTHORIZED;
        ExceptionResponse exceptionResponse=new ExceptionResponse(
                e.getMessage(),
                status,
                LocalDateTime.now()
        );
        return new ResponseEntity<>( exceptionResponse,status);
    }
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExceptionResponse> AuthenticationExceptionHandler(AuthenticationException e){
        HttpStatus status=HttpStatus.UNAUTHORIZED;
        ExceptionResponse exceptionResponse=new ExceptionResponse(
                e.getMessage(),
                status,
                LocalDateTime.now()
        );
        return new ResponseEntity<>( exceptionResponse,status);
    }

    @ExceptionHandler(NotFoundException.class)
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ExceptionResponse> NotFoundExceptionHandler(NotFoundException e){
        HttpStatus status=HttpStatus.NOT_FOUND;
        ExceptionResponse exceptionResponse=new ExceptionResponse(
                e.getMessage(),
                status,
                LocalDateTime.now()
        );
        return new ResponseEntity<>( exceptionResponse,status);
    }


}
