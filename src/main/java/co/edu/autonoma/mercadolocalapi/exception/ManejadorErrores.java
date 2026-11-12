package co.edu.autonoma.mercadolocalapi.exception;

import co.edu.autonoma.mercadolocalapi.dto.response.DetalleErrorResponse;
import co.edu.autonoma.mercadolocalapi.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.OffsetDateTime;
import java.util.List;

@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(RecursoNoEncontradoException ex,
                                                      HttpServletRequest request) {
        return responder(HttpStatus.NOT_FOUND, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> reglaIncumplida(ReglaNegocioException ex,
                                                         HttpServletRequest request) {
        return responder(HttpStatus.CONFLICT, ex.getMessage(), request, List.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> camposInvalidos(MethodArgumentNotValidException ex,
                                                         HttpServletRequest request) {
        List<DetalleErrorResponse> detalles = ex.getBindingResult().getAllErrors().stream()
                .map(error -> new DetalleErrorResponse(
                        error instanceof FieldError campo ? campo.getField() : error.getObjectName(),
                        error.getDefaultMessage()))
                .toList();
        return responder(HttpStatus.BAD_REQUEST, "La petición tiene campos inválidos", request, detalles);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> cuerpoIlegible(HttpMessageNotReadableException ex,
                                                        HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST,
                "El cuerpo no es un JSON válido o tiene un valor no permitido", request, List.of());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tipoInvalido(MethodArgumentTypeMismatchException ex,
                                                      HttpServletRequest request) {
        return responder(HttpStatus.BAD_REQUEST,
                "El valor '" + ex.getValue() + "' no es válido para '" + ex.getName() + "'", request, List.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> datosEnConflicto(DataIntegrityViolationException ex,
                                                          HttpServletRequest request) {
        return responder(HttpStatus.CONFLICT,
                "La operación choca con una regla de la base de datos", request, List.of());
    }

    private ResponseEntity<ErrorResponse> responder(HttpStatus status, String mensaje,
                                                    HttpServletRequest request,
                                                    List<DetalleErrorResponse> detalles) {
        ErrorResponse cuerpo = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                mensaje,
                request.getRequestURI(),
                OffsetDateTime.now(),
                detalles
        );
        return ResponseEntity.status(status).body(cuerpo);
    }
}