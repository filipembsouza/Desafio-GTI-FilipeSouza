package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.Perfil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerfilResponseDTO {
    
    private Long id;
    private String descricao;
    private List<FuncionalidadeResponseDTO> funcionalidades;
    
    public PerfilResponseDTO(Perfil perfil) {
        this.id = perfil.getId();
        this.descricao = perfil.getDescricao();
        
        if (perfil.getFuncionalidades() != null) {
            this.funcionalidades = perfil.getFuncionalidades().stream()
                .map(FuncionalidadeResponseDTO::new)
                .collect(Collectors.toList());
        } else {
            this.funcionalidades = new ArrayList<>();
        }
    }
}