package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.AgendamentoVisita;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de Agendamento de Visita.
 */
@Getter
@Setter
@NoArgsConstructor
public class AgendamentoVisitaDTO {
    
    private Long id;
    
    @NotNull(message = "ID do custodiado é obrigatório")
    private Long custodiadoId;
    
    @NotNull(message = "ID do visitante é obrigatório")
    private Long visitanteId;
    
    @NotNull(message = "Data e hora do agendamento são obrigatórias")
    @Future(message = "A data do agendamento deve ser futura")
    private LocalDateTime dataHoraAgendamento;
    
    private Long statusId;
    
    // Campos adicionais para exibição
    private String nomeCustodiado;
    private String nomeVisitante;
    private String descricaoStatus;

    /**
     * Construtor que converte uma entidade AgendamentoVisita para DTO.
     */
    public AgendamentoVisitaDTO(AgendamentoVisita agendamento) {
        this.id = agendamento.getId();
        this.custodiadoId = agendamento.getCustodiado().getId();
        this.visitanteId = agendamento.getVisitante().getId();
        this.dataHoraAgendamento = agendamento.getDataHoraAgendamento();
        this.statusId = agendamento.getStatus().getId();
        this.nomeCustodiado = agendamento.getCustodiado().getPessoa().getNome();
        this.nomeVisitante = agendamento.getVisitante().getPessoa().getNome();
        this.descricaoStatus = agendamento.getStatus().getDescricao();
    }
}
