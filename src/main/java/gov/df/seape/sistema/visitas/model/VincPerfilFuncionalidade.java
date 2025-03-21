package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * Entidade associativa que representa a tabela VINC_PERFIL_FUNCIONALIDADE no banco de dados.
 * Esta entidade implementa um relacionamento muitos-para-muitos entre Perfis e Funcionalidades,
 * permitindo atribuir diversas funcionalidades a diferentes perfis de forma flexível.
 * 
 * Através desta entidade, o sistema pode definir precisamente quais ações cada tipo
 * de usuário pode realizar, criando uma matriz de permissões altamente configurável.
 */
@Getter
@Setter
@ToString(exclude = {"perfil", "funcionalidade"})
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
    @Column(name = "id")
    private Long id;
    
    /**
     * Relação com a entidade Perfil.
     * Indica qual perfil de usuário está recebendo uma determinada funcionalidade.
     * Esta associação é obrigatória e representa um dos lados da relação
     * muitos-para-muitos entre perfis e funcionalidades.
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VincPerfilFuncionalidade)) return false;
        VincPerfilFuncionalidade that = (VincPerfilFuncionalidade) o;
        // Se ambos possuem id, comparamos apenas os ids.
        if (this.id != null && that.id != null) {
            return this.id.equals(that.id);
        }
        // Se os ids forem nulos, compara os atributos perfil e funcionalidade.
        return Objects.equals(perfil, that.perfil) &&
               Objects.equals(funcionalidade, that.funcionalidade);
    }
    
    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(id);
        }
        return Objects.hash(perfil, funcionalidade);
    }
}
