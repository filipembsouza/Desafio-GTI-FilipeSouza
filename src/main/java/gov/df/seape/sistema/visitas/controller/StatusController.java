package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.StatusRequestDTO;
import gov.df.seape.sistema.visitas.dto.StatusResponseDTO;
import gov.df.seape.sistema.visitas.service.StatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciamento de Status no sistema de visitas prisionais.
 * Fornece endpoints para operações de criação, atualização, consulta e exclusão de status.
 */
@RestController
@RequestMapping("/api/status")
@Tag(name = "Status", description = "Endpoints para gerenciamento de status de agendamentos")
public class StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    /**
     * Cria um novo status no sistema.
     * 
     * @param statusRequestDTO Dados do novo status
     * @return Resposta contendo o status criado e código HTTP de criação
     */
    @PostMapping
    @Operation(summary = "Criar novo status", description = "Endpoint para criar um novo status de agendamento")
    public ResponseEntity<StatusResponseDTO> criarStatus(
            @Valid @RequestBody StatusRequestDTO statusRequestDTO) {
        StatusResponseDTO novoStatus = statusService.criarStatus(statusRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoStatus);
    }

    /**
     * Atualiza um status existente.
     * 
     * @param id Identificador do status a ser atualizado
     * @param statusRequestDTO Novos dados do status
     * @return Resposta contendo o status atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar status", description = "Endpoint para atualizar um status existente")
    public ResponseEntity<StatusResponseDTO> atualizarStatus(
            @PathVariable Long id, 
            @Valid @RequestBody StatusRequestDTO statusRequestDTO) {
        StatusResponseDTO statusAtualizado = statusService.atualizarStatus(id, statusRequestDTO);
        return ResponseEntity.ok(statusAtualizado);
    }

    /**
     * Busca um status pelo seu identificador.
     * 
     * @param id Identificador do status
     * @return Resposta contendo o status encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar status por ID", description = "Endpoint para recuperar um status específico")
    public ResponseEntity<StatusResponseDTO> buscarStatusPorId(@PathVariable Long id) {
        return statusService.buscarStatusPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lista todos os status cadastrados.
     * 
     * @return Lista de todos os status
     */
    @GetMapping
    @Operation(summary = "Listar todos os status", description = "Endpoint para recuperar todos os status")
    public ResponseEntity<List<StatusResponseDTO>> listarTodosStatus() {
        List<StatusResponseDTO> statusList = statusService.listarTodosStatus();
        return ResponseEntity.ok(statusList);
    }

    /**
     * Lista status paginados.
     * 
     * @param pageable Informações de paginação
     * @return Página de status
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar status paginados", description = "Endpoint para recuperar status com paginação")
    public ResponseEntity<Page<StatusResponseDTO>> listarStatusPaginado(Pageable pageable) {
        Page<StatusResponseDTO> statusPage = statusService.listarStatusPaginado(pageable);
        return ResponseEntity.ok(statusPage);
    }

    /**
     * Busca status por descrição.
     * 
     * @param descricao Termo de busca na descrição do status
     * @return Lista de status que correspondem à descrição
     */
    @GetMapping("/busca")
    @Operation(summary = "Buscar status por descrição", description = "Endpoint para buscar status contendo um termo")
    public ResponseEntity<List<StatusResponseDTO>> buscarStatusPorDescricao(
            @RequestParam String descricao) {
        List<StatusResponseDTO> statusList = statusService.buscarStatusPorDescricao(descricao);
        return ResponseEntity.ok(statusList);
    }

    /**
     * Exclui um status pelo seu identificador.
     * 
     * @param id Identificador do status a ser excluído
     * @return Resposta indicando sucesso na exclusão
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir status", description = "Endpoint para remover um status")
    public ResponseEntity<Void> excluirStatus(@PathVariable Long id) {
        statusService.excluirStatus(id);
        return ResponseEntity.noContent().build();
    }
}