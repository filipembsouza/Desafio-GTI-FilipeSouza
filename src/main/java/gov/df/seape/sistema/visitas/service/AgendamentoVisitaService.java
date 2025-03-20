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
     * Agendar uma nova visita.
     * 
     * @param agendamentoDTO Dados do agendamento a ser criado
     * @return O agendamento criado, com ID gerado
     */
    AgendamentoVisitaDTO agendarVisita(AgendamentoVisitaDTO agendamentoDTO);
    
    /**
     * Atualizar um agendamento existente.
     * 
     * @param id ID do agendamento a ser atualizado
     * @param agendamentoDTO Novos dados do agendamento
     * @return O agendamento atualizado
     */
    AgendamentoVisitaDTO atualizarVisita(Long id, AgendamentoVisitaDTO agendamentoDTO);
    
    /**
     * Buscar agendamentos aplicando filtros opcionais.
     * 
     * @param filtro Objeto contendo critérios de busca
     * @return Lista de DTOs representando os agendamentos encontrados
     */
    List<AgendamentoVisitaDTO> buscarAgendamentosPorFiltros(FiltroAgendamentoDTO filtro);
    
    /**
     * Cancelar um agendamento de visita.
     * 
     * @param id ID do agendamento a ser cancelado
     */
    void cancelarVisita(Long id);
}
