package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade que representa um custodiado (detento) no sistema prisional.
 * Armazena informações específicas sobre o custodiado.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "custodiado")
public class Custodiado {
    
    /**
     * Identificador único do custodiado.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    /**
     * Pessoa associada ao custodiado.
     * Relacionamento obrigatório um-para-um com a entidade Pessoa.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    @NotNull(message = "A pessoa é obrigatória")
    private Pessoa pessoa;
    
    /**
     * Número de prontuário do custodiado.
     * Deve ser único no sistema e limitado a 45 caracteres.
     */
    @NotBlank(message = "Número do prontuário é obrigatório")
    @Column(name = "numero_prontuario", length = 45, nullable = false, unique = true)
    private String numeroProntuario;
    
    /**
     * Vulgo ou apelido do custodiado.
     * Campo opcional, limitado a 45 caracteres.
     */
    @Column(name = "vulgo", length = 45)
    private String vulgo;
    
    /**
     * Unidade penal onde o custodiado está alocado.
     * Relacionamento obrigatório muitos-para-um com UnidadePenal.
     */
    @ManyToOne
    @JoinColumn(name = "unidade_penal_id", nullable = false)
    @NotNull(message = "A unidade penal é obrigatória")
    private UnidadePenal unidadePenal;
    
    /**
     * Construtor personalizado para criação de custodiado.
     * 
     * @param pessoa Pessoa associada
     * @param numeroProntuario Número do prontuário
     * @param vulgo Apelido (opcional)
     * @param unidadePenal Unidade penal
     */
    public Custodiado(Pessoa pessoa, String numeroProntuario, String vulgo, UnidadePenal unidadePenal) {
        this.pessoa = pessoa;
        this.numeroProntuario = numeroProntuario;
        this.vulgo = vulgo;
        this.unidadePenal = unidadePenal;
    }
    
    /**
     * Implementação customizada de equals para evitar duplicidade.
     * Se os ids estiverem definidos, compara-os. Caso contrário, compara o número do prontuário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Custodiado)) return false;
        Custodiado that = (Custodiado) o;
        if (this.id != null && that.id != null) {
            return this.id.equals(that.id);
        }
        return numeroProntuario != null ? numeroProntuario.equals(that.numeroProntuario) : that.numeroProntuario == null;
    }
    
    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return numeroProntuario != null ? numeroProntuario.hashCode() : 0;
    }
}
