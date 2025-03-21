package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.VincPerfilFuncionalidade;

/**
 * DTO para resposta de vínculo entre Perfil e Funcionalidade.
 * Retorna informações detalhadas do vínculo.
 */
public class VincPerfilFuncionalidadeResponseDTO {
    /**
     * Identificador único do vínculo.
     */
    private Long id;

    /**
     * Perfil associado.
     */
    private PerfilResponseDTO perfil;

    /**
     * Funcionalidade associada.
     */
    private FuncionalidadeResponseDTO funcionalidade;

    // Construtores
    public VincPerfilFuncionalidadeResponseDTO() {}

    public VincPerfilFuncionalidadeResponseDTO(Long id, 
                                               PerfilResponseDTO perfil, 
                                               FuncionalidadeResponseDTO funcionalidade) {
        this.id = id;
        this.perfil = perfil;
        this.funcionalidade = funcionalidade;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PerfilResponseDTO getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilResponseDTO perfil) {
        this.perfil = perfil;
    }

    public FuncionalidadeResponseDTO getFuncionalidade() {
        return funcionalidade;
    }

    public void setFuncionalidade(FuncionalidadeResponseDTO funcionalidade) {
        this.funcionalidade = funcionalidade;
    }

    public VincPerfilFuncionalidadeResponseDTO(VincPerfilFuncionalidade vinculo) {
    this.id = vinculo.getId();
    this.perfil = vinculo.getPerfil() != null ? new PerfilResponseDTO(vinculo.getPerfil()) : null;
    this.funcionalidade = vinculo.getFuncionalidade() != null ? new FuncionalidadeResponseDTO(vinculo.getFuncionalidade()) : null;
}
}