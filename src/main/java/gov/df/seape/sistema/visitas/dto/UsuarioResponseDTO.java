package gov.df.seape.sistema.visitas.dto;

/**
 * DTO para transferência de dados de usuário na resposta da API.
 * Expõe informações básicas do usuário, excluindo informações sensíveis.
 */
public class UsuarioResponseDTO {
    /**
     * Identificador único do usuário.
     */
    private Long id;

    /**
     * Dados pessoais do usuário.
     */
    private PessoaResponseDTO pessoa;

    /**
     * Email do usuário.
     */
    private String email;

    /**
     * Perfil do usuário.
     */
    private PerfilResponseDTO perfil;

    // Construtores
    public UsuarioResponseDTO() {}

    public UsuarioResponseDTO(Long id, PessoaResponseDTO pessoa, String email, PerfilResponseDTO perfil) {
        this.id = id;
        this.pessoa = pessoa;
        this.email = email;
        this.perfil = perfil;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PessoaResponseDTO getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaResponseDTO pessoa) {
        this.pessoa = pessoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public PerfilResponseDTO getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilResponseDTO perfil) {
        this.perfil = perfil;
    }
}