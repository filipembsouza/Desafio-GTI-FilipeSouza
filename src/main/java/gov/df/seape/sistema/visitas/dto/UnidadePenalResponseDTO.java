package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.UnidadePenal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnidadePenalResponseDTO {
    
    private Long id;
    private String nome;
    private String descricao;
    private Integer totalCustodiados;
    
    public UnidadePenalResponseDTO(UnidadePenal unidadePenal) {
        if (unidadePenal != null) {
            this.id = unidadePenal.getId();
            this.nome = unidadePenal.getNome();
            this.descricao = unidadePenal.getDescricao();
            this.totalCustodiados = unidadePenal.getCustodiados() != null ? 
                                    unidadePenal.getCustodiados().size() : 0;
        }
    }
}