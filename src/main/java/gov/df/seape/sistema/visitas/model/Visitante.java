package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidade que representa visitantes no sistema prisional.
 * Armazena informações específicas dos visitantes que realizam visitas aos custodiados.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "pessoa")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Visitante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    
    @Size(max = 45, message = "Senha online deve ter no máximo 45 caracteres")
    @Column(name = "senha_online", length = 45, nullable = true)
    private String senhaOnline;
    
    @Size(max = 45, message = "Grau de parentesco deve ter no máximo 45 caracteres")
    @Column(name = "grau_parentesco", length = 45, nullable = true)
    private String grauParentesco;
    
    @NotNull(message = "A pessoa é obrigatória")
    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    /**
     * Verifica se o visitante possui senha online configurada.
     * 
     * @return true se possui senha online, false caso contrário
     */
    public boolean possuiSenhaOnline() {
        return this.senhaOnline != null && !this.senhaOnline.isEmpty();
    }

    /**
     * Atualiza a senha online do visitante.
     * 
     * @param novaSenha Nova senha online
     * @throws SenhaOnlineInvalidaException se a nova senha for nula, vazia ou exceder 45 caracteres.
     */
    public void atualizarSenhaOnline(String novaSenha) {
        if (novaSenha == null || novaSenha.isEmpty() || novaSenha.length() > 45) {
            throw new SenhaOnlineInvalidaException("Senha online inválida: deve ser não nula, não vazia e ter no máximo 45 caracteres.");
        }
        this.senhaOnline = novaSenha;
    }
    
    /**
     * Exceção customizada para senha online inválida.
     */
    public static class SenhaOnlineInvalidaException extends RuntimeException {
        public SenhaOnlineInvalidaException(String message) {
            super(message);
        }
    }
}