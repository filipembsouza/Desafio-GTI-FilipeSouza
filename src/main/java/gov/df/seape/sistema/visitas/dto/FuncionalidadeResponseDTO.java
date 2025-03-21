package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.Funcionalidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionalidadeResponseDTO {
    
    private Long id;
    private String descricao;
    private String authority;
    
    public FuncionalidadeResponseDTO(Funcionalidade funcionalidade) {
        this.id = funcionalidade.getId();
        this.descricao = funcionalidade.getDescricao();
        this.authority = funcionalidade.getAuthority();
    }
}