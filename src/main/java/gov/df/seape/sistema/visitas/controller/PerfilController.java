package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.FuncionalidadeResponseDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.PerfilRequestDTO;
import gov.df.seape.sistema.visitas.dto.PerfilResponseDTO;
import gov.df.seape.sistema.visitas.service.PerfilService;
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
 * Controlador REST para operações relacionadas a Perfis de usuário.
 */
@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/perfis")
@RequiredArgsConstructor
@Tag(name = "Perfis", description = "Endpoints para gerenciamento de perfis de usuário")
public class PerfilController {

    private final PerfilService perfilService;

    /**
     * Cria um novo perfil.
     * 
     * @param requestDTO Dados do perfil a ser criado
     * @return O perfil criado com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar perfil", description = "Cria um novo perfil de usuário")
    public ResponseEntity<PerfilResponseDTO> criarPerfil(@Valid @RequestBody PerfilRequestDTO requestDTO) {
        PerfilResponseDTO responseDTO = perfilService.criarPerfil(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Atualiza um perfil existente.
     * 
     * @param id ID do perfil a ser atualizado
     * @param requestDTO Novos dados do perfil
     * @return O perfil atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar perfil", description = "Atualiza um perfil existente")
    public ResponseEntity<PerfilResponseDTO> atualizarPerfil(
            @PathVariable Long id, 
            @Valid @RequestBody PerfilRequestDTO requestDTO) {
        PerfilResponseDTO responseDTO = perfilService.atualizarPerfil(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os perfis com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de perfis
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar perfis paginados", description = "Lista todos os perfis com paginação")
    public ResponseEntity<PageResponseDTO<PerfilResponseDTO>> listarPerfisPaginados(Pageable pageable) {
        PageResponseDTO<PerfilResponseDTO> pageResponseDTO = perfilService.listarPerfisPaginados(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Lista todos os perfis.
     * 
     * @return Lista de todos os perfis
     */
    @GetMapping
    @Operation(summary = "Listar perfis", description = "Lista todos os perfis")
    public ResponseEntity<List<PerfilResponseDTO>> listarPerfis() {
        List<PerfilResponseDTO> perfis = perfilService.listarPerfis();
        return ResponseEntity.ok(perfis);
    }

    /**
     * Busca um perfil pelo seu ID.
     * 
     * @param id ID do perfil a ser buscado
     * @return O perfil encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar perfil por ID", description = "Retorna um perfil específico pelo ID")
    public ResponseEntity<PerfilResponseDTO> buscarPerfilPorId(@PathVariable Long id) {
        PerfilResponseDTO responseDTO = perfilService.buscarPerfilPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca perfis pela descrição.
     * 
     * @param descricao Descrição ou parte da descrição
     * @param pageable Configurações de paginação
     * @return Página de perfis que contêm a descrição especificada
     */
    @GetMapping("/descricao")
    @Operation(summary = "Buscar por descrição", description = "Busca perfis pela descrição")
    public ResponseEntity<PageResponseDTO<PerfilResponseDTO>> buscarPorDescricao(
            @RequestParam String descricao, 
            Pageable pageable) {
        PageResponseDTO<PerfilResponseDTO> pageResponseDTO = perfilService.buscarPorDescricao(descricao, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Adiciona funcionalidades a um perfil.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeIds Lista de IDs de funcionalidades a serem adicionadas
     * @return O perfil atualizado com as novas funcionalidades
     */
    @PostMapping("/{perfilId}/funcionalidades")
    @Operation(summary = "Adicionar funcionalidades", description = "Adiciona funcionalidades a um perfil")
    public ResponseEntity<PerfilResponseDTO> adicionarFuncionalidades(
            @PathVariable Long perfilId,
            @RequestBody List<Long> funcionalidadeIds) {
        PerfilResponseDTO responseDTO = perfilService.adicionarFuncionalidades(perfilId, funcionalidadeIds);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Remove funcionalidades de um perfil.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeIds Lista de IDs de funcionalidades a serem removidas
     * @return O perfil atualizado
     */
    @DeleteMapping("/{perfilId}/funcionalidades")
    @Operation(summary = "Remover funcionalidades", description = "Remove funcionalidades de um perfil")
    public ResponseEntity<PerfilResponseDTO> removerFuncionalidades(
            @PathVariable Long perfilId,
            @RequestBody List<Long> funcionalidadeIds) {
        PerfilResponseDTO responseDTO = perfilService.removerFuncionalidades(perfilId, funcionalidadeIds);
        return ResponseEntity.ok(responseDTO);
    }
}