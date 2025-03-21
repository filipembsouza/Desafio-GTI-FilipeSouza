package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.VincPerfilFuncionalidade;

/**
 * DTO detalhado para vinculação entre Perfil e Funcionalidade,
 * utilizado para retornar informações completas.
 */
public class VincPerfilFuncionalidadeResponseDTO {
    
    private Long id;
    private PerfilResponseDTO perfil;
    private FuncionalidadeResponseDTO funcionalidade;
    
    public VincPerfilFuncionalidadeResponseDTO() {}
    
    public VincPerfilFuncionalidadeResponseDTO(Long id, 
                                               PerfilResponseDTO perfil, 
                                               FuncionalidadeResponseDTO funcionalidade) {
        this.id = id;
        this.perfil = perfil;
        this.funcionalidade = funcionalidade;
    }
    
    public VincPerfilFuncionalidadeResponseDTO(VincPerfilFuncionalidade vinculo) {
        this.id = vinculo.getId();
        this.perfil = new PerfilResponseDTO(vinculo.getPerfil());
        this.funcionalidade = new FuncionalidadeResponseDTO(vinculo.getFuncionalidade());
    }
    
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
}