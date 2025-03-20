package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaDTO;
import gov.df.seape.sistema.visitas.dto.FiltroAgendamentoDTO;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Agendamentos de Visita.
 * Define o contrato de operações disponíveis para manipulação de agendamentos.
 */
public interface AgendamentoVisitaService {
    
    /**
     * Cria um novo agendamento de visita.
     * 
     * @param agendamentoDTO Dados do agendamento a ser criado
     * @return O agendamento criado com ID gerado
     */
    AgendamentoVisitaDTO criarAgendamento(AgendamentoVisitaDTO agendamentoDTO);
    
    /**
     * Atualiza um agendamento existente.
     * 
     * @param id ID do agendamento a ser atualizado
     * @param agendamentoDTO Novos dados do agendamento
     * @return O agendamento atualizado
     */
    AgendamentoVisitaDTO atualizarAgendamento(Long id, AgendamentoVisitaDTO agendamentoDTO);
    
    /**
     * Lista todos os agendamentos.
     * 
     * @return Lista de todos os agendamentos
     */
    List<AgendamentoVisitaDTO> listarAgendamentos();
    
    /**
     * Busca um agendamento pelo seu ID.
     * 
     * @param id ID do agendamento a ser buscado
     * @return O agendamento encontrado
     */
    AgendamentoVisitaDTO buscarAgendamentoPorId(Long id);
    
    /**
     * Cancela um agendamento de visita.
     * 
     * @param id ID do agendamento a ser cancelado
     */
    void cancelarAgendamento(Long id);
    
    /**
     * Filtra agendamentos de acordo com os critérios fornecidos.
     * 
     * @param filtro Objeto contendo critérios de filtro
     * @return Lista de agendamentos filtrados
     */
    List<AgendamentoVisitaDTO> filtrarAgendamentos(FiltroAgendamentoDTO filtro);
    
    /**
     * Cancela uma visita (método legado).
     * 
     * @param id ID da visita a ser cancelada
     */
    void cancelarVisita(Long id);
}