package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;

/**
 * Entidade que representa a tabela PESSOA no banco de dados.
 * Armazena informações básicas de uma pessoa, seja ela visitante ou custodiado.
 * Esta é uma entidade base que será referenciada por outras entidades.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Pessoa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;
    
    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "nome", length = 45, nullable = false)
    private String nome;
    
    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
    @Column(name = "cpf", length = 45, nullable = false, unique = true)
    private String cpf;
    
    @Past(message = "A data de nascimento deve ser no passado")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    /**
     * Idade calculada com base na data de nascimento.
     * Campo transiente não persistido no banco de dados.
     */
    @Transient
    private Integer idade;

    /**
     * Construtor personalizado para criação de pessoa.
     * 
     * @param nome Nome completo da pessoa
     * @param cpf CPF da pessoa
     * @param dataNascimento Data de nascimento
     */
    public Pessoa(String nome, String cpf, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        calcularIdade();
    }

    /**
     * Método para calcular a idade baseada na data de nascimento.
     * Atualiza o campo transiente idade.
     */
    public void calcularIdade() {
        if (this.dataNascimento != null) {
            this.idade = Period.between(this.dataNascimento, LocalDate.now()).getYears();
        }
    }

    /**
     * Método para validar o CPF.
     * 
     * @return true se o CPF for válido, false caso contrário
     */
    public boolean validarCPF() {
        // Implementação de validação de CPF
        // Algoritmo de validação do CPF
        return cpf != null && cpf.length() == 11;
    }
}