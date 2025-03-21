package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa os usuários do sistema de visitas prisionais.
 * Gerencia credenciais de acesso e informações de autenticação.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"senha", "perfil", "pessoa"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    @Size(max = 255, message = "Email deve ter no máximo 255 caracteres")
    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;
    
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter no mínimo 8 caracteres")
    @Column(name = "senha", length = 255, nullable = false)
    private String senha;
    
    @NotNull(message = "O perfil é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_perfil", nullable = false)
    private Perfil perfil;
    
    @NotNull(message = "A pessoa é obrigatória")
    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    /**
     * Data do último acesso do usuário.
     * Permite rastrear atividade e sessões.
     */
    @Column(name = "ultimo_acesso")
    private LocalDateTime ultimoAcesso;

    /**
     * Data da última alteração de senha.
     * Útil para políticas de segurança.
     */
    @Column(name = "ultima_alteracao_senha")
    private LocalDateTime ultimaAlteracaoSenha;

    /**
     * Indica se o usuário está ativo no sistema.
     */
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    /**
     * Registra o último login do usuário.
     * 
     * @param dataLogin Data e hora do login
     */
    public void registrarLogin(LocalDateTime dataLogin) {
        this.ultimoAcesso = dataLogin;
    }

    /**
     * Atualiza a senha do usuário.
     * 
     * @param novaSenha Nova senha criptografada
     */
    public void atualizarSenha(String novaSenha) {
        this.senha = novaSenha;
        this.ultimaAlteracaoSenha = LocalDateTime.now();
    }

    /**
     * Verifica se o usuário está inativo por muito tempo.
     * 
     * @param tempoMaximoInatividade Tempo máximo de inatividade em dias
     * @return true se estiver inativo, false caso contrário
     */
    public boolean estaInativo(long tempoMaximoInatividade) {
        return this.ultimoAcesso == null || 
               LocalDateTime.now().minusDays(tempoMaximoInatividade)
               .isAfter(this.ultimoAcesso);
    }
}
