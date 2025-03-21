package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionalidadeRequestDTO {
    
    @NotBlank(message = "Descrição da funcionalidade é obrigatória")
    @Size(min = 3, max = 45, message = "Descrição deve ter entre 3 e 45 caracteres")
    private String descricao;
    
    @NotBlank(message = "Authority é obrigatória")
    @Size(min = 3, max = 200, message = "Authority deve ter entre 3 e 200 caracteres")
    private String authority;
}