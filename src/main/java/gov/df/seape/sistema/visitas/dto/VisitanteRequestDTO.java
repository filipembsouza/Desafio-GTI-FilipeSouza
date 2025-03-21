package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

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
    
    /**
     * Grau de parentesco do visitante com o custodiado.
     */
    private String grauParentesco;

    // Construtores
    public VisitanteRequestDTO() {}

    public VisitanteRequestDTO(PessoaRequestDTO pessoa, String senhaOnline, String grauParentesco) {
        this.pessoa = pessoa;
        this.senhaOnline = senhaOnline;
        this.grauParentesco = grauParentesco;
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
    
    public String getGrauParentesco() {
        return grauParentesco;
    }

    public void setGrauParentesco(String grauParentesco) {
        this.grauParentesco = grauParentesco;
    }
    
    // Métodos delegados para acessar dados da pessoa
    public String getNome() {
        return pessoa != null ? pessoa.getNome() : null;
    }

    public String getCpf() {
        return pessoa != null ? pessoa.getCpf() : null;
    }

    public LocalDate getDataNascimento() {
        return pessoa != null ? pessoa.getDataNascimento() : null;
    }
}