package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaRequestDTO;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaResponseDTO;
import gov.df.seape.sistema.visitas.dto.FiltroAgendamentoDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Agendamentos de Visita.
 */
public interface AgendamentoVisitaService {
    
    /**
     * Criar um novo agendamento de visita.
     * 
     * @param requestDTO Dados do agendamento a ser criado
     * @return O agendamento criado, com ID gerado
     */
    AgendamentoVisitaResponseDTO criarAgendamento(AgendamentoVisitaRequestDTO requestDTO);
    
    /**
     * Atualizar um agendamento existente.
     * 
     * @param id ID do agendamento a ser atualizado
     * @param requestDTO Novos dados do agendamento
     * @return O agendamento atualizado
     */
    AgendamentoVisitaResponseDTO atualizarAgendamento(Long id, AgendamentoVisitaRequestDTO requestDTO);
    
    /**
     * Buscar todos os agendamentos com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de agendamentos
     */
    PageResponseDTO<AgendamentoVisitaResponseDTO> listarAgendamentosPaginados(Pageable pageable);
    
    /**
     * Buscar todos os agendamentos.
     * 
     * @return Lista de todos os agendamentos
     */
    List<AgendamentoVisitaResponseDTO> listarAgendamentos();
    
    /**
     * Buscar um agendamento específico pelo ID.
     * 
     * @param id ID do agendamento a ser buscado
     * @return O agendamento encontrado
     */
    AgendamentoVisitaResponseDTO buscarAgendamentoPorId(Long id);
    
    /**
     * Filtrar agendamentos com base em critérios específicos.
     * 
     * @param filtro Objeto contendo critérios de filtro
     * @param pageable Configurações de paginação
     * @return Página de agendamentos que atendem aos critérios
     */
    PageResponseDTO<AgendamentoVisitaResponseDTO> filtrarAgendamentos(FiltroAgendamentoDTO filtro, Pageable pageable);
    
    /**
     * Buscar agendamentos por data.
     * 
     * @param data Data para filtrar agendamentos
     * @param pageable Configurações de paginação
     * @return Página de agendamentos na data especificada
     */
    PageResponseDTO<AgendamentoVisitaResponseDTO> buscarAgendamentosPorData(LocalDate data, Pageable pageable);
    
    /**
     * Buscar agendamentos de um custodiado.
     * 
     * @param custodiadoId ID do custodiado
     * @param pageable Configurações de paginação
     * @return Página de agendamentos do custodiado
     */
    PageResponseDTO<AgendamentoVisitaResponseDTO> buscarAgendamentosPorCustodiado(Long custodiadoId, Pageable pageable);
    
    /**
     * Buscar agendamentos de um visitante.
     * 
     * @param visitanteId ID do visitante
     * @param pageable Configurações de paginação
     * @return Página de agendamentos do visitante
     */
    PageResponseDTO<AgendamentoVisitaResponseDTO> buscarAgendamentosPorVisitante(Long visitanteId, Pageable pageable);
    
    /**
     * Cancelar um agendamento existente.
     * 
     * @param id ID do agendamento a ser cancelado
     */
    void cancelarAgendamento(Long id);
}
