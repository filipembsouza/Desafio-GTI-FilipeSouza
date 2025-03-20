package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade associativa que representa a tabela VINC_PERFIL_FUNCIONALIDADE no banco de dados.
 * Esta entidade implementa um relacionamento muitos-para-muitos entre Perfis e Funcionalidades,
 * permitindo atribuir diversas funcionalidades a diferentes perfis de forma flexível.
 * 
 * Através desta entidade, o sistema pode definir precisamente quais ações cada tipo
 * de usuário pode realizar, criando uma matriz de permissões altamente configurável.
 */
@Getter // Gera automaticamente os métodos getter para os atributos
@Setter // Gera automaticamente os métodos setter para os atributos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usa apenas o ID para comparação
@ToString(exclude = {"perfil", "funcionalidade"}) // Evita loop infinito ao imprimir a entidade
@NoArgsConstructor
@Entity
@Table(
    name = "vinc_perfil_funcionalidade",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_perfil_funcionalidade", 
            columnNames = {"id_perfil", "funcionalidade_id"}
        )
    }
)
public class VincPerfilFuncionalidade {
    
    /**
     * Identificador único do vínculo entre perfil e funcionalidade.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Usa apenas o ID para comparar objetos
    @Column(name = "id")
    private Long id;
    
    /**
     * Relação com a entidade Perfil.
     * Indica qual perfil de usuário está recebendo uma determinada funcionalidade.
     * Esta associação é obrigatória e representa um dos lados da relação
     * muitos-para-muitos entre perfis e funcionalidades.
     * 
     * Por exemplo: o perfil "Agente Penitenciário" pode ter várias funcionalidades
     * associadas, como "Verificar Visitantes", "Registrar Entrada", etc.
     */
    @NotNull(message = "O perfil é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;
    
    /**
     * Relação com a entidade Funcionalidade.
     * Indica qual funcionalidade está sendo atribuída a um determinado perfil.
     * Esta associação é obrigatória e representa o outro lado da relação
     * muitos-para-muitos entre perfis e funcionalidades.
     * 
     * Por exemplo: a funcionalidade "Agendar Visita" pode estar associada
     * a vários perfis, como "Atendente", "Administrador", etc.
     */
    @NotNull(message = "A funcionalidade é obrigatória")
    @ManyToOne
    @JoinColumn(name = "funcionalidade_id", nullable = false)
    private Funcionalidade funcionalidade;
    
    /**
     * Construtor que facilita a criação de vínculos entre perfil e funcionalidade.
     * 
     * @param perfil O perfil a ser vinculado
     * @param funcionalidade A funcionalidade a ser vinculada
     */
    public VincPerfilFuncionalidade(Perfil perfil, Funcionalidade funcionalidade) {
        this.perfil = perfil;
        this.funcionalidade = funcionalidade;
    }
}