package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade que representa a tabela CUSTODIADO no banco de dados.
 * Armazena informações específicas de detentos (custodiados) no sistema prisional.
 * Custodiados são as pessoas que estão sob custódia no sistema penitenciário
 * e podem receber visitas.
 */

@Getter // Gera automaticamente os métodos getter para os atributos
@Setter // Gera automaticamente os métodos setter para os atributos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usa apenas o ID para comparação
@ToString(exclude = {"pessoa", "unidadePenal"}) // Evita loop infinito ao imprimir a entidade
@NoArgsConstructor
@Entity
public class Custodiado {
    
    /**
     * Identificador único do custodiado no sistema.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Usa apenas o ID para comparar objetos
    @Column(name = "id")
    private Long id;
    
    /**
     * Relação com a entidade Pessoa.
     * Cada custodiado está associado a uma Pessoa que contém seus dados básicos
     * como nome, CPF e data de nascimento.
     * Esta é uma relação obrigatória (nullable = false).
     */
    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    @NotNull(message = "A pessoa é obrigatória")
    private Pessoa pessoa;
    
    /**
     * Número de prontuário do custodiado no sistema prisional.
     * Este campo é obrigatório e serve como identificador formal
     * do custodiado dentro do sistema penitenciário.
     * Deve ser único no sistema.
     */
    @NotBlank(message = "O número do prontuário é obrigatório")
    @Column(name = "numero_prontuario", length = 45, nullable = false, unique = true)
    private String numeroProntuario;
    
    /**
     * Vulgo ou apelido pelo qual o custodiado é conhecido.
     * Campo opcional que pode ser utilizado para identificação alternativa.
     */
    @Column(name = "vulgo", length = 45, nullable = true)
    private String vulgo;
    
    /**
     * Relação com a entidade UnidadePenal.
     * Indica em qual unidade prisional o custodiado está atualmente.
     * Esta é uma relação obrigatória pois todo custodiado deve estar
     * alocado em alguma unidade penal.
     */
    @ManyToOne
    @JoinColumn(name = "unidade_penal_id", nullable = false)
    @NotNull(message = "A unidade penal é obrigatória")
    private UnidadePenal unidadePenal;

    // Construtor personalizado para facilitar a criação de instâncias
    public Custodiado(Pessoa pessoa, String numeroProntuario, String vulgo, UnidadePenal unidadePenal) {
        this.pessoa = pessoa;
        this.numeroProntuario = numeroProntuario;
        this.vulgo = vulgo;
        this.unidadePenal = unidadePenal;
    }
}