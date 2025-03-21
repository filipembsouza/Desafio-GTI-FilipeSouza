package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa unidades prisionais no sistema.
 * Gerencia informações das instituições onde custodiados estão alocados.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "custodiados")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "unidade_penal")
public class UnidadePenal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    
    @NotBlank(message = "O nome da unidade penal é obrigatório")
    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    @Column(name = "nome", length = 255, nullable = false)
    private String nome;
    
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    @Column(name = "descricao", length = 255, nullable = true)
    private String descricao;

    @Size(max = 100, message = "Endereço deve ter no máximo 100 caracteres")
    @Column(name = "endereco", length = 100, nullable = true)
    private String endereco;

    @Column(name = "capacidade")
    private Integer capacidade;
    
    @OneToMany(mappedBy = "unidadePenal", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Custodiado> custodiados = new ArrayList<>();

    /**
     * Adiciona um custodiado à unidade penal.
     * 
     * @param custodiado Custodiado a ser adicionado
     */
    public void adicionarCustodiado(Custodiado custodiado) {
        if (custodiados.size() < capacidade) {
            custodiados.add(custodiado);
            custodiado.setUnidadePenal(this);
        } else {
            throw new IllegalStateException("Capacidade máxima da unidade penal excedida");
        }
    }

    /**
     * Calcula o número atual de custodiados na unidade.
     * 
     * @return Número de custodiados
     */
    public int getTotalCustodiados() {
        return custodiados.size();
    }

    /**
     * Verifica se a unidade penal está com lotação máxima.
     * 
     * @return true se estiver lotada, false caso contrário
     */
    public boolean estaLotada() {
        return capacidade != null && custodiados.size() >= capacidade;
    }
}