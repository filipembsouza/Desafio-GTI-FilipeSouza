package gov.df.seape.sistema.visitas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Tratador centralizado de exceções para o Sistema de Visitas Prisionais.
 * 
 * Responsável por capturar e transformar diferentes tipos de exceções 
 * em respostas HTTP padronizadas e informativas, garantindo uma 
 * comunicação clara de erros para os clientes da API.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Representa uma estrutura padronizada para respostas de erro.
     * Fornece informações consistentes sobre exceções ocorridas no sistema.
     */
    public record ErrorResponse(
        int status, 
        String message, 
        LocalDateTime timestamp
    ) {}

    /**
     * Trata exceções de recursos não encontrados.
     * 
     * Retorna uma resposta 404 com detalhes do recurso não localizado.
     */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        log.error("Recurso não encontrado: {}", ex.getMessage());
        
        ErrorResponse response = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Trata exceções de agendamentos conflitantes.
     * 
     * Retorna uma resposta 409 indicando conflito de agendamento.
     */
    @ExceptionHandler(AgendamentoConflitanteException.class)
    public ResponseEntity<ErrorResponse> handleAgendamentoConflitante(AgendamentoConflitanteException ex) {
        log.warn("Conflito de agendamento: {}", ex.getMessage());
        
        ErrorResponse response = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Trata exceções de operações inválidas.
     * 
     * Retorna uma resposta 400 com detalhes da operação inválida.
     */
    @ExceptionHandler(OperacaoInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleOperacaoInvalida(OperacaoInvalidaException ex) {
        log.error("Operação inválida: {}", ex.getMessage());
        
        ErrorResponse response = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata exceções de validação de argumentos.
     * 
     * Captura erros de validação de campos, fornecendo detalhes 
     * específicos sobre quais campos falharam na validação.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Erro de validação de argumentos");
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Trata exceções genéricas não mapeadas especificamente.
     * 
     * Funciona como um tratador de último recurso para qualquer 
     * exceção não capturada por handlers mais específicos.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Erro interno no servidor: {}", ex.getMessage(), ex);
        
        ErrorResponse response = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Ocorreu um erro interno no servidor: " + ex.getMessage(),
            LocalDateTime.now()
        );
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}