package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para solicitação de vínculo entre Perfil e Funcionalidade.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VincPerfilFuncionalidadeRequestDTO {
    
    @NotNull(message = "ID do perfil é obrigatório")
    private Long perfilId;
    
    @NotNull(message = "ID da funcionalidade é obrigatório")
    private Long funcionalidadeId;
}