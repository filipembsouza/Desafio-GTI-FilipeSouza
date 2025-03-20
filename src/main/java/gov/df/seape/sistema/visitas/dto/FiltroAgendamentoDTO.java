package gov.df.seape.sistema.visitas.dto;

import java.time.LocalDateTime;

/**
 * DTO para filtrar agendamentos de visita.
 * Permite realizar buscas complexas com múltiplos critérios.
 */
public class FiltroAgendamentoDTO {
    /**
     * ID do custodiado para filtrar visitas.
     */
    private Long custodiadoId;

    /**
     * ID do visitante para filtrar visitas.
     */
    private Long visitanteId;

    /**
     * Data e hora inicial para filtro de agendamentos.
     */
    private LocalDateTime dataHoraInicio;

    /**
     * Data e hora final para filtro de agendamentos.
     */
    private LocalDateTime dataHoraFim;

    /**
     * ID do status para filtrar agendamentos.
     */
    private Long statusId;

    /**
     * Número da página para resultados paginados.
     */
    private int pagina = 0;

    /**
     * Tamanho da página para resultados paginados.
     */
    private int tamanhoPagina = 10;

    /**
     * Construtor padrão.
     */
    public FiltroAgendamentoDTO() {}

    /**
     * Construtor com todos os campos.
     */
    public FiltroAgendamentoDTO(Long custodiadoId, Long visitanteId, 
                                 LocalDateTime dataHoraInicio, 
                                 LocalDateTime dataHoraFim, 
                                 Long statusId,
                                 int pagina,
                                 int tamanhoPagina) {
        this.custodiadoId = custodiadoId;
        this.visitanteId = visitanteId;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.statusId = statusId;
        this.pagina = pagina;
        this.tamanhoPagina = tamanhoPagina;
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

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public int getTamanhoPagina() {
        return tamanhoPagina;
    }

    public void setTamanhoPagina(int tamanhoPagina) {
        this.tamanhoPagina = tamanhoPagina;
    }
}