package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa um agendamento de visita no sistema prisional.
 * Conecta visitantes, custodiados e define o status da visita.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(
    name = "agendamento_visita",
    indexes = {
        @Index(name = "idx_agendamento_data", columnList = "data_hora_agendamento"),
        @Index(name = "idx_agendamento_custodiado", columnList = "custodiado_id"),
        @Index(name = "idx_agendamento_status", columnList = "status_id")
    }
)
public class AgendamentoVisita {
    /**
     * Identificador único do agendamento.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    /**
     * Custodiado que receberá a visita.
     * Relacionamento obrigatório muitos-para-um com Custodiado.
     */
    @NotNull(message = "O custodiado é obrigatório")
    @ManyToOne
    @JoinColumn(name = "custodiado_id", nullable = false)
    private Custodiado custodiado;

    /**
     * Visitante que realizará a visita.
     * Relacionamento obrigatório muitos-para-um com Visitante.
     */
    @NotNull(message = "O visitante é obrigatório")
    @ManyToOne
    @JoinColumn(name = "visitante_id", nullable = false)
    private Visitante visitante;

    /**
     * Data e hora agendadas para a visita.
     * Deve ser uma data presente ou futura.
     */
    @NotNull(message = "A data e hora do agendamento são obrigatórias")
    @FutureOrPresent(message = "A data do agendamento deve ser presente ou futura")
    @Column(name = "data_hora_agendamento", nullable = false)
    private LocalDateTime dataHoraAgendamento;

    /**
     * Status atual do agendamento.
     * Relacionamento obrigatório muitos-para-um com Status.
     */
    @NotNull(message = "O status é obrigatório")
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    /**
     * Data e hora de criação do registro.
     * Preenchido automaticamente na criação.
     */
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Data e hora da última atualização do registro.
     * Atualizado automaticamente em modificações.
     */
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    /**
     * Método executado antes de persistir o objeto.
     * Preenche a data de criação.
     */
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    /**
     * Método executado antes de atualizar o objeto.
     * Preenche a data de atualização.
     */
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Construtor personalizado para criação de agendamento.
     * 
     * @param custodiado Custodiado que receberá a visita
     * @param visitante Visitante que realizará a visita
     * @param dataHoraAgendamento Data e hora da visita
     * @param status Status do agendamento
     */
    public AgendamentoVisita(Custodiado custodiado, Visitante visitante, 
                              LocalDateTime dataHoraAgendamento, Status status) {
        this.custodiado = custodiado;
        this.visitante = visitante;
        this.dataHoraAgendamento = dataHoraAgendamento;
        this.status = status;
    }
}