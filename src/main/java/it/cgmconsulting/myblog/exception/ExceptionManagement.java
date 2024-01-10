package it.cgmconsulting.myblog.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//gestire eccezioni che potrebbero apparire
//spring entra in questa classe se esistono metodi che gestiscono quel determinato errore
//qui inserirò tutti i metodi per le eccezioni che voglio gestire
@ControllerAdvice
public class ExceptionManagement
{
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> methodArgumentTypeMismatchExceptionManagement(MethodArgumentTypeMismatchException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<String> resourceNotFoundExceptionManagement(ResourceNotFoundException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({DefaultAuthorityException.class})
    public ResponseEntity<String> defaultAuthorityExceptionManagement(DefaultAuthorityException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> illegalArgumentExceptionManagement(IllegalArgumentException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({OwnerException.class})
    public ResponseEntity<String> ownerExceptionManagement(OwnerException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({UniqueConstraintViolationException.class})
    public ResponseEntity<String> uniqueConstraintViolationExceptionManagement(UniqueConstraintViolationException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmptyListException.class})
    public ResponseEntity<String> emptyListExceptionManagement(EmptyListException ex)
    {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<String> constraintViolationExceptionManagement(ConstraintViolationException ex)
    {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        List<String> errors = violations.stream() //creo uno stream di oggetti "ConstraintViolation"
                .map(e -> {return e.getMessage(); //qui mappo ciascuna "ConstraintViolation" all'attributo "message", che conterrà il messaggio di errore
                }).collect(Collectors.toList());  //qui i messaggi di errore vengono raccolti e trasformati in una lista di stringhe
        return new ResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleArgumentNotValidValue(MethodArgumentNotValidException ex) {

        BindingResult bindingResults = ex.getBindingResult();
        List<String> errors = bindingResults.getFieldErrors()
                .stream().map(e -> {
                    return e.getField()+": "+e.getDefaultMessage();
                })
                .collect(Collectors.toList());

        return new ResponseEntity<String>(errors.toString(), HttpStatus.BAD_REQUEST);
    }


}
