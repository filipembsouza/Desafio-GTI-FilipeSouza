package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa os perfis de usuário no sistema.
 * Define os diferentes níveis de acesso e permissões.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "perfil")
public class Perfil {
    /**
     * Identificador único do perfil.
     * Gerado automaticamente, seguindo especificação do diagrama.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id_perfil")
    private Long id;

    /**
     * Descrição do perfil.
     * Define o nome ou título do perfil de acesso.
     */
    @NotBlank(message = "Descrição do perfil é obrigatória")
    @Column(name = "descricao", length = 100, nullable = false)
    private String descricao;

    /**
     * Vínculos entre Perfil e Funcionalidade.
     * Representa as permissões associadas ao perfil.
     */
    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VincPerfilFuncionalidade> vinculos = new ArrayList<>();

    /**
     * Relação direta com Funcionalidade para consultas de permissão.
     */
    @ManyToMany
    @JoinTable(
        name = "vinc_perfil_funcionalidade",
        joinColumns = @JoinColumn(name = "id_perfil"),
        inverseJoinColumns = @JoinColumn(name = "funcionalidade_id")
    )
    private List<Funcionalidade> funcionalidades = new ArrayList<>();

    /**
     * Verifica se o perfil possui uma determinada funcionalidade.
     * 
     * @param authority Identificador da funcionalidade
     * @return true se o perfil possuir a funcionalidade, false caso contrário
     */
    public boolean possuiFuncionalidade(String authority) {
        return funcionalidades.stream()
            .anyMatch(f -> authority.equals(f.getAuthority()));
    }

    /**
     * Adiciona uma funcionalidade ao perfil.
     * 
     * @param funcionalidade Funcionalidade a ser adicionada
     */
    public void adicionarFuncionalidade(Funcionalidade funcionalidade) {
        if (!possuiFuncionalidade(funcionalidade.getAuthority())) {
            this.funcionalidades.add(funcionalidade);
        }
    }
}