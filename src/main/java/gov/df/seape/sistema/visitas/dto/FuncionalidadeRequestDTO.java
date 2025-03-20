package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para recebimento de dados de funcionalidade no momento de criação ou atualização.
 * Contém validações para garantir a integridade dos dados de entrada.
 */
public class FuncionalidadeRequestDTO {
    /**
     * Descrição da funcionalidade.
     * Obrigatória e limitada a 45 caracteres.
     */
    @NotBlank(message = "Descrição da funcionalidade é obrigatória")
    @Size(max = 45, message = "Descrição deve ter no máximo 45 caracteres")
    private String descricao;

    /**
     * Identificador técnico da autoridade (permissão).
     * Obrigatório e limitado a 200 caracteres.
     */
    @NotBlank(message = "Identificador de autoridade é obrigatório")
    @Size(max = 200, message = "Identificador de autoridade deve ter no máximo 200 caracteres")
    private String authority;

    // Getters e Setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}