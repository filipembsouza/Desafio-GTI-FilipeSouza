package gov.df.seape.sistema.visitas.dto;

/**
 * DTO para transferência de dados de unidade penal na resposta da API.
 * Expõe informações básicas da unidade penal.
 */
public class UnidadePenalResponseDTO {
    /**
     * Identificador único da unidade penal.
     */
    private Long id;

    /**
     * Nome da unidade penal.
     */
    private String nome;

    /**
     * Descrição adicional da unidade penal.
     */
    private String descricao;

    // Construtores
    public UnidadePenalResponseDTO() {}

    public UnidadePenalResponseDTO(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}