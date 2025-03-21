package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.UnidadePenalRequestDTO;
import gov.df.seape.sistema.visitas.dto.UnidadePenalResponseDTO;
import gov.df.seape.sistema.visitas.service.UnidadePenalService;
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
 * Controlador REST para operações relacionadas a Unidades Penais.
 */
@RestController
@RequestMapping("/api/unidades-penais")
@RequiredArgsConstructor
@Tag(name = "Unidades Penais", description = "Endpoints para gerenciamento de unidades penais")
public class UnidadePenalController {

    private final UnidadePenalService unidadePenalService;

    /**
     * Cria uma nova unidade penal.
     * 
     * @param requestDTO Dados da unidade penal a ser criada
     * @return A unidade penal criada com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar unidade penal", description = "Cria uma nova unidade penal")
    public ResponseEntity<UnidadePenalResponseDTO> criarUnidadePenal(@Valid @RequestBody UnidadePenalRequestDTO requestDTO) {
        UnidadePenalResponseDTO responseDTO = unidadePenalService.criarUnidadePenal(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Atualiza uma unidade penal existente.
     * 
     * @param id ID da unidade penal a ser atualizada
     * @param requestDTO Novos dados da unidade penal
     * @return A unidade penal atualizada
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar unidade penal", description = "Atualiza uma unidade penal existente")
    public ResponseEntity<UnidadePenalResponseDTO> atualizarUnidadePenal(
            @PathVariable Long id, 
            @Valid @RequestBody UnidadePenalRequestDTO requestDTO) {
        UnidadePenalResponseDTO responseDTO = unidadePenalService.atualizarUnidadePenal(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todas as unidades penais com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de unidades penais
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar unidades penais paginadas", description = "Lista todas as unidades penais com paginação")
    public ResponseEntity<PageResponseDTO<UnidadePenalResponseDTO>> listarUnidadesPenaisPaginadas(Pageable pageable) {
        PageResponseDTO<UnidadePenalResponseDTO> pageResponseDTO = unidadePenalService.listarUnidadesPenaisPaginadas(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Lista todas as unidades penais.
     * 
     * @return Lista de todas as unidades penais
     */
    @GetMapping
    @Operation(summary = "Listar unidades penais", description = "Lista todas as unidades penais")
    public ResponseEntity<List<UnidadePenalResponseDTO>> listarUnidadesPenais() {
        List<UnidadePenalResponseDTO> unidadesPenais = unidadePenalService.listarUnidadesPenais();
        return ResponseEntity.ok(unidadesPenais);
    }

    /**
     * Busca uma unidade penal pelo seu ID.
     * 
     * @param id ID da unidade penal a ser buscada
     * @return A unidade penal encontrada
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar unidade penal por ID", description = "Retorna uma unidade penal específica pelo ID")
    public ResponseEntity<UnidadePenalResponseDTO> buscarUnidadePenalPorId(@PathVariable Long id) {
        UnidadePenalResponseDTO responseDTO = unidadePenalService.buscarUnidadePenalPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca unidades penais pelo nome.
     * 
     * @param nome Nome ou parte do nome
     * @param pageable Configurações de paginação
     * @return Página de unidades penais que contêm o nome especificado
     */
    @GetMapping("/nome")
    @Operation(summary = "Buscar por nome", description = "Busca unidades penais pelo nome")
    public ResponseEntity<PageResponseDTO<UnidadePenalResponseDTO>> buscarPorNome(
            @RequestParam String nome, 
            Pageable pageable) {
        PageResponseDTO<UnidadePenalResponseDTO> pageResponseDTO = unidadePenalService.buscarPorNome(nome, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }
}