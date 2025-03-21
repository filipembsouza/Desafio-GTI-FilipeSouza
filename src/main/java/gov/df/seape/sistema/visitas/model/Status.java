package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entidade que representa a tabela STATUS no banco de dados.
 * Armazena os possíveis estados que um agendamento de visita pode ter.
 * Esta entidade é fundamental para controlar o fluxo de processamento das visitas
 * desde seu agendamento até sua realização ou cancelamento.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@Entity
public class Status {
    
    /**
     * Identificador único do status.
     * Gerado automaticamente pelo banco de dados.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    
    /**
     * Descrição textual do status.
     * Este campo é obrigatório e representa o nome do status em linguagem natural.
     * Exemplos típicos incluem: "AGENDADO", "REALIZADO", "CANCELADO", "EM_ANÁLISE", etc.
     * O formato é padronizado para usar apenas letras maiúsculas, números e underscores.
     */
    @NotBlank(message = "A descrição é obrigatória")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "A descrição deve conter apenas letras maiúsculas, números e underscores")
    @Column(name = "descricao", length = 45, nullable = false, unique = true)
    private String descricao;

    /**
     * Construtor personalizado para facilitar a criação de STATUS.
     */
    public Status(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Exemplo de método para verificar se é possível transitar deste status para outro.
     * As regras de transição devem ser implementadas conforme as regras de negócio.
     * 
     * @param novoStatus O novo status para o qual deseja transitar.
     * @return true se a transição for permitida, false caso contrário.
     */
    public boolean podeTransitarPara(Status novoStatus) {
                return novoStatus != null && !this.descricao.equals(novoStatus.getDescricao());
    }
}
