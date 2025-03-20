package gov.df.seape.sistema.visitas.model;

import java.util.List;  
import java.util.ArrayList;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade que representa a tabela UNIDADE_PENAL no banco de dados.
 * Armazena informações sobre as unidades prisionais onde os custodiados
 * estão detidos e onde ocorrem as visitas.
 * Exemplos de unidades penais: Penitenciária, Colônia Penal, Centro de Detenção.
 */
@Getter // Gera automaticamente os métodos getter para os atributos
@Setter // Gera automaticamente os métodos setter para os atributos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usa apenas o ID para comparação
@ToString(exclude = "custodiados") // Evita loops infinitos e problemas de desempenho
@NoArgsConstructor
@Entity
@Table(name = "unidade_penal")  // Nome explícito da tabela para corresponder ao diagrama
public class UnidadePenal {
    
    /**
     * Identificador único da unidade penal.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Usa apenas o ID para comparar objetos
    @Column(name = "id")
    private Long id;
    
    /**
     * Nome oficial da unidade penal.
     * Este campo é obrigatório e permite identificar a unidade prisional.
     * Exemplo: "Penitenciária I do Distrito Federal ", "Centro de Detenção Provisória"
     */
    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;
    
    /**
     * Descrição ou informações adicionais sobre a unidade penal.
     * Campo opcional que pode conter detalhes como localização, tipo de regime, etc.
     */
    @Column(name = "descricao", length = 45, nullable = true)
    private String descricao; 
    
    /**
     * Relação com os custodiados.
     * Uma unidade penal pode ter vários custodiados associados a ela.
     * Essa relação permite que seja possível consultar todos os custodiados
     * que pertencem a determinada unidade penal.
     * O carregamento LAZY melhora o desempenho para grandes volumes de dados.
     */
    @OneToMany(mappedBy = "unidadePenal", fetch = FetchType.LAZY)
    private List<Custodiado> custodiados = new ArrayList<>();

    // Construtor personalizado para facilitar a criação de instâncias
    public UnidadePenal(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}