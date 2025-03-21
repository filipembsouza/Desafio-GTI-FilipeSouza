package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusRequestDTO {
    
    @NotBlank(message = "A descrição do status é obrigatória")
    @Size(max = 45, message = "A descrição do status deve ter no máximo 45 caracteres")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "A descrição deve conter apenas letras maiúsculas, números e underscores")
    private String descricao;
}