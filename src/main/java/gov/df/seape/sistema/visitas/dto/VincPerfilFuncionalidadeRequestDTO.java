package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.VincPerfilFuncionalidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para envio de dados de vínculo entre Perfil e Funcionalidade.
 * Utilizado para representar associações existentes entre perfis e funcionalidades.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VincPerfilFuncionalidadeResponseDTO {
    
    private Long id;
    private Long perfilId;
    private String descricaoPerfil;
    private Long funcionalidadeId;
    private String descricaoFuncionalidade;
    private String authority;
    
    /**
     * Construtor que converte uma entidade VincPerfilFuncionalidade para DTO.
     */
    public VincPerfilFuncionalidadeResponseDTO(VincPerfilFuncionalidade vinculo) {
        this.id = vinculo.getId();
        this.perfilId = vinculo.getPerfil().getId();
        this.descricaoPerfil = vinculo.getPerfil().getDescricao();
        this.funcionalidadeId = vinculo.getFuncionalidade().getId();
        this.descricaoFuncionalidade = vinculo.getFuncionalidade().getDescricao();
        this.authority = vinculo.getFuncionalidade().getAuthority();
    }
}