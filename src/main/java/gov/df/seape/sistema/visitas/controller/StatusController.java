package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.StatusRequestDTO;
import gov.df.seape.sistema.visitas.dto.StatusResponseDTO;
import gov.df.seape.sistema.visitas.service.StatusService;
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
 * Controlador REST para gerenciamento de Status de Agendamentos.
 */
@RestController
@RequestMapping("/api/status")
@RequiredArgsConstructor
@Tag(name = "Status", description = "Endpoints para gerenciamento de status de agendamentos")
public class StatusController {

    private final StatusService statusService;

    /**
     * Cria um novo status.
     * 
     * @param requestDTO Dados do status a ser criado
     * @return O status criado com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar status", description = "Cria um novo status de agendamento")
    public ResponseEntity<StatusResponseDTO> criarStatus(@Valid @RequestBody StatusRequestDTO requestDTO) {
        StatusResponseDTO responseDTO = statusService.criarStatus(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Atualiza um status existente.
     * 
     * @param id ID do status a ser atualizado
     * @param requestDTO Novos dados do status
     * @return O status atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar status", description = "Atualiza um status existente")
    public ResponseEntity<StatusResponseDTO> atualizarStatus(
            @PathVariable Long id, 
            @Valid @RequestBody StatusRequestDTO requestDTO) {
        StatusResponseDTO responseDTO = statusService.atualizarStatus(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca um status pelo seu ID.
     * 
     * @param id ID do status a ser buscado
     * @return O status encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar status por ID", description = "Retorna um status específico pelo ID")
    public ResponseEntity<StatusResponseDTO> buscarStatusPorId(@PathVariable Long id) {
        return statusService.buscarStatusPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os status.
     * 
     * @return Lista de todos os status
     */
    @GetMapping
    @Operation(summary = "Listar status", description = "Lista todos os status de agendamento")
    public ResponseEntity<List<StatusResponseDTO>> listarTodosStatus() {
        List<StatusResponseDTO> statusList = statusService.listarTodosStatus();
        return ResponseEntity.ok(statusList);
    }

    /**
     * Lista status com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de status
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar status paginados", description = "Lista todos os status com paginação")
    public ResponseEntity<PageResponseDTO<StatusResponseDTO>> listarStatusPaginado(Pageable pageable) {
        PageResponseDTO<StatusResponseDTO> pageResponseDTO = statusService.listarStatusPaginado(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca status por descrição.
     * 
     * @param descricao Termo de busca na descrição do status
     * @return Lista de status que contêm a descrição especificada
     */
    @GetMapping("/busca")
    @Operation(summary = "Buscar por descrição", description = "Busca status por descrição")
    public ResponseEntity<List<StatusResponseDTO>> buscarStatusPorDescricao(
            @RequestParam String descricao) {
        List<StatusResponseDTO> statusList = statusService.buscarStatusPorDescricao(descricao);
        return ResponseEntity.ok(statusList);
    }

    /**
     * Exclui um status pelo seu ID.
     * 
     * @param id ID do status a ser excluído
     * @return Status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir status", description = "Exclui um status existente")
    public ResponseEntity<Void> excluirStatus(@PathVariable Long id) {
        statusService.excluirStatus(id);
        return ResponseEntity.noContent().build();
    }
}