package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusResponseDTO {
    
    private Long id;
    private String descricao;
    
    public StatusResponseDTO(Status status) {
        this.id = status.getId();
        this.descricao = status.getDescricao();
    }
}