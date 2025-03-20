package gov.df.seape.sistema.visitas.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade que representa a tabela FUNCIONALIDADE no banco de dados.
 * Esta entidade define as diferentes ações e recursos que podem ser 
 * controlados pelo sistema de segurança da aplicação.
 * 
 * Cada funcionalidade representa uma capacidade específica dentro do
 * sistema que pode ser concedida ou negada a diferentes perfis de usuário.
 */
@Getter // Gera automaticamente os métodos getter para os atributos
@Setter // Gera automaticamente os métodos setter para os atributos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usa apenas o ID para comparação
@ToString(exclude = {"vinculos", "perfis"}) // Evita loop infinito ao imprimir a entidade
@NoArgsConstructor // Construtor sem argumentos obrigatório para JPA
@Entity
public class Funcionalidade {
    
    /**
     * Identificador único da funcionalidade.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Marca explicitamente o ID para equals e hashCode
    @Column(name = "id")
    private Long id;
    
    /**
     * Descrição da funcionalidade em linguagem natural.
     * Este campo é obrigatório e define o nome ou título da funcionalidade
     * de uma forma compreensível para os usuários do sistema.
     * 
     * Exemplos de funcionalidades em um sistema de visitas prisionais:
     * - "Cadastrar Visitante"
     * - "Agendar Visita"
     * - "Cancelar Visita"
     * - "Gerenciar Usuários"
     * - "Gerar Relatórios"
     */
    @NotBlank(message = "A descrição da funcionalidade é obrigatória")
    @Column(name = "descricao", length = 45, nullable = false)
    private String descricao;
    
    /**
     * Identificador técnico da permissão/autoridade no framework de segurança.
     * Este campo armazena o nome da permissão no formato usado pelo
     * Spring Security, facilitando a integração entre o modelo de dados
     * e o sistema de controle de acesso.
     * 
     * Exemplos de valores usados pelo Spring Security:
     * - "ROLE_ADMIN"
     * - "ROLE_AGENT"
     * - "PERMISSION_CREATE_VISIT"
     * - "PERMISSION_CANCEL_VISIT"
     */
    @NotBlank(message = "O identificador de autoridade é obrigatório")
    @Column(name = "authority", length = 200, nullable = false, unique = true)
    private String authority;

    /**
     * Lista de vínculos entre Funcionalidade e Perfil.
     * Representa quais perfis têm permissão para acessar essa funcionalidade.
     */
    @OneToMany(mappedBy = "funcionalidade", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VincPerfilFuncionalidade> vinculos = new ArrayList<>();
    
    /**
     * Relação direta com Perfil.
     * Facilita o acesso aos perfis que possuem esta funcionalidade.
     */
    @ManyToMany(mappedBy = "funcionalidades")
    private List<Perfil> perfis = new ArrayList<>();

    /**
     * Construtor personalizado para facilitar a criação de instâncias.
     */
    public Funcionalidade(String descricao, String authority) {
        this.descricao = descricao;
        this.authority = authority;
    }
}