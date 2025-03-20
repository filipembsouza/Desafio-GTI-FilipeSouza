package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Entidade que representa um custodiado (detento) no sistema prisional.
 * Armazena informações específicas sobre o custodiado.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "custodiado")
public class Custodiado {
    /**
     * Identificador único do custodiado.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    /**
     * Pessoa associada ao custodiado.
     * Relacionamento obrigatório um-para-um com a entidade Pessoa.
     */
    @OneToOne
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
}