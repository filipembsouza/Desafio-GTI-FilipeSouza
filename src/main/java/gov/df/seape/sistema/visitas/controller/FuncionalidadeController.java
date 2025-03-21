package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.FuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.FuncionalidadeResponseDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.service.FuncionalidadeService;
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
 * Controlador REST para operações relacionadas a Funcionalidades do sistema.
 */
@RestController
@RequestMapping("/api/funcionalidades")
@RequiredArgsConstructor
@Tag(name = "Funcionalidades", description = "Endpoints para gerenciamento de funcionalidades do sistema")
public class FuncionalidadeController {

    private final FuncionalidadeService funcionalidadeService;

    /**
     * Cria uma nova funcionalidade.
     * 
     * @param requestDTO Dados da funcionalidade a ser criada
     * @return A funcionalidade criada com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar funcionalidade", description = "Cria uma nova funcionalidade do sistema")
    public ResponseEntity<FuncionalidadeResponseDTO> criarFuncionalidade(@Valid @RequestBody FuncionalidadeRequestDTO requestDTO) {
        FuncionalidadeResponseDTO responseDTO = funcionalidadeService.criarFuncionalidade(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Atualiza uma funcionalidade existente.
     * 
     * @param id ID da funcionalidade a ser atualizada
     * @param requestDTO Novos dados da funcionalidade
     * @return A funcionalidade atualizada
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar funcionalidade", description = "Atualiza uma funcionalidade existente")
    public ResponseEntity<FuncionalidadeResponseDTO> atualizarFuncionalidade(
            @PathVariable Long id, 
            @Valid @RequestBody FuncionalidadeRequestDTO requestDTO) {
        FuncionalidadeResponseDTO responseDTO = funcionalidadeService.atualizarFuncionalidade(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todas as funcionalidades com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar funcionalidades paginadas", description = "Lista todas as funcionalidades com paginação")
    public ResponseEntity<PageResponseDTO<FuncionalidadeResponseDTO>> listarFuncionalidadesPaginadas(Pageable pageable) {
        PageResponseDTO<FuncionalidadeResponseDTO> pageResponseDTO = funcionalidadeService.listarFuncionalidadesPaginadas(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Lista todas as funcionalidades.
     * 
     * @return Lista de todas as funcionalidades
     */
    @GetMapping
    @Operation(summary = "Listar funcionalidades", description = "Lista todas as funcionalidades")
    public ResponseEntity<List<FuncionalidadeResponseDTO>> listarFuncionalidades() {
        List<FuncionalidadeResponseDTO> funcionalidades = funcionalidadeService.listarFuncionalidades();
        return ResponseEntity.ok(funcionalidades);
    }

    /**
     * Busca uma funcionalidade pelo seu ID.
     * 
     * @param id ID da funcionalidade a ser buscada
     * @return A funcionalidade encontrada
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar funcionalidade por ID", description = "Retorna uma funcionalidade específica pelo ID")
    public ResponseEntity<FuncionalidadeResponseDTO> buscarFuncionalidadePorId(@PathVariable Long id) {
        FuncionalidadeResponseDTO responseDTO = funcionalidadeService.buscarFuncionalidadePorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca funcionalidades pela descrição.
     * 
     * @param descricao Descrição ou parte da descrição
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades que contêm a descrição especificada
     */
    @GetMapping("/descricao")
    @Operation(summary = "Buscar por descrição", description = "Busca funcionalidades pela descrição")
    public ResponseEntity<PageResponseDTO<FuncionalidadeResponseDTO>> buscarPorDescricao(
            @RequestParam String descricao, 
            Pageable pageable) {
        PageResponseDTO<FuncionalidadeResponseDTO> pageResponseDTO = funcionalidadeService.buscarPorDescricao(descricao, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca funcionalidades pela authority.
     * 
     * @param authority Authority ou parte da authority
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades que contêm a authority especificada
     */
    @GetMapping("/authority")
    @Operation(summary = "Buscar por authority", description = "Busca funcionalidades pela authority")
    public ResponseEntity<PageResponseDTO<FuncionalidadeResponseDTO>> buscarPorAuthority(
            @RequestParam String authority, 
            Pageable pageable) {
        PageResponseDTO<FuncionalidadeResponseDTO> pageResponseDTO = funcionalidadeService.buscarPorAuthority(authority, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca funcionalidades associadas a um perfil.
     * 
     * @param perfilId ID do perfil
     * @return Lista de funcionalidades associadas ao perfil
     */
    @GetMapping("/perfil/{perfilId}")
    @Operation(summary = "Buscar por perfil", description = "Busca funcionalidades associadas a um perfil")
    public ResponseEntity<List<FuncionalidadeResponseDTO>> buscarPorPerfil(@PathVariable Long perfilId) {
        List<FuncionalidadeResponseDTO> funcionalidades = funcionalidadeService.buscarPorPerfil(perfilId);
        return ResponseEntity.ok(funcionalidades);
    }

    /**
     * Busca funcionalidades não associadas a um perfil.
     * 
     * @param perfilId ID do perfil
     * @return Lista de funcionalidades não associadas ao perfil
     */
    @GetMapping("/nao-associadas/{perfilId}")
    @Operation(summary = "Buscar não associadas", description = "Busca funcionalidades não associadas a um perfil")
    public ResponseEntity<List<FuncionalidadeResponseDTO>> buscarNaoAssociadasAoPerfil(@PathVariable Long perfilId) {
        List<FuncionalidadeResponseDTO> funcionalidades = funcionalidadeService.buscarNaoAssociadasAoPerfil(perfilId);
        return ResponseEntity.ok(funcionalidades);
    }
}