package gov.df.seape.sistema.visitas.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    
    private String token;
    private String refreshToken;
    private Long usuarioId;
    private String nome;
    private String email;
    private String perfil;
    private List<String> permissoes;
    private Long expiresIn;
}