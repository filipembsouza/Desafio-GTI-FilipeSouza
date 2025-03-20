package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

/**
 * DTO para recebimento de dados de visitante no momento de criação ou atualização.
 * Utiliza composição com PessoaRequestDTO para dados pessoais.
 */
public class VisitanteRequestDTO {
    /**
     * Dados pessoais do visitante.
     * Utiliza validação em cascata para garantir integridade dos dados.
     */
    @Valid
    private PessoaRequestDTO pessoa;

    /**
     * Senha online do visitante.
     * Opcional e limitada a 45 caracteres.
     */
    @Size(max = 45, message = "Senha online deve ter no máximo 45 caracteres")
    private String senhaOnline;

    // Construtores
    public VisitanteRequestDTO() {}

    public VisitanteRequestDTO(PessoaRequestDTO pessoa, String senhaOnline) {
        this.pessoa = pessoa;
        this.senhaOnline = senhaOnline;
    }

    // Getters e Setters
    public PessoaRequestDTO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaRequestDTO pessoa) {
        this.pessoa = pessoa;
    }

    public String getSenhaOnline() {
        return senhaOnline;
    }

    public void setSenhaOnline(String senhaOnline) {
        this.senhaOnline = senhaOnline;
    }
}