package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para receber dados de criação e atualização da entidade Status.
 * Contém validações para garantir a integridade dos dados recebidos.
 */
public class StatusRequestDTO {
    
    @NotBlank(message = "A descrição do status é obrigatória")
    @Size(max = 45, message = "A descrição do status deve ter no máximo 45 caracteres")
    private String descricao;
    
    // Construtores
    public StatusRequestDTO() {
    }
    
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