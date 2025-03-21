package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.VisitanteRequestDTO;
import gov.df.seape.sistema.visitas.dto.VisitanteResponseDTO;
import gov.df.seape.sistema.visitas.service.VisitanteService;
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
 * Controlador REST para operações relacionadas a Visitantes.
 */
@RestController
@RequestMapping("/api/visitantes")
@RequiredArgsConstructor
@Tag(name = "Visitantes", description = "Endpoints para gerenciamento de visitantes")
public class VisitanteController {

    private final VisitanteService visitanteService;

    /**
     * Cria um novo visitante.
     * 
     * @param requestDTO Dados do visitante a ser criado
     * @return O visitante criado com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar visitante", description = "Cria um novo visitante")
    public ResponseEntity<VisitanteResponseDTO> criarVisitante(@Valid @RequestBody VisitanteRequestDTO requestDTO) {
        VisitanteResponseDTO responseDTO = visitanteService.criarVisitante(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Atualiza um visitante existente.
     * 
     * @param id ID do visitante a ser atualizado
     * @param requestDTO Novos dados do visitante
     * @return O visitante atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar visitante", description = "Atualiza um visitante existente")
    public ResponseEntity<VisitanteResponseDTO> atualizarVisitante(
            @PathVariable Long id, 
            @Valid @RequestBody VisitanteRequestDTO requestDTO) {
        VisitanteResponseDTO responseDTO = visitanteService.atualizarVisitante(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os visitantes com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de visitantes
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar visitantes paginados", description = "Lista todos os visitantes com paginação")
    public ResponseEntity<PageResponseDTO<VisitanteResponseDTO>> listarVisitantesPaginados(Pageable pageable) {
        PageResponseDTO<VisitanteResponseDTO> pageResponseDTO = visitanteService.listarVisitantesPaginados(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Lista todos os visitantes.
     * 
     * @return Lista de todos os visitantes
     */
    @GetMapping
    @Operation(summary = "Listar visitantes", description = "Lista todos os visitantes")
    public ResponseEntity<List<VisitanteResponseDTO>> listarVisitantes() {
        List<VisitanteResponseDTO> visitantes = visitanteService.listarVisitantes();
        return ResponseEntity.ok(visitantes);
    }

    /**
     * Busca um visitante pelo seu ID.
     * 
     * @param id ID do visitante a ser buscado
     * @return O visitante encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar visitante por ID", description = "Retorna um visitante específico pelo ID")
    public ResponseEntity<VisitanteResponseDTO> buscarVisitantePorId(@PathVariable Long id) {
        VisitanteResponseDTO responseDTO = visitanteService.buscarVisitantePorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca um visitante pelo CPF.
     * 
     * @param cpf CPF do visitante
     * @return O visitante encontrado
     */
    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar por CPF", description = "Busca um visitante pelo CPF")
    public ResponseEntity<VisitanteResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        VisitanteResponseDTO responseDTO = visitanteService.buscarPorCpf(cpf);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca visitantes pelo nome.
     * 
     * @param nome Nome ou parte do nome
     * @param pageable Configurações de paginação
     * @return Página de visitantes que contêm o nome especificado
     */
    @GetMapping("/nome")
    @Operation(summary = "Buscar por nome", description = "Busca visitantes pelo nome")
    public ResponseEntity<PageResponseDTO<VisitanteResponseDTO>> buscarPorNome(
            @RequestParam String nome, 
            Pageable pageable) {
        PageResponseDTO<VisitanteResponseDTO> pageResponseDTO = visitanteService.buscarPorNome(nome, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca visitantes que visitam um determinado custodiado.
     * 
     * @param custodiadoId ID do custodiado
     * @param pageable Configurações de paginação
     * @return Página de visitantes que visitam o custodiado especificado
     */
    @GetMapping("/custodiado/{custodiadoId}")
    @Operation(summary = "Buscar por custodiado", description = "Busca visitantes que visitam um determinado custodiado")
    public ResponseEntity<PageResponseDTO<VisitanteResponseDTO>> buscarVisitantesPorCustodiado(
            @PathVariable Long custodiadoId, 
            Pageable pageable) {
        PageResponseDTO<VisitanteResponseDTO> pageResponseDTO = visitanteService.buscarVisitantesPorCustodiado(custodiadoId, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }
}