package gov.df.seape.sistema.visitas.dto;

import java.time.LocalDateTime;

/**
 * DTO para resposta de agendamento de visita.
 * Contém informações detalhadas do agendamento.
 */
public class AgendamentoVisitaResponseDTO {
    /**
     * ID único do agendamento.
     */
    private Long id;

    /**
     * Custodiado que receberá a visita.
     */
    private CustodiadoResponseDTO custodiado;

    /**
     * Visitante que realizará a visita.
     */
    private VisitanteResponseDTO visitante;

    /**
     * Data e hora do agendamento.
     */
    private LocalDateTime dataHoraAgendamento;

    /**
     * Status atual do agendamento.
     */
    private StatusResponseDTO status;

    /**
     * Data de criação do agendamento.
     */
    private LocalDateTime dataCriacao;

    /**
     * Data da última atualização do agendamento.
     */
    private LocalDateTime dataAtualizacao;

    /**
     * Construtor padrão.
     */
    public AgendamentoVisitaResponseDTO() {}

    /**
     * Construtor com todos os campos.
     */
    public AgendamentoVisitaResponseDTO(Long id, 
                                         CustodiadoResponseDTO custodiado, 
                                         VisitanteResponseDTO visitante, 
                                         LocalDateTime dataHoraAgendamento, 
                                         StatusResponseDTO status,
                                         LocalDateTime dataCriacao,
                                         LocalDateTime dataAtualizacao) {
        this.id = id;
        this.custodiado = custodiado;
        this.visitante = visitante;
        this.dataHoraAgendamento = dataHoraAgendamento;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CustodiadoResponseDTO getCustodiado() {
        return custodiado;
    }

    public void setCustodiado(CustodiadoResponseDTO custodiado) {
        this.custodiado = custodiado;
    }

    public VisitanteResponseDTO getVisitante() {
        return visitante;
    }

    public void setVisitante(VisitanteResponseDTO visitante) {
        this.visitante = visitante;
    }

    public LocalDateTime getDataHoraAgendamento() {
        return dataHoraAgendamento;
    }

    public void setDataHoraAgendamento(LocalDateTime dataHoraAgendamento) {
        this.dataHoraAgendamento = dataHoraAgendamento;
    }

    public StatusResponseDTO getStatus() {
        return status;
    }

    public void setStatus(StatusResponseDTO status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}