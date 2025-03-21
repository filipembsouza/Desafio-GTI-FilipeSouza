package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.AgendamentoVisita;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AgendamentoVisitaResponseDTO {
    
    private Long id;
    private Long custodiadoId;
    private String nomeCustodiado;
    private String numeroProntuarioCustodiado;
    private Long visitanteId;
    private String nomeVisitante;
    private String cpfVisitante;
    private LocalDateTime dataHoraAgendamento;
    private String dataHoraFormatada;
    private Long statusId;
    private String descricaoStatus;
    private Long unidadePenalId;
    private String nomeUnidadePenal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private String observacoes;
    
    public AgendamentoVisitaResponseDTO(AgendamentoVisita agendamento) {
        this.id = agendamento.getId();
        this.custodiadoId = agendamento.getCustodiado().getId();
        this.nomeCustodiado = agendamento.getCustodiado().getPessoa().getNome();
        this.numeroProntuarioCustodiado = agendamento.getCustodiado().getNumeroProntuario();
        this.visitanteId = agendamento.getVisitante().getId();
        this.nomeVisitante = agendamento.getVisitante().getPessoa().getNome();
        this.cpfVisitante = agendamento.getVisitante().getPessoa().getCpf();
        this.dataHoraAgendamento = agendamento.getDataHoraAgendamento();
        this.dataHoraFormatada = agendamento.getDataHoraAgendamento()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        this.statusId = agendamento.getStatus().getId();
        this.descricaoStatus = agendamento.getStatus().getDescricao();
        this.unidadePenalId = agendamento.getCustodiado().getUnidadePenal().getId();
        this.nomeUnidadePenal = agendamento.getCustodiado().getUnidadePenal().getNome();
        this.dataCriacao = agendamento.getDataCriacao();
        this.dataAtualizacao = agendamento.getDataAtualizacao();
        this.observacoes = agendamento.getObservacoes();
    }
}