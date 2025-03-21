package gov.df.seape.sistema.visitas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção lançada quando um horário de visita não é permitido.
 * Indica que o agendamento foi solicitado em um horário fora
 * dos períodos configurados para visitas na unidade prisional.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class HorarioNaoPermitidoException extends RuntimeException {
    
    public HorarioNaoPermitidoException(String message) {
        super(message);
    }
    
    public HorarioNaoPermitidoException(String message, Throwable cause) {
        super(message, cause);
    }
}