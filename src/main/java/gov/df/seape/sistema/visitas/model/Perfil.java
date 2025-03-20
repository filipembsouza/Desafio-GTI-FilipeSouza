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
 * Entidade que representa a tabela PERFIL no banco de dados.
 * Esta entidade é fundamental para o controle de acesso ao sistema,
 * definindo os diferentes tipos de perfis de usuário que existem na aplicação.
 * 
 * Cada perfil pode ter diferentes permissões e funcionalidades associadas a ele,
 * determinando o que cada tipo de usuário pode fazer no sistema.
 */
@Getter // Gera automaticamente os métodos getter para os atributos
@Setter // Gera automaticamente os métodos setter para os atributos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usa apenas o ID para comparação
@ToString(exclude = {"vinculos", "funcionalidades"}) // Evita loop infinito ao imprimir a entidade
@NoArgsConstructor
@Entity
public class Perfil {
    
    /**
     * Identificador único do perfil.
     * Note que este campo tem um nome específico "id_perfil" conforme
     * definido no diagrama, diferente do padrão "id" das outras entidades.
     * Essa especificidade é importante para manter a compatibilidade exata
     * com a estrutura do banco de dados especificada.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Marca explicitamente o ID para equals e hashCode
    @Column(name = "id_perfil")  // Nome específico conforme o diagrama
    private Long id;
    
    /**
     * Descrição do perfil, representando seu nome ou título.
     * Este campo é obrigatório e define o tipo de perfil em linguagem natural.
     * 
     * Exemplos comuns incluem:
     * - "Administrador": Acesso total ao sistema
     * - "Policial Penal": Acesso às funcionalidades de controle de visitas
     * - "Diretor": Acesso a relatórios e aprovações
     */
    @NotBlank(message = "A descrição do perfil é obrigatória")
    @Column(name = "descricao", length = 100, nullable = false)
    private String descricao;

    /**
     * Lista de vínculos entre Perfil e Funcionalidade.
     * Essa relação representa as permissões associadas a cada perfil no sistema.
     */
    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VincPerfilFuncionalidade> vinculos = new ArrayList<>();
    
    /**
     * Relação direta com Funcionalidade.
     * Esta é uma forma alternativa de acessar as funcionalidades associadas a este perfil,
     * mais conveniente para uso na lógica de autorização.
     */
    @ManyToMany
    @JoinTable(
        name = "vinc_perfil_funcionalidade",
        joinColumns = @JoinColumn(name = "id_perfil"),
        inverseJoinColumns = @JoinColumn(name = "funcionalidade_id")
    )
    private List<Funcionalidade> funcionalidades = new ArrayList<>();

    /**
     * Construtor personalizado para facilitar a criação de instâncias.
     */
    public Perfil(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Verifica se este perfil possui uma determinada funcionalidade.
     * 
     * @param authority Identificador técnico da funcionalidade (authority)
     * @return true se o perfil possuir a funcionalidade, false caso contrário
     */
    public boolean possuiFuncionalidade(String authority) {
        return funcionalidades.stream()
                .anyMatch(f -> authority.equals(f.getAuthority()));
    }
    
    /**
     * Adiciona uma nova funcionalidade a este perfil.
     * 
     * @param funcionalidade A funcionalidade a ser adicionada
     */
    public void adicionarFuncionalidade(Funcionalidade funcionalidade) {
        if (!possuiFuncionalidade(funcionalidade.getAuthority())) {
            this.funcionalidades.add(funcionalidade);
        }
    }
}