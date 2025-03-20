package gov.df.seape.sistema.visitas.dto;

import java.util.List;

/**
 * DTO para transferência de dados de perfil na resposta da API.
 * Expõe informações básicas do perfil e suas funcionalidades.
 */
public class PerfilResponseDTO {
    /**
     * Identificador único do perfil.
     */
    private Long id;

    /**
     * Descrição do perfil.
     */
    private String descricao;

    /**
     * Lista de funcionalidades associadas ao perfil.
     */
    private List<FuncionalidadeResponseDTO> funcionalidades;

    // Construtores
    public PerfilResponseDTO() {}

    public PerfilResponseDTO(Long id, String descricao, List<FuncionalidadeResponseDTO> funcionalidades) {
        this.id = id;
        this.descricao = descricao;
        this.funcionalidades = funcionalidades;
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

    public List<FuncionalidadeResponseDTO> getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(List<FuncionalidadeResponseDTO> funcionalidades) {
        this.funcionalidades = funcionalidades;
    }
}