package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoVisitaRequestDTO {
    
    @NotNull(message = "ID do custodiado é obrigatório")
    private Long custodiadoId;
    
    @NotNull(message = "ID do visitante é obrigatório")
    private Long visitanteId;
    
    @NotNull(message = "Data e hora do agendamento são obrigatórias")
    @FutureOrPresent(message = "A data do agendamento deve ser presente ou futura")
    private LocalDateTime dataHoraAgendamento;
    
    // O status será definido pelo sistema na criação (AGENDADO)
    // Na atualização, pode ser fornecido um novo status
    private Long statusId;
    
    // Campo opcional para observações sobre a visita
    private String observacoes;
}
