package gov.df.seape.sistema.visitas.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entidade que representa um agendamento de visita no sistema prisional.
 * Gerencia as informações de visitas entre visitantes e custodiados.
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
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @NotNull(message = "O custodiado é obrigatório")
    @ManyToOne
    @JoinColumn(name = "custodiado_id", nullable = false)
    private Custodiado custodiado;

    @NotNull(message = "O visitante é obrigatório")
    @ManyToOne
    @JoinColumn(name = "visitante_id", nullable = false)
    private Visitante visitante;

    @NotNull(message = "A data e hora do agendamento são obrigatórias")
    @FutureOrPresent(message = "A data do agendamento deve ser presente ou futura")
    @Column(name = "data_hora_agendamento", nullable = false)
    private LocalDateTime dataHoraAgendamento;

    @NotNull(message = "O status é obrigatório")
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Verifica conflito de agendamento.
     * 
     * @param outroAgendamento Agendamento a ser comparado
     * @return true se houver conflito, false caso contrário
     */
    public boolean verificarConflito(AgendamentoVisita outroAgendamento) {
        return this.custodiado.equals(outroAgendamento.custodiado) 
            && this.dataHoraAgendamento.equals(outroAgendamento.dataHoraAgendamento);
    }

    /**
     * Valida horário de visita conforme regras do sistema.
     * 
     * @return true se horário válido, false caso contrário
     */
    public boolean validarHorarioVisita() {
        LocalTime horario = dataHoraAgendamento.toLocalTime();
        DayOfWeek diaSemana = dataHoraAgendamento.getDayOfWeek();
        
        return diaSemana != DayOfWeek.MONDAY 
            && horario.isAfter(LocalTime.of(9, 0)) 
            && horario.isBefore(LocalTime.of(17, 0));
    }
}