package gov.df.seape.sistema.visitas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO para filtrar agendamentos de visita.
 */
@Getter
@Setter
@NoArgsConstructor
public class FiltroAgendamentoDTO {
    
    private Long custodiadoId;
    private Long visitanteId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Long statusId;

    /**
     * Construtor para facilitar a criação do DTO com filtros opcionais.
     */
    public FiltroAgendamentoDTO(Long custodiadoId, Long visitanteId, LocalDate dataInicio, LocalDate dataFim, Long statusId) {
        this.custodiadoId = custodiadoId;
        this.visitanteId = visitanteId;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.statusId = statusId;
    }
}
