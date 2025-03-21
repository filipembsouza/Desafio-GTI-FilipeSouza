package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CustodiadoRequestDTO extends PessoaRequestDTO {
    
    @NotBlank(message = "O número do prontuário é obrigatório")
    @Size(min = 5, max = 45, message = "Número do prontuário deve ter entre 5 e 45 caracteres")
    private String numeroProntuario;
    
    @Size(max = 45, message = "Vulgo deve ter no máximo 45 caracteres")
    private String vulgo;
    
    @NotNull(message = "A unidade penal é obrigatória")
    private Long unidadePenalId;
}