package gov.df.seape.sistema.visitas.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade que representa funcionalidades do sistema de visitas prisionais.
 * Define as diferentes ações e recursos disponíveis.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"vinculos", "perfis"})
@NoArgsConstructor
@Entity
public class Funcionalidade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    
    @NotBlank(message = "A descrição da funcionalidade é obrigatória")
    @Column(name = "descricao", length = 45, nullable = false)
    private String descricao;
    
    @NotBlank(message = "O identificador de autoridade é obrigatório")
    @Column(name = "authority", length = 200, nullable = false, unique = true)
    private String authority;

    @OneToMany(mappedBy = "funcionalidade", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<VincPerfilFuncionalidade> vinculos = new HashSet<>();
    
    @ManyToMany(mappedBy = "funcionalidades", fetch = FetchType.LAZY)
    private Set<Perfil> perfis = new HashSet<>();

    /**
     * Construtor para criação de funcionalidade.
     * 
     * @param descricao Descrição da funcionalidade
     * @param authority Identificador de autoridade
     */
    public Funcionalidade(String descricao, String authority) {
        this.descricao = descricao;
        this.authority = authority;
    }

    /**
     * Verifica se a funcionalidade está associada a um perfil.
     * 
     * @param perfil Perfil a ser verificado
     * @return true se estiver associada, false caso contrário
     */
    public boolean estaSendoUsadaPor(Perfil perfil) {
        return perfis.contains(perfil);
    }
}
