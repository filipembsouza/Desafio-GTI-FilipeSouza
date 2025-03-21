package gov.df.seape.sistema.visitas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção especializada para representar recursos não encontrados no sistema de visitas prisionais.
 * 
 * Esta exceção fornece um mecanismo detalhado para sinalizar e comunicar 
 * problemas relacionados à ausência de recursos específicos durante as operações do sistema.
 * 
 * Características principais:
 * - Identifica precisamente o recurso não encontrado
 * - Permite inclusão de informações contextuais sobre a ausência do recurso
 * - Mapeada para o código HTTP 404 (Not Found)
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecursoNaoEncontradoException extends RuntimeException {
    
    /**
     * Código identificador do tipo específico de recurso não encontrado.
     * Permite uma categorização mais precisa do erro.
     */
    private final String codigoRecurso;

    /**
     * Construtor padrão com mensagem de erro.
     * 
     * @param mensagem Descrição detalhada do recurso não encontrado
     */
    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
        this.codigoRecurso = "RECURSO_NAO_ENCONTRADO_GENERICO";
    }

    /**
     * Construtor que permite especificar um código de recurso.
     * 
     * @param mensagem Descrição detalhada do recurso não encontrado
     * @param codigoRecurso Código específico para identificação do tipo de recurso ausente
     */
    public RecursoNaoEncontradoException(String mensagem, String codigoRecurso) {
        super(mensagem);
        this.codigoRecurso = codigoRecurso;
    }

    /**
     * Construtor que permite incluir a causa original da não localização do recurso.
     * 
     * @param mensagem Descrição detalhada do recurso não encontrado
     * @param causa Exceção original que motivou a não localização do recurso
     */
    public RecursoNaoEncontradoException(String mensagem, Throwable causa) {
        super(mensagem, causa);
        this.codigoRecurso = "RECURSO_NAO_ENCONTRADO_COM_CAUSA";
    }

    /**
     * Recupera o código específico do recurso não encontrado.
     * 
     * @return Código que categoriza o tipo de recurso ausente
     */
    public String getCodigoRecurso() {
        return codigoRecurso;
    }
}