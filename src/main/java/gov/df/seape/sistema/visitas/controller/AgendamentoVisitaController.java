package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaDTO;
import gov.df.seape.sistema.visitas.dto.FiltroAgendamentoDTO;
import gov.df.seape.sistema.visitas.service.AgendamentoVisitaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para operações relacionadas a Agendamentos de Visita.
 * Expõe endpoints para criação, consulta, atualização e cancelamento de agendamentos.
 */
@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor

public class AgendamentoVisitaController {

    private final AgendamentoVisitaService agendamentoVisitaService;

    /**
     * Cria um novo agendamento de visita.
     * 
     * @param agendamentoDTO Dados do agendamento a ser criado
     * @return O agendamento criado com status 201 (Created)
     */
    @PostMapping
    public ResponseEntity<AgendamentoVisitaDTO> criarAgendamento(@Valid @RequestBody AgendamentoVisitaDTO agendamentoDTO) {
        AgendamentoVisitaDTO agendamentoCriado = agendamentoVisitaService.criarAgendamento(agendamentoDTO);
        return new ResponseEntity<>(agendamentoCriado, HttpStatus.CREATED);
    }

    /**
     * Atualiza um agendamento existente.
     * 
     * @param id ID do agendamento a ser atualizado
     * @param agendamentoDTO Novos dados do agendamento
     * @return O agendamento atualizado
     */

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoVisitaDTO> atualizarAgendamento(
            @PathVariable Long id, 
            @Valid @RequestBody AgendamentoVisitaDTO agendamentoDTO) {
        AgendamentoVisitaDTO agendamentoAtualizado = agendamentoVisitaService.atualizarAgendamento(id, agendamentoDTO);
        return ResponseEntity.ok(agendamentoAtualizado);
    }

    /**
     * Lista todos os agendamentos ou filtra conforme parâmetros.
     * 
     * @param custodiadoId ID do custodiado (opcional)
     * @param visitanteId ID do visitante (opcional)
     * @param dataInicio Data inicial para filtro (opcional)
     * @param dataFim Data final para filtro (opcional)
     * @param statusId ID do status (opcional)
     * @return Lista de agendamentos
     */

    @GetMapping
    public ResponseEntity<List<AgendamentoVisitaDTO>> listarAgendamentos(
            @RequestParam(required = false) Long custodiadoId,
            @RequestParam(required = false) Long visitanteId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            @RequestParam(required = false) Long statusId) {
        
        // Se não houver parâmetros, retorna todos os agendamentos
        if (custodiadoId == null && visitanteId == null && dataInicio == null && 
            dataFim == null && statusId == null) {
            return ResponseEntity.ok(agendamentoVisitaService.listarAgendamentos());
        }
        
        // Caso contrário, aplica os filtros
        FiltroAgendamentoDTO filtro = new FiltroAgendamentoDTO();
        filtro.setCustodiadoId(custodiadoId);
        filtro.setVisitanteId(visitanteId);
        filtro.setDataInicio(dataInicio);
        filtro.setDataFim(dataFim);
        filtro.setStatusId(statusId);
        
        return ResponseEntity.ok(agendamentoVisitaService.filtrarAgendamentos(filtro));
    }

    /**
     * Busca um agendamento pelo seu ID.
     * 
     * @param id ID do agendamento a ser buscado
     * @return O agendamento encontrado
     */

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoVisitaDTO> buscarAgendamentoPorId(@PathVariable Long id) {
        AgendamentoVisitaDTO agendamento = agendamentoVisitaService.buscarAgendamentoPorId(id);
        return ResponseEntity.ok(agendamento);
    }

    /**
     * Cancela um agendamento de visita.
     * 
     * @param id ID do agendamento a ser cancelado
     * @return Status 204 (No Content)
     */
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable Long id) {
        agendamentoVisitaService.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}