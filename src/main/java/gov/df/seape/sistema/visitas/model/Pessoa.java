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
     * Calcula e retorna a idade baseada na data de nascimento.
     * 
     * @return Idade em anos.
     */
    public int getIdade() {
        if (this.dataNascimento != null) {
            return Period.between(this.dataNascimento, LocalDate.now()).getYears();
        }
        return 0;
    }

    /**
     * Método para validar o CPF utilizando o algoritmo padrão.
     * 
     * @return true se o CPF for válido, false caso contrário.
     */
    public boolean validarCPF() {
        if (cpf == null || !cpf.matches("\\d{11}")) {
            return false;
        }
        // Verifica se todos os dígitos são iguais (ex.: "11111111111")
        if (cpf.chars().distinct().count() == 1) {
            return false;
        }
        try {
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int resto = 11 - (soma % 11);
            int digito1 = resto >= 10 ? 0 : resto;

            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            resto = 11 - (soma % 11);
            int digito2 = resto >= 10 ? 0 : resto;

            return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
                   digito2 == Character.getNumericValue(cpf.charAt(10));
        } catch (Exception e) {
            return false;
        }
    }
    
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
    }
}
