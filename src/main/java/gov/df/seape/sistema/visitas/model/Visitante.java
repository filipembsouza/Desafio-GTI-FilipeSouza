package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * Entidade que representa a tabela VISITANTE no banco de dados.
 * Armazena informações específicas de visitantes que realizam visitas aos custodiados.
 * Esta entidade possui uma relação com a entidade Pessoa.
 */
@Getter // Gera automaticamente os métodos getter para os atributos
@Setter // Gera automaticamente os métodos setter para os atributos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas atributos importantes sejam usados no hashCode e equals
@ToString(exclude = "pessoa") // Evita loop infinito ao imprimir a entidade
@NoArgsConstructor
@Entity
public class Visitante {
    
    /**
     * Identificador único do visitante no sistema.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // Usa apenas o ID para comparar objetos
    @Column(name = "id")
    private Long id;
    
    /**
     * Senha online utilizada pelo visitante para acessar o sistema.
     * Limitado a 45 caracteres e pode ser nulo conforme o diagrama.
     * Nota: Esta não é a senha principal de autenticação, que seria gerenciada
     * pelo sistema de segurança, mas sim um código de acesso específico.
     */
    @Column(name = "senha_online", length = 45, nullable = true)
    private String senhaOnline;
    
    /**
     * Relação com a entidade Pessoa.
     * Cada visitante está associado a uma única Pessoa que contém seus dados básicos.
     * Esta é uma relação obrigatória.
     */
    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    @NotNull(message = "A pessoa é obrigatória")
    private Pessoa pessoa;

    // Construtor personalizado para facilitar a criação de instâncias
    public Visitante(String senhaOnline, Pessoa pessoa) {
        this.senhaOnline = senhaOnline;
        this.pessoa = pessoa;
    }
}