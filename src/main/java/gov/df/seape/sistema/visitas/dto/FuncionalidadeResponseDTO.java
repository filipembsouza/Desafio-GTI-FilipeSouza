package gov.df.seape.sistema.visitas.dto;

/**
 * DTO para transferência de dados de funcionalidade na resposta da API.
 * Expõe informações básicas da funcionalidade.
 */
public class FuncionalidadeResponseDTO {
    /**
     * Identificador único da funcionalidade.
     */
    private Long id;

    /**
     * Descrição da funcionalidade.
     */
    private String descricao;

    /**
     * Identificador técnico da autoridade (permissão).
     */
    private String authority;

    // Construtores
    public FuncionalidadeResponseDTO() {}

    public FuncionalidadeResponseDTO(Long id, String descricao, String authority) {
        this.id = id;
        this.descricao = descricao;
        this.authority = authority;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}