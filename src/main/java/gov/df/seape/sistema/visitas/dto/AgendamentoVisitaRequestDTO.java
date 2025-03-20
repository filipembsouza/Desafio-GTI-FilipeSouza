package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO para criação de um novo agendamento de visita.
 * Representa os dados necessários para agendar uma visita.
 */
public class AgendamentoVisitaRequestDTO {
    /**
     * ID do custodiado que receberá a visita.
     * Campo obrigatório.
     */
    @NotNull(message = "ID do custodiado é obrigatório")
    private Long custodiadoId;

    /**
     * ID do visitante que realizará a visita.
     * Campo obrigatório.
     */
    @NotNull(message = "ID do visitante é obrigatório")
    private Long visitanteId;

    /**
     * Data e hora do agendamento.
     * Deve ser uma data futura.
     */
    @NotNull(message = "Data e hora do agendamento são obrigatórias")
    @Future(message = "A data do agendamento deve ser futura")
    private LocalDateTime dataHoraAgendamento;

    /**
     * Construtor padrão.
     */
    public AgendamentoVisitaRequestDTO() {}

    /**
     * Construtor com todos os campos.
     */
    public AgendamentoVisitaRequestDTO(Long custodiadoId, Long visitanteId, LocalDateTime dataHoraAgendamento) {
        this.custodiadoId = custodiadoId;
        this.visitanteId = visitanteId;
        this.dataHoraAgendamento = dataHoraAgendamento;
    }

    // Getters e Setters
    public Long getCustodiadoId() {
        return custodiadoId;
    }

    public void setCustodiadoId(Long custodiadoId) {
        this.custodiadoId = custodiadoId;
    }

    public Long getVisitanteId() {
        return visitanteId;
    }

    public void setVisitanteId(Long visitanteId) {
        this.visitanteId = visitanteId;
    }

    public LocalDateTime getDataHoraAgendamento() {
        return dataHoraAgendamento;
    }

    public void setDataHoraAgendamento(LocalDateTime dataHoraAgendamento) {
        this.dataHoraAgendamento = dataHoraAgendamento;
    }
}