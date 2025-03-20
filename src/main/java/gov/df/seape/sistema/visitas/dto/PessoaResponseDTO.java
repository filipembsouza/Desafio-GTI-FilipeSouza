package gov.df.seape.sistema.visitas.dto;

import java.time.LocalDate;

/**
 * DTO para transferência de dados de pessoa na resposta da API.
 * Expõe informações básicas da pessoa, preservando segurança e privacidade.
 */
public class PessoaResponseDTO {
    /**
     * Identificador único da pessoa.
     */
    private Long id;

    /**
     * Nome completo da pessoa.
     */
    private String nome;

    /**
     * CPF da pessoa (pode ser parcialmente mascarado).
     */
    private String cpf;

    /**
     * Data de nascimento da pessoa.
     */
    private LocalDate dataNascimento;

    // Construtores
    public PessoaResponseDTO() {}

    public PessoaResponseDTO(Long id, String nome, String cpf, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}