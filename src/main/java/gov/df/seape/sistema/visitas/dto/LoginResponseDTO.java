package gov.df.seape.sistema.visitas.dto;

/**
 * DTO para resposta de autenticação.
 * Contém informações do token de acesso e usuário autenticado.
 */
public class LoginResponseDTO {
    /**
     * Token de acesso JWT.
     */
    private String token;

    /**
     * Informações básicas do usuário autenticado.
     */
    private UsuarioResponseDTO usuario;

    /**
     * Tipo de token (geralmente "Bearer").
     */
    private String tipoToken;

    // Construtores
    public LoginResponseDTO() {}

    public LoginResponseDTO(String token, UsuarioResponseDTO usuario, String tipoToken) {
        this.token = token;
        this.usuario = usuario;
        this.tipoToken = tipoToken;
    }

    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UsuarioResponseDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioResponseDTO usuario) {
        this.usuario = usuario;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(String tipoToken) {
        this.tipoToken = tipoToken;
    }
}