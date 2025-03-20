package gov.df.seape.sistema.visitas.dto;

/**
 * DTO para retorno de dados da entidade Status.
 * Usado para enviar informações de Status nas respostas da API.
 */
public class StatusResponseDTO {
    
    private Long id;
    private String descricao;
    
    // Construtores
    public StatusResponseDTO() {
    }
    
    public StatusResponseDTO(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
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
}