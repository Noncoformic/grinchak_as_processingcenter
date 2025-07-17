package ru.edme.exception;

import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**

 Глобальный обработчик исключений для REST-контроллеров.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**

     Обработка ошибок валидации DTO (@Valid).
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));

        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Validation failed"
        );
        pd.setProperty("errors", errors);
        return pd;
    }

    /**

     Обработка ошибок, когда сущность не найдена в БД.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ProblemDetail handleNotFound(EntityNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    /**

     Ошибки удалённых сервисов: HTTP 4xx.
     */
    @ExceptionHandler(ClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleClientError(ClientErrorException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Client error: " + ex.getMessage()
        );
    }

    /**

     Ошибки удалённых сервисов, допускающие ретрай: HTTP 5xx или пустой ответ.
     */
    @ExceptionHandler(RetryableRemoteServiceException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ProblemDetail handleRetryable(RetryableRemoteServiceException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Temporary error: " + ex.getMessage()
        );
    }

    /**

     Прочие ошибки удалённых сервисов.
     */
    @ExceptionHandler(RemoteServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleRemote(RemoteServiceException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Service error: " + ex.getMessage()
        );
    }

    /**

     Общий fallback для непредусмотренных исключений.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleAny(Exception ex) {
// TODO: логирование ex
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );
    }
}