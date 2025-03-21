package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.Visitante;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VisitanteResponseDTO extends PessoaResponseDTO {
    
    private Long visitanteId;
    private String grauParentesco;
    private Boolean senhaOnlineAtiva;
    
    public VisitanteResponseDTO(Visitante visitante) {
        super(
            visitante.getPessoa().getId(),
            visitante.getPessoa().getNome(),
            visitante.getPessoa().getCpf(),
            visitante.getPessoa().getDataNascimento(),
            null
        );
        calcularIdade();
        
        this.visitanteId = visitante.getId();
        this.senhaOnlineAtiva = visitante.getSenhaOnline() != null && !visitante.getSenhaOnline().isEmpty();
    }
}