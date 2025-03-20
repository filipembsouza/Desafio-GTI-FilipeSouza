package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade que representa a tabela USUARIO no banco de dados.
 * Esta entidade armazena as informações de acesso dos usuários do sistema,
 * como credenciais de login e associações com perfis de acesso.
 * 
 * Os usuários são os atores que interagem com o sistema para gerenciar
 * os agendamentos de visitas e outras funções administrativas. Eles
 * podem ser servidores da unidade prisional como agentes, diretores,
 * ou outros operadores do sistema.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"senha", "perfil", "pessoa"})
@NoArgsConstructor
@Entity
public class Usuario {
    
    /**
     * Identificador único do usuário.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    
    /**
     * Email do usuário, utilizado como nome de usuário (login) para acesso ao sistema.
     * Este campo é obrigatório e deve ser único para cada usuário.
     * É através deste email que o usuário se identificará ao fazer login no sistema.
     */
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;
    
    /**
     * Senha do usuário para autenticação no sistema.
     * Este campo é obrigatório e, por questões de segurança, deve armazenar
     * a senha de forma criptografada, nunca em texto plano.
     * O tamanho de 255 caracteres é adequado para armazenar hashes de senha
     * gerados por algoritmos como BCrypt ou PBKDF2.
     */
    @NotBlank(message = "A senha é obrigatória")
    @Column(name = "senha", length = 255, nullable = false)
    private String senha;
    
    /**
     * Relação com a entidade Perfil.
     * Indica qual perfil de acesso o usuário possui, determinando quais
     * funcionalidades ele pode acessar no sistema.
     * Esta associação é obrigatória, pois todo usuário deve ter um perfil
     * para determinar suas permissões no sistema.
     */
    @NotNull(message = "O perfil é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;
    
    /**
     * Relação com a entidade Pessoa.
     * Associa o usuário a uma pessoa física cadastrada no sistema.
     * Esta associação é obrigatória, pois todo usuário no sistema
     * representa uma pessoa real com seus dados cadastrais.
     */
    @NotNull(message = "A pessoa é obrigatória")
    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;
    
    /**
     * Construtor com todos os campos obrigatórios.
     * Facilita a criação de instâncias completas desta entidade.
     */
    public Usuario(String email, String senha, Perfil perfil, Pessoa pessoa) {
        this.email = email;
        this.senha = senha;
        this.perfil = perfil;
        this.pessoa = pessoa;
    }
}