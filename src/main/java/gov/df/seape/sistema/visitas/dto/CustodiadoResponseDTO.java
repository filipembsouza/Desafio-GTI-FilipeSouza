package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.Custodiado;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustodiadoResponseDTO extends PessoaResponseDTO {
    
    private String numeroProntuario;
    private String vulgo;
    private Long unidadePenalId;
    private String nomeUnidadePenal;
    
    public CustodiadoResponseDTO(Custodiado custodiado) {
        super(
            custodiado.getPessoa().getId(),
            custodiado.getPessoa().getNome(),
            custodiado.getPessoa().getCpf(),
            custodiado.getPessoa().getDataNascimento(),
            null
        );
        calcularIdade();
        
        this.numeroProntuario = custodiado.getNumeroProntuario();
        this.vulgo = custodiado.getVulgo();
        this.unidadePenalId = custodiado.getUnidadePenal().getId();
        this.nomeUnidadePenal = custodiado.getUnidadePenal().getNome();
    }
}