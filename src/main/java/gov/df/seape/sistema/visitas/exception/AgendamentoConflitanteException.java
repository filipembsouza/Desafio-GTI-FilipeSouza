package gov.df.seape.sistema.visitas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção especializada para representar conflitos em agendamentos de visitas.
 * 
 * Esta exceção fornece um mecanismo detalhado para sinalizar e comunicar 
 * problemas específicos relacionados a conflitos de agendamento no sistema 
 * de visitas prisionais.
 * 
 * Características principais:
 * - Identifica precisamente a natureza do conflito de agendamento
 * - Permite inclusão de informações contextuais sobre o conflito
 * - Mapeada para o código HTTP 409 (Conflict)
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AgendamentoConflitanteException extends RuntimeException {
    
    /**
     * Código identificador do tipo específico de conflito de agendamento.
     * Permite uma categorização mais precisa do erro.
     */
    private final String codigoConflito;

    /**
     * Construtor padrão com mensagem de erro.
     * 
     * @param mensagem Descrição detalhada do conflito de agendamento
     */
    public AgendamentoConflitanteException(String mensagem) {
        super(mensagem);
        this.codigoConflito = "CONFLITO_AGENDAMENTO_GENERICO";
    }

    /**
     * Construtor que permite especificar um código de conflito.
     * 
     * @param mensagem Descrição detalhada do conflito de agendamento
     * @param codigoConflito Código específico para identificação do tipo de conflito
     */
    public AgendamentoConflitanteException(String mensagem, String codigoConflito) {
        super(mensagem);
        this.codigoConflito = codigoConflito;
    }

    /**
     * Construtor que permite incluir a causa original do conflito.
     * 
     * @param mensagem Descrição detalhada do conflito de agendamento
     * @param causa Exceção original que motivou este conflito
     */
    public AgendamentoConflitanteException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.codigoConflito = "CONFLITO_AGENDAMENTO_COM_CAUSA";
    }

    /**
     * Recupera o código específico do conflito de agendamento.
     * 
     * @return Código que categoriza o tipo de conflito
     */
    public String getCodigoConflito() {
        return codigoConflito;
    }
}