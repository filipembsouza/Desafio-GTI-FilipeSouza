package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaRequestDTO;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaResponseDTO;
import gov.df.seape.sistema.visitas.dto.FiltroAgendamentoDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.service.AgendamentoVisitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para operações relacionadas a Agendamentos de Visita.
 */
@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
@Tag(name = "Agendamentos", description = "Endpoints para gerenciamento de agendamentos de visitas")
public class AgendamentoVisitaController {

    private final AgendamentoVisitaService agendamentoVisitaService;

    /**
     * Cria um novo agendamento de visita.
     * 
     * @param requestDTO Dados do agendamento a ser criado
     * @return O agendamento criado com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar agendamento", description = "Cria um novo agendamento de visita")
    public ResponseEntity<AgendamentoVisitaResponseDTO> criarAgendamento(@Valid @RequestBody AgendamentoVisitaRequestDTO requestDTO) {
        AgendamentoVisitaResponseDTO responseDTO = agendamentoVisitaService.criarAgendamento(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Atualiza um agendamento existente.
     * 
     * @param id ID do agendamento a ser atualizado
     * @param requestDTO Novos dados do agendamento
     * @return O agendamento atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar agendamento", description = "Atualiza um agendamento existente")
    public ResponseEntity<AgendamentoVisitaResponseDTO> atualizarAgendamento(
            @PathVariable Long id, 
            @Valid @RequestBody AgendamentoVisitaRequestDTO requestDTO) {
        AgendamentoVisitaResponseDTO responseDTO = agendamentoVisitaService.atualizarAgendamento(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os agendamentos com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de agendamentos
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar agendamentos paginados", description = "Lista todos os agendamentos com paginação")
    public ResponseEntity<PageResponseDTO<AgendamentoVisitaResponseDTO>> listarAgendamentosPaginados(Pageable pageable) {
        PageResponseDTO<AgendamentoVisitaResponseDTO> pageResponseDTO = agendamentoVisitaService.listarAgendamentosPaginados(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Lista todos os agendamentos.
     * 
     * @return Lista de todos os agendamentos
     */
    @GetMapping
    @Operation(summary = "Listar agendamentos", description = "Lista todos os agendamentos")
    public ResponseEntity<List<AgendamentoVisitaResponseDTO>> listarAgendamentos() {
        List<AgendamentoVisitaResponseDTO> agendamentos = agendamentoVisitaService.listarAgendamentos();
        return ResponseEntity.ok(agendamentos);
    }

    /**
     * Busca um agendamento pelo seu ID.
     * 
     * @param id ID do agendamento a ser buscado
     * @return O agendamento encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID", description = "Retorna um agendamento específico pelo ID")
    public ResponseEntity<AgendamentoVisitaResponseDTO> buscarAgendamentoPorId(@PathVariable Long id) {
        AgendamentoVisitaResponseDTO responseDTO = agendamentoVisitaService.buscarAgendamentoPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Filtra agendamentos com base em critérios específicos.
     * 
     * @param filtro Objeto contendo critérios de filtro
     * @param pageable Configurações de paginação
     * @return Página de agendamentos que atendem aos critérios
     */
    @PostMapping("/filtro")
    @Operation(summary = "Filtrar agendamentos", description = "Filtra agendamentos com base em critérios específicos")
    public ResponseEntity<PageResponseDTO<AgendamentoVisitaResponseDTO>> filtrarAgendamentos(
            @Valid @RequestBody FiltroAgendamentoDTO filtro, 
            Pageable pageable) {
        PageResponseDTO<AgendamentoVisitaResponseDTO> pageResponseDTO = agendamentoVisitaService.filtrarAgendamentos(filtro, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca agendamentos por data.
     * 
     * @param data Data para filtrar agendamentos
     * @param pageable Configurações de paginação
     * @return Página de agendamentos na data especificada
     */
    @GetMapping("/data/{data}")
    @Operation(summary = "Buscar por data", description = "Busca agendamentos por data")
    public ResponseEntity<PageResponseDTO<AgendamentoVisitaResponseDTO>> buscarAgendamentosPorData(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data, 
            Pageable pageable) {
        PageResponseDTO<AgendamentoVisitaResponseDTO> pageResponseDTO = agendamentoVisitaService.buscarAgendamentosPorData(data, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca agendamentos de um custodiado.
     * 
     * @param custodiadoId ID do custodiado
     * @param pageable Configurações de paginação
     * @return Página de agendamentos do custodiado
     */
    @GetMapping("/custodiado/{custodiadoId}")
    @Operation(summary = "Buscar por custodiado", description = "Busca agendamentos de um custodiado")
    public ResponseEntity<PageResponseDTO<AgendamentoVisitaResponseDTO>> buscarAgendamentosPorCustodiado(
            @PathVariable Long custodiadoId, 
            Pageable pageable) {
        PageResponseDTO<AgendamentoVisitaResponseDTO> pageResponseDTO = agendamentoVisitaService.buscarAgendamentosPorCustodiado(custodiadoId, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca agendamentos de um visitante.
     * 
     * @param visitanteId ID do visitante
     * @param pageable Configurações de paginação
     * @return Página de agendamentos do visitante
     */
    @GetMapping("/visitante/{visitanteId}")
    @Operation(summary = "Buscar por visitante", description = "Busca agendamentos de um visitante")
    public ResponseEntity<PageResponseDTO<AgendamentoVisitaResponseDTO>> buscarAgendamentosPorVisitante(
            @PathVariable Long visitanteId, 
            Pageable pageable) {
        PageResponseDTO<AgendamentoVisitaResponseDTO> pageResponseDTO = agendamentoVisitaService.buscarAgendamentosPorVisitante(visitanteId, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Cancela um agendamento existente.
     * 
     * @param id ID do agendamento a ser cancelado
     * @return Status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancelar agendamento", description = "Cancela um agendamento existente")
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable Long id) {
        agendamentoVisitaService.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}