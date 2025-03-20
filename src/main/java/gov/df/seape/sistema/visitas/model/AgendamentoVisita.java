package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * Entidade central que representa a tabela AGENDAMENTO_VISITA no banco de dados.
 * Esta é a entidade principal do sistema, responsável por registrar e gerenciar as
 * visitas agendadas para os custodiados (detentos) da unidade prisional.
 * 
 * AgendamentoVisita conecta todas as principais entidades do sistema: Visitante,
 * Custodiado e Status, formando o núcleo funcional da aplicação.
 */

@Getter // Gera automaticamente os métodos getter para os atributos
@Setter // Gera automaticamente os métodos setter para os atributos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Evita problemas de hashCode e equals
@NoArgsConstructor // Construtor padrão sem argumentos
@ToString(exclude = {"custodiado", "visitante", "status"}) // Evita loop infinito ao imprimir a entidade
@Entity
@Table(name = "agendamento_visita", indexes = {
    @Index(name = "idx_agendamento_data", columnList = "data_hora_agendamento"),
    @Index(name = "idx_agendamento_custodiado", columnList = "custodiado_id"),
    @Index(name = "idx_agendamento_status", columnList = "status_id")
})
public class AgendamentoVisita {
    
    /**
     * Identificador único do agendamento.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Inclui apenas o ID no hashCode e equals, evitando problemas
    @Column(name = "id")
    private Long id;

       
    /**
     * Relação com a entidade Custodiado.
     * Indica qual detento receberá a visita.
     * Esta associação é obrigatória, pois toda visita deve ter um custodiado
     * designado como receptor da visita.
     */
    @ManyToOne
    @JoinColumn(name = "custodiado_id", nullable = false)
    @NotNull(message = "O custodiado é obrigatório")
    private Custodiado custodiado;
    
    /**
     * Relação com a entidade Visitante.
     * Indica qual visitante realizará a visita.
     * Esta associação é obrigatória, pois toda visita precisa ter
     * um visitante designado.
     */
    @ManyToOne
    @JoinColumn(name = "visitante_id", nullable = false)
    @NotNull(message = "O visitante é obrigatório")
    private Visitante visitante;
    
    /**
     * Data e hora agendadas para a realização da visita.
     * Campo obrigatório que determina quando a visita deverá ocorrer.
     * Este campo será usado para verificar conflitos de agendamento
     * e para organizar a programação diária de visitas.
     */
    @Column(name = "data_hora_agendamento", nullable = false)
    @FutureOrPresent(message = "A data e hora do agendamento devem estar no futuro ou no presente.") 
    @NotNull(message = "A data e hora do agendamento são obrigatórias")
    private LocalDateTime dataHoraAgendamento;

    
    /**
     * Relação com a entidade Status.
     * Indica o estado atual do agendamento (agendado, cancelado, realizado, etc).
     * Esta associação é obrigatória e permite rastrear o ciclo de vida
     * completo da visita desde seu agendamento inicial.
     */
    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    @NotNull(message = "O status é obrigatório")
    private Status status;
    
    /**
     * Data e hora de criação do registro.
     * Campo de auditoria preenchido automaticamente quando o registro é criado.
     */
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    /**
     * Data e hora da última alteração do registro.
     * Campo de auditoria atualizado automaticamente quando o registro é modificado.
     */
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;
    
    /**
     * Método executado antes de persistir o objeto pela primeira vez.
     * Preenche automaticamente a data de criação.
     */
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }
    
    /**
     * Método executado antes de atualizar o objeto.
     * Preenche automaticamente a data de atualização.
     */
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

    /**
     * Construtor personalizado para facilitar a criação de instâncias.
     */
    public AgendamentoVisita(Custodiado custodiado, Visitante visitante, LocalDateTime dataHoraAgendamento, Status status) {
        this.custodiado = custodiado;
        this.visitante = visitante;
        this.dataHoraAgendamento = dataHoraAgendamento;
        this.status = status;
    }
}