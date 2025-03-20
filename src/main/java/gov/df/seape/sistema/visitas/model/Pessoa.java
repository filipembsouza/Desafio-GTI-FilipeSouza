package gov.df.seape.sistema.visitas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Entidade que representa a tabela PESSOA no banco de dados.
 * Armazena informações básicas de uma pessoa, seja ela visitante ou custodiado.
 * Esta é uma entidade base que será referenciada por outras entidades.
 */
@Getter // Gera automaticamente os métodos getter para os atributos
@Setter // Gera automaticamente os métodos setter para os atributos
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Usa apenas o ID para comparação
@ToString // Mantém a conversão para string, mas sem causar loops
@NoArgsConstructor  // Cria um construtor sem argumentos (exigido pelo JPA)
@Entity             // Marca esta classe como uma entidade JPA
public class Pessoa {
    
    /**
     * Identificador único da pessoa no sistema.
     * Configurado com geração automática de valor (auto-incremento).
     */
    @Id                                              // Define este campo como chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Estratégia de geração de valor: auto-incremento
    @EqualsAndHashCode.Include // Usa apenas o ID para comparar objetos
    @Column(name = "id")                            // Nome explícito da coluna no banco de dados
    private Long id;
    
    /**
     * Nome completo da pessoa.
     * Limitado a 45 caracteres e obrigatório.
     */
    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "nome", length = 45, nullable = false)
    private String nome;
    
    /**
     * CPF da pessoa.
     * Limitado a 45 caracteres, obrigatório e único no sistema.
     * Deve seguir o formato de 11 dígitos numéricos.
     */
    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
    @Column(name = "cpf", length = 45, nullable = false, unique = true)
    private String cpf;
    
    /**
     * Data de nascimento da pessoa.
     * Campo obrigatório e deve ser uma data no passado.
     */
    @Past(message = "A data de nascimento deve ser no passado")
    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    // Construtor personalizado para facilitar a criação de instâncias
    public Pessoa(String nome, String cpf, LocalDate dataNascimento) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }
}