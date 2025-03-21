package gov.df.seape.sistema.visitas.dto;

import gov.df.seape.sistema.visitas.model.Usuario;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponseDTO {
    
    private Long id;
    private String email;
    private Long perfilId;
    private String descricaoPerfil;
    private PessoaResponseDTO pessoa;
    private LocalDateTime ultimoAcesso;
    
    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.email = usuario.getEmail();
        this.perfilId = usuario.getPerfil().getId();
        this.descricaoPerfil = usuario.getPerfil().getDescricao();
        
        PessoaResponseDTO pessoaDTO = new PessoaResponseDTO(
            usuario.getPessoa().getId(),
            usuario.getPessoa().getNome(),
            usuario.getPessoa().getCpf(),
            usuario.getPessoa().getDataNascimento(),
            null
        );
        pessoaDTO.calcularIdade();
        this.pessoa = pessoaDTO;
    }
}