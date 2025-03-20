package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CustodiadoRequestDTO extends PessoaRequestDTO {
    
    @NotBlank(message = "O número do prontuário é obrigatório")
    @Size(max = 45, message = "Número do prontuário deve ter no máximo 45 caracteres")
    private String numeroProntuario;
    
    @Size(max = 45, message = "Vulgo deve ter no máximo 45 caracteres")
    private String vulgo;
    
    @NotNull(message = "A unidade penal é obrigatória")
    private Long unidadePenalId;

    // Getters e Setters
    public String getNumeroProntuario() {
        return numeroProntuario;
    }

    public void setNumeroProntuario(String numeroProntuario) {
        this.numeroProntuario = numeroProntuario;
    }

    public String getVulgo() {
        return vulgo;
    }

    public void setVulgo(String vulgo) {
        this.vulgo = vulgo;
    }

    public Long getUnidadePenalId() {
        return unidadePenalId;
    }

    public void setUnidadePenalId(Long unidadePenalId) {
        this.unidadePenalId = unidadePenalId;
    }
}