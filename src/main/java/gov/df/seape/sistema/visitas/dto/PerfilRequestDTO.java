package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para recebimento de dados de perfil no momento de criação ou atualização.
 * Contém validações para garantir a integridade dos dados de entrada.
 */
public class PerfilRequestDTO {
    /**
     * Descrição do perfil.
     * Obrigatória e limitada a 100 caracteres.
     */
    @NotBlank(message = "Descrição do perfil é obrigatória")
    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    private String descricao;

    // Getters e Setters
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}