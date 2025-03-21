package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.Perfil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponseDTO {
    private Long id;
    private String descricao;
    private List<FuncionalidadeResponseDTO> funcionalidades = new ArrayList<>();
    
    public PerfilResponseDTO(Perfil perfil) {
        if (perfil != null) {
            this.id = perfil.getId();
            this.descricao = perfil.getDescricao();
        }
    }
    
    public PerfilResponseDTO(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
}