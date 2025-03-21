package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.UsuarioRequestDTO;
import gov.df.seape.sistema.visitas.dto.UsuarioResponseDTO;
import gov.df.seape.sistema.visitas.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operações relacionadas a Usuários do sistema.
 */
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários do sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Cria um novo usuário.
     * 
     * @param requestDTO Dados do usuário a ser criado
     * @return O usuário criado com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário do sistema")
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO responseDTO = usuarioService.criarUsuario(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Atualiza um usuário existente.
     * 
     * @param id ID do usuário a ser atualizado
     * @param requestDTO Novos dados do usuário
     * @return O usuário atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(
            @PathVariable Long id, 
            @Valid @RequestBody UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO responseDTO = usuarioService.atualizarUsuario(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os usuários com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de usuários
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar usuários paginados", description = "Lista todos os usuários com paginação")
    public ResponseEntity<PageResponseDTO<UsuarioResponseDTO>> listarUsuariosPaginados(Pageable pageable) {
        PageResponseDTO<UsuarioResponseDTO> pageResponseDTO = usuarioService.listarUsuariosPaginados(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Lista todos os usuários.
     * 
     * @return Lista de todos os usuários
     */
    @GetMapping
    @Operation(summary = "Listar usuários", description = "Lista todos os usuários")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Busca um usuário pelo seu ID.
     * 
     * @param id ID do usuário a ser buscado
     * @return O usuário encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna um usuário específico pelo ID")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id) {
        UsuarioResponseDTO responseDTO = usuarioService.buscarUsuarioPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca um usuário pelo email.
     * 
     * @param email Email do usuário
     * @return O usuário encontrado
     */
    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar por email", description = "Busca um usuário pelo email")
    public ResponseEntity<UsuarioResponseDTO> buscarPorEmail(@PathVariable String email) {
        UsuarioResponseDTO responseDTO = usuarioService.buscarPorEmail(email);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca usuários pelo nome.
     * 
     * @param nome Nome ou parte do nome
     * @param pageable Configurações de paginação
     * @return Página de usuários que contêm o nome especificado
     */
    @GetMapping("/nome")
    @Operation(summary = "Buscar por nome", description = "Busca usuários pelo nome")
    public ResponseEntity<PageResponseDTO<UsuarioResponseDTO>> buscarPorNome(
            @RequestParam String nome, 
            Pageable pageable) {
        PageResponseDTO<UsuarioResponseDTO> pageResponseDTO = usuarioService.buscarPorNome(nome, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca usuários por perfil.
     * 
     * @param perfilId ID do perfil
     * @param pageable Configurações de paginação
     * @return Página de usuários com o perfil especificado
     */
    @GetMapping("/perfil/{perfilId}")
    @Operation(summary = "Buscar por perfil", description = "Busca usuários por perfil")
    public ResponseEntity<PageResponseDTO<UsuarioResponseDTO>> buscarPorPerfil(
            @PathVariable Long perfilId, 
            Pageable pageable) {
        PageResponseDTO<UsuarioResponseDTO> pageResponseDTO = usuarioService.buscarPorPerfil(perfilId, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Altera a senha de um usuário.
     * 
     * @param id ID do usuário
     * @param senhaAtual Senha atual
     * @param novaSenha Nova senha
     * @return O usuário com a senha atualizada
     */
    @PutMapping("/{id}/alterar-senha")
    @Operation(summary = "Alterar senha", description = "Altera a senha de um usuário")
    public ResponseEntity<UsuarioResponseDTO> alterarSenha(
            @PathVariable Long id,
            @RequestParam String senhaAtual,
            @RequestParam String novaSenha) {
        UsuarioResponseDTO responseDTO = usuarioService.alterarSenha(id, senhaAtual, novaSenha);
        return ResponseEntity.ok(responseDTO);
    }
}