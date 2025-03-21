package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.AgendamentoVisita;
import gov.df.seape.sistema.visitas.model.Custodiado;
import gov.df.seape.sistema.visitas.model.Status;
import gov.df.seape.sistema.visitas.model.Visitante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório para a entidade AgendamentoVisita.
 * Fornece métodos para realizar operações de banco de dados relacionadas a Agendamentos de Visitas.
 */
@Repository
public interface AgendamentoVisitaRepository extends JpaRepository<AgendamentoVisita, Long> {
    
    /**
     * Lista todos os agendamentos de um determinado custodiado.
     * 
     * @param custodiado O custodiado para filtrar os agendamentos
     * @return Lista de agendamentos do custodiado especificado
     */
    List<AgendamentoVisita> findByCustodiado(Custodiado custodiado);
    
    /**
     * Lista todos os agendamentos de um determinado custodiado, usando o ID diretamente.
     * Esta versão simplifica a busca quando só temos o ID do custodiado,
     * evitando a necessidade de buscar a entidade Custodiado completa antes.
     * 
     * @param custodiadoId O ID do custodiado para filtrar os agendamentos
     * @return Lista de agendamentos do custodiado especificado
     */
    List<AgendamentoVisita> findByCustodiadoId(Long custodiadoId);
    
    /**
     * Lista todos os agendamentos de um determinado custodiado com suporte a paginação.
     * 
     * @param custodiadoId O ID do custodiado para filtrar os agendamentos
     * @param pageable Objeto com informações de paginação (página, tamanho, ordenação)
     * @return Página de agendamentos do custodiado especificado
     */
    Page<AgendamentoVisita> findByCustodiadoId(Long custodiadoId, Pageable pageable);
    
    /**
     * Lista todos os agendamentos de um determinado visitante.
     * 
     * @param visitante O visitante para filtrar os agendamentos
     * @return Lista de agendamentos do visitante especificado
     */
    List<AgendamentoVisita> findByVisitante(Visitante visitante);
    
    /**
     * Lista todos os agendamentos de um determinado visitante, usando o ID diretamente.
     * 
     * @param visitanteId O ID do visitante para filtrar os agendamentos
     * @return Lista de agendamentos do visitante especificado
     */
    List<AgendamentoVisita> findByVisitanteId(Long visitanteId);
    
    /**
     * Lista todos os agendamentos de um determinado visitante com suporte a paginação.
     * 
     * @param visitanteId O ID do visitante para filtrar os agendamentos
     * @param pageable Objeto com informações de paginação
     * @return Página de agendamentos do visitante especificado
     */
    Page<AgendamentoVisita> findByVisitanteId(Long visitanteId, Pageable pageable);
    
    /**
     * Lista todos os agendamentos com um determinado status.
     * 
     * @param status O status para filtrar os agendamentos
     * @return Lista de agendamentos com o status especificado
     */
    List<AgendamentoVisita> findByStatus(Status status);
    
    /**
     * Lista todos os agendamentos com um determinado status, usando o ID diretamente.
     * 
     * @param statusId O ID do status para filtrar os agendamentos
     * @return Lista de agendamentos com o status especificado
     */
    List<AgendamentoVisita> findByStatusId(Long statusId);
    
    /**
     * Lista todos os agendamentos com um determinado status com suporte a paginação.
     * 
     * @param statusId O ID do status para filtrar os agendamentos
     * @param pageable Objeto com informações de paginação
     * @return Página de agendamentos com o status especificado
     */
    Page<AgendamentoVisita> findByStatusId(Long statusId, Pageable pageable);
    
    /**
     * Lista agendamentos para uma data específica (considerando apenas a data, não a hora).
     * 
     * @param data A data para filtrar os agendamentos
     * @return Lista de agendamentos para a data especificada
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE FUNCTION('DATE', a.dataHoraAgendamento) = :data")
    List<AgendamentoVisita> findByData(@Param("data") LocalDate data);    
    
    /**
     * Lista agendamentos para uma data específica com suporte a paginação.
     * 
     * @param data A data para filtrar os agendamentos
     * @param pageable Objeto com informações de paginação
     * @return Página de agendamentos para a data especificada
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE FUNCTION('DATE', a.dataHoraAgendamento) = :data")
    Page<AgendamentoVisita> findByData(@Param("data") LocalDate data, Pageable pageable);
    
    /**
     * Lista agendamentos dentro de um período específico.
     * 
     * @param inicio Data e hora de início do período
     * @param fim Data e hora de fim do período
     * @return Lista de agendamentos dentro do período especificado
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE a.dataHoraAgendamento BETWEEN :inicio AND :fim")
    List<AgendamentoVisita> findByPeriodo(
            @Param("inicio") LocalDateTime inicio, 
            @Param("fim") LocalDateTime fim);
    
    /**
     * Lista agendamentos dentro de um período específico com suporte a paginação.
     * 
     * @param inicio Data e hora de início do período
     * @param fim Data e hora de fim do período
     * @param pageable Objeto com informações de paginação
     * @return Página de agendamentos dentro do período especificado
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE a.dataHoraAgendamento BETWEEN :inicio AND :fim")
    Page<AgendamentoVisita> findByPeriodo(
            @Param("inicio") LocalDateTime inicio, 
            @Param("fim") LocalDateTime fim,
            Pageable pageable);
    
    /**
     * Verifica se existem agendamentos conflitantes para um mesmo custodiado.
     * Esta consulta é fundamental para implementar a regra de negócio que não
     * permite agendamentos conflitantes para o mesmo detento em um mesmo horário.
     * 
     * @param custodiado O custodiado para verificar
     * @param inicio Data e hora de início do período
     * @param fim Data e hora de fim do período
     * @param statusCancelado Status que representa agendamentos cancelados
     * @return Lista de agendamentos que possam entrar em conflito
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE a.custodiado = :custodiado " +
           "AND a.status != :statusCancelado " +
           "AND a.dataHoraAgendamento BETWEEN :inicio AND :fim " +
           "ORDER BY a.dataHoraAgendamento ASC")
    List<AgendamentoVisita> findAgendamentosConflitantes(
            @Param("custodiado") Custodiado custodiado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusCancelado") Status statusCancelado);
    
    /**
     * Verifica se existem agendamentos conflitantes para um mesmo custodiado, usando IDs diretamente.
     * 
     * @param custodiadoId O ID do custodiado para verificar
     * @param inicio Data e hora de início do período
     * @param fim Data e hora de fim do período
     * @param statusCanceladoId O ID do status que representa agendamentos cancelados
     * @return Lista de agendamentos que possam entrar em conflito
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE a.custodiado.id = :custodiadoId " +
           "AND a.status.id != :statusCanceladoId " +
           "AND a.dataHoraAgendamento BETWEEN :inicio AND :fim " +
           "ORDER BY a.dataHoraAgendamento ASC")
    List<AgendamentoVisita> findAgendamentosConflitantes(
            @Param("custodiadoId") Long custodiadoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusCanceladoId") Long statusCanceladoId);

    /**
     * Conta quantas visitas foram agendadas para um custodiado em um determinado dia.
     * Essa consulta é usada para garantir que um custodiado não receba mais de 2 visitantes no mesmo dia.
     * 
     * @param custodiadoId ID do custodiado para verificar as visitas.
     * @param inicioDia Início do período (início do dia).
     * @param fimDia Fim do período (fim do dia).
     * @return O número de visitas agendadas para o custodiado no período especificado.
     */
    @Query("SELECT COUNT(a) FROM AgendamentoVisita a WHERE a.custodiado.id = :custodiadoId " +
           "AND a.dataHoraAgendamento BETWEEN :inicioDia AND :fimDia")
    long countByCustodiadoIdAndDataHoraAgendamentoBetween(
            @Param("custodiadoId") Long custodiadoId,
            @Param("inicioDia") LocalDateTime inicioDia,
            @Param("fimDia") LocalDateTime fimDia);
    
    /**
     * Busca agendamentos com filtros combinados (custodiado, visitante e período).
     * 
     * @param custodiadoId O ID do custodiado para filtrar (opcional)
     * @param visitanteId O ID do visitante para filtrar (opcional)
     * @param inicio Data e hora de início do período (opcional)
     * @param fim Data e hora de fim do período (opcional)
     * @param statusId O ID do status para filtrar (opcional)
     * @param pageable Objeto com informações de paginação
     * @return Página de agendamentos que atendem aos critérios especificados
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE " +
           "(:custodiadoId IS NULL OR a.custodiado.id = :custodiadoId) AND " +
           "(:visitanteId IS NULL OR a.visitante.id = :visitanteId) AND " +
           "(:inicio IS NULL OR a.dataHoraAgendamento >= :inicio) AND " +
           "(:fim IS NULL OR a.dataHoraAgendamento <= :fim) AND " +
           "(:statusId IS NULL OR a.status.id = :statusId)")
    Page<AgendamentoVisita> findComFiltrosPaginado(
            @Param("custodiadoId") Long custodiadoId,
            @Param("visitanteId") Long visitanteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusId") Long statusId,
            Pageable pageable);
    
    /**
     * Retorna estatísticas de agendamentos por status em um período específico.
     * 
     * @param inicio Data de início do período
     * @param fim Data de fim do período
     * @return Lista contendo status e quantidade de agendamentos em cada status
     */
    @Query("SELECT a.status.descricao as status, COUNT(a) as quantidade FROM AgendamentoVisita a " +
           "WHERE a.dataHoraAgendamento BETWEEN :inicio AND :fim " +
           "GROUP BY a.status.descricao ORDER BY COUNT(a) DESC")
    List<Object[]> contarAgendamentosPorStatus(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
    
    /**
     * Verifica se um visitante já tem agendamento no horário especificado.
     * Importante para garantir que um visitante não tenha dois agendamentos ao mesmo tempo.
     * 
     * @param visitanteId ID do visitante
     * @param inicio Início do período de verificação
     * @param fim Fim do período de verificação
     * @param statusCanceladoId ID do status cancelado
     * @return true se existir agendamento no horário, false caso contrário
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AgendamentoVisita a " +
           "WHERE a.visitante.id = :visitanteId " +
           "AND a.status.id != :statusCanceladoId " +
           "AND a.dataHoraAgendamento BETWEEN :inicio AND :fim")
    boolean existsAgendamentoVisitanteNoHorario(
            @Param("visitanteId") Long visitanteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("statusCanceladoId") Long statusCanceladoId);
}