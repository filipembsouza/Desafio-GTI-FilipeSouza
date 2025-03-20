package gov.df.seape.sistema.visitas.dto;

/**
 * DTO para transferência de dados de visitante na resposta da API.
 * Utiliza composição com PessoaResponseDTO para dados pessoais.
 */
public class VisitanteResponseDTO {
    /**
     * Identificador único do visitante.
     */
    private Long id;

    /**
     * Dados pessoais do visitante.
     */
    private PessoaResponseDTO pessoa;

    /**
     * Indica se o visitante possui senha online.
     */
    private boolean possuiSenhaOnline;

    // Construtores
    public VisitanteResponseDTO() {}

    public VisitanteResponseDTO(Long id, PessoaResponseDTO pessoa, boolean possuiSenhaOnline) {
        this.id = id;
        this.pessoa = pessoa;
        this.possuiSenhaOnline = possuiSenhaOnline;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PessoaResponseDTO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaResponseDTO pessoa) {
        this.pessoa = pessoa;
    }

    public boolean isPossuiSenhaOnline() {
        return possuiSenhaOnline;
    }

    public void setPossuiSenhaOnline(boolean possuiSenhaOnline) {
        this.possuiSenhaOnline = possuiSenhaOnline;
    }
}