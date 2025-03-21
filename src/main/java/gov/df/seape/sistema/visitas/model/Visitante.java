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
     */
    public void atualizarSenhaOnline(String novaSenha) {
        if (novaSenha != null && novaSenha.length() <= 45) {
            this.senhaOnline = novaSenha;
        } else {
            throw new IllegalArgumentException("Senha online inválida");
        }
    }
}