package gov.df.seape.sistema.visitas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para recebimento de credenciais de login.
 * Contém validações para garantir a integridade dos dados de entrada.
 */
public class LoginRequestDTO {
    /**
     * Email do usuário para login.
     * Deve ser um email válido.
     */
    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Formato de email inválido")
    private String email;

    /**
     * Senha do usuário.
     * Deve ter entre 6 e 255 caracteres.
     */
    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres")
    private String senha;

    // Construtores
    public LoginRequestDTO() {}

    public LoginRequestDTO(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}