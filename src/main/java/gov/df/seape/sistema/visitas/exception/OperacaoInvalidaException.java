package gov.df.seape.sistema.visitas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção especializada para representar operações inválidas no sistema de visitas prisionais.
 * 
 * Esta exceção oferece um mecanismo robusto para sinalizar e comunicar 
 * problemas operacionais específicos durante o processamento de solicitações.
 * 
 * Características principais:
 * - Indica claramente a natureza da operação inválida
 * - Permite inclusão de detalhes adicionais sobre o erro
 * - Mapeada para o código HTTP 400 (Bad Request)
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OperacaoInvalidaException extends RuntimeException {
    
    /**
     * Código identificador do tipo específico de operação inválida.
     * Permite uma categorização mais precisa do erro.
     */
    private final String codigoErro;

    /**
     * Construtor padrão com mensagem de erro.
     * 
     * @param mensagem Descrição detalhada da operação inválida
     */
    public OperacaoInvalidaException(String mensagem) {
        super(mensagem);
        this.codigoErro = "OPERACAO_INVALIDA_GENERICA";
    }

    /**
     * Construtor completo permitindo especificar um código de erro.
     * 
     * @param mensagem Descrição detalhada da operação inválida
     * @param codigoErro Código específico para identificação do tipo de erro
     */
    public OperacaoInvalidaException(String mensagem, String codigoErro) {
        super(mensagem);
        this.codigoErro = codigoErro;
    }

    /**
     * Construtor que permite incluir a causa original do erro.
     * 
     * @param mensagem Descrição detalhada da operação inválida
     * @param causa Exceção original que motivou esta exceção
     */
    public OperacaoInvalidaException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.codigoErro = "OPERACAO_INVALIDA_COM_CAUSA";
    }

    /**
     * Recupera o código específico do erro.
     * 
     * @return Código que categoriza o tipo de operação inválida
     */
    public String getCodigoErro() {
        return codigoErro;
    }
}