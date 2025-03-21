package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeResponseDTO;
import gov.df.seape.sistema.visitas.service.VincPerfilFuncionalidadeService;
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
 * Controlador REST para operações relacionadas a Vínculos entre Perfis e Funcionalidades.
 */
@RestController
@RequestMapping("/api/vinculos-perfil-funcionalidade")
@RequiredArgsConstructor
@Tag(name = "Vínculos Perfil-Funcionalidade", description = "Endpoints para gerenciamento de vínculos entre perfis e funcionalidades")
public class VincPerfilFuncionalidadeController {

    private final VincPerfilFuncionalidadeService vincPerfilFuncionalidadeService;

    /**
     * Cria um novo vínculo entre perfil e funcionalidade.
     * 
     * @param requestDTO Dados do vínculo a ser criado
     * @return O vínculo criado com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar vínculo", description = "Cria um novo vínculo entre perfil e funcionalidade")
    public ResponseEntity<VincPerfilFuncionalidadeResponseDTO> criarVinculo(@Valid @RequestBody VincPerfilFuncionalidadeRequestDTO requestDTO) {
        VincPerfilFuncionalidadeResponseDTO responseDTO = vincPerfilFuncionalidadeService.criarVinculo(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Exclui um vínculo existente.
     * 
     * @param id ID do vínculo a ser excluído
     * @return Status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir vínculo", description = "Exclui um vínculo existente")
    public ResponseEntity<Void> excluirVinculo(@PathVariable Long id) {
        vincPerfilFuncionalidadeService.excluirVinculo(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Lista todos os vínculos com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de vínculos
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar vínculos paginados", description = "Lista todos os vínculos com paginação")
    public ResponseEntity<PageResponseDTO<VincPerfilFuncionalidadeResponseDTO>> listarVinculosPaginados(Pageable pageable) {
        PageResponseDTO<VincPerfilFuncionalidadeResponseDTO> pageResponseDTO = vincPerfilFuncionalidadeService.listarVinculosPaginados(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Lista todos os vínculos.
     * 
     * @return Lista de todos os vínculos
     */
    @GetMapping
    @Operation(summary = "Listar vínculos", description = "Lista todos os vínculos")
    public ResponseEntity<List<VincPerfilFuncionalidadeResponseDTO>> listarVinculos() {
        List<VincPerfilFuncionalidadeResponseDTO> vinculos = vincPerfilFuncionalidadeService.listarVinculos();
        return ResponseEntity.ok(vinculos);
    }

    /**
     * Busca um vínculo pelo seu ID.
     * 
     * @param id ID do vínculo a ser buscado
     * @return O vínculo encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar vínculo por ID", description = "Retorna um vínculo específico pelo ID")
    public ResponseEntity<VincPerfilFuncionalidadeResponseDTO> buscarVinculoPorId(@PathVariable Long id) {
        VincPerfilFuncionalidadeResponseDTO responseDTO = vincPerfilFuncionalidadeService.buscarVinculoPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca vínculos por perfil.
     * 
     * @param perfilId ID do perfil
     * @param pageable Configurações de paginação
     * @return Página de vínculos do perfil
     */
    @GetMapping("/perfil/{perfilId}")
    @Operation(summary = "Buscar por perfil", description = "Busca vínculos por perfil")
    public ResponseEntity<PageResponseDTO<VincPerfilFuncionalidadeResponseDTO>> buscarPorPerfil(
            @PathVariable Long perfilId, 
            Pageable pageable) {
        PageResponseDTO<VincPerfilFuncionalidadeResponseDTO> pageResponseDTO = vincPerfilFuncionalidadeService.buscarPorPerfil(perfilId, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca vínculos por funcionalidade.
     * 
     * @param funcionalidadeId ID da funcionalidade
     * @param pageable Configurações de paginação
     * @return Página de vínculos da funcionalidade
     */
    @GetMapping("/funcionalidade/{funcionalidadeId}")
    @Operation(summary = "Buscar por funcionalidade", description = "Busca vínculos por funcionalidade")
    public ResponseEntity<PageResponseDTO<VincPerfilFuncionalidadeResponseDTO>> buscarPorFuncionalidade(
            @PathVariable Long funcionalidadeId, 
            Pageable pageable) {
        PageResponseDTO<VincPerfilFuncionalidadeResponseDTO> pageResponseDTO = vincPerfilFuncionalidadeService.buscarPorFuncionalidade(funcionalidadeId, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Verifica se existe um vínculo entre um perfil e uma funcionalidade.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeId ID da funcionalidade
     * @return true se o vínculo existir, false caso contrário
     */
    @GetMapping("/verificar")
    @Operation(summary = "Verificar vínculo", description = "Verifica se existe um vínculo entre um perfil e uma funcionalidade")
    public ResponseEntity<Boolean> verificarVinculo(
            @RequestParam Long perfilId, 
            @RequestParam Long funcionalidadeId) {
        boolean vinculoExiste = vincPerfilFuncionalidadeService.verificarVinculo(perfilId, funcionalidadeId);
        return ResponseEntity.ok(vinculoExiste);
    }
}