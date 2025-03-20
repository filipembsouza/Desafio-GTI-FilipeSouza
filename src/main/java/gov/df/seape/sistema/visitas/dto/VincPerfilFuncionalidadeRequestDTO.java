package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para criação de vínculo entre Perfil e Funcionalidade.
 * Representa a associação de permissões a um perfil.
 */
public class VincPerfilFuncionalidadeRequestDTO {
    /**
     * ID do perfil ao qual a funcionalidade será vinculada.
     */
    @NotNull(message = "O ID do perfil é obrigatório")
    private Long perfilId;

    /**
     * ID da funcionalidade a ser vinculada ao perfil.
     */
    @NotNull(message = "O ID da funcionalidade é obrigatório")
    private Long funcionalidadeId;

    // Construtores
    public VincPerfilFuncionalidadeRequestDTO() {}

    public VincPerfilFuncionalidadeRequestDTO(Long perfilId, Long funcionalidadeId) {
        this.perfilId = perfilId;
        this.funcionalidadeId = funcionalidadeId;
    }

    // Getters e Setters
    public Long getPerfilId() {
        return perfilId;
    }

    public void setPerfilId(Long perfilId) {
        this.perfilId = perfilId;
    }

    public Long getFuncionalidadeId() {
        return funcionalidadeId;
    }

    public void setFuncionalidadeId(Long funcionalidadeId) {
        this.funcionalidadeId = funcionalidadeId;
    }
}