package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para recebimento de dados de status no momento de criação ou atualização.
 * Contém validações para garantir a integridade dos dados de entrada.
 */
public class StatusRequestDTO {
    /**
     * Descrição do status.
     * Deve conter apenas letras maiúsculas, números e underscores.
     * Limitado a 45 caracteres.
     */
    @NotBlank(message = "Descrição do status é obrigatória")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Descrição deve conter apenas letras maiúsculas, números e underscores")
    @Size(max = 45, message = "Descrição deve ter no máximo 45 caracteres")
    private String descricao;

    // Construtores
    public StatusRequestDTO() {}

    public StatusRequestDTO(String descricao) {
        this.descricao = descricao;
    }

    // Getters e Setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}