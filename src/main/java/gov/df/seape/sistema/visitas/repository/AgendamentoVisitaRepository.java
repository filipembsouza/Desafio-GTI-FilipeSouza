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
 */
@Repository
public interface AgendamentoVisitaRepository extends JpaRepository<AgendamentoVisita, Long> {
    
    /**
     * Lista todos os agendamentos de um determinado custodiado.
     * 
     * @param custodiado Custodiado para filtrar os agendamentos
     * @return Lista de agendamentos do custodiado especificado
     */
    List<AgendamentoVisita> findByCustodiado(Custodiado custodiado);
    
    /**
     * Lista todos os agendamentos de um determinado custodiado pelo ID.
     * 
     * @param custodiadoId ID do custodiado para filtrar os agendamentos
     * @return Lista de agendamentos do custodiado especificado
     */
    List<AgendamentoVisita> findByCustodiadoId(Long custodiadoId);
    
    /**
     * Lista todos os agendamentos de um determinado custodiado pelo ID com paginação.
     * 
     * @param custodiadoId ID do custodiado para filtrar os agendamentos
     * @param pageable Informações de paginação
     * @return Página de agendamentos do custodiado especificado
     */
    Page<AgendamentoVisita> findByCustodiadoId(Long custodiadoId, Pageable pageable);
    
    /**
     * Lista todos os agendamentos de um determinado visitante.
     * 
     * @param visitante Visitante para filtrar os agendamentos
     * @return Lista de agendamentos do visitante especificado
     */
    List<AgendamentoVisita> findByVisitante(Visitante visitante);
    
    /**
     * Lista todos os agendamentos de um determinado visitante pelo ID.
     * 
     * @param visitanteId ID do visitante para filtrar os agendamentos
     * @return Lista de agendamentos do visitante especificado
     */
    List<AgendamentoVisita> findByVisitanteId(Long visitanteId);
    
    /**
     * Lista todos os agendamentos de um determinado visitante pelo ID com paginação.
     * 
     * @param visitanteId ID do visitante para filtrar os agendamentos
     * @param pageable Informações de paginação
     * @return Página de agendamentos do visitante especificado
     */
    Page<AgendamentoVisita> findByVisitanteId(Long visitanteId, Pageable pageable);
    
    /**
     * Lista todos os agendamentos com um determinado status.
     * 
     * @param status Status para filtrar os agendamentos
     * @return Lista de agendamentos com o status especificado
     */
    List<AgendamentoVisita> findByStatus(Status status);
    
    /**
     * Lista todos os agendamentos com um determinado status pelo ID.
     * 
     * @param statusId ID do status para filtrar os agendamentos
     * @return Lista de agendamentos com o status especificado
     */
    List<AgendamentoVisita> findByStatusId(Long statusId);
    
    /**
     * Lista todos os agendamentos com um determinado status pelo ID com paginação.
     * 
     * @param statusId ID do status para filtrar os agendamentos
     * @param pageable Informações de paginação
     * @return Página de agendamentos com o status especificado
     */
    Page<AgendamentoVisita> findByStatusId(Long statusId, Pageable pageable);
    
    /**
     * Conta o número de agendamentos com um determinado status.
     * 
     * @param statusId ID do status
     * @return Número de agendamentos com o status especificado
     */
    long countByStatusId(Long statusId);
    
    /**
     * Lista agendamentos para uma data específica.
     * 
     * @param data Data para filtrar os agendamentos
     * @return Lista de agendamentos para a data especificada
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE FUNCTION('DATE', a.dataHoraAgendamento) = :data")
    List<AgendamentoVisita> findByData(@Param("data") LocalDate data);
    
    /**
     * Lista agendamentos para uma data específica com paginação.
     * 
     * @param data Data para filtrar os agendamentos
     * @param pageable Informações de paginação
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
    @Query("SELECT a FROM AgendamentoVisita a WHERE a.dataHoraAgendamento BETWEEN :inicio AND :fim ORDER BY a.dataHoraAgendamento ASC")
    List<AgendamentoVisita> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    /**
     * Conta o número de agendamentos para um custodiado em um determinado período.
     * 
     * @param custodiadoId ID do custodiado
     * @param inicio Data e hora de início do período
     * @param fim Data e hora de fim do período
     * @return Número de agendamentos do custodiado no período especificado
     */
    @Query("SELECT COUNT(a) FROM AgendamentoVisita a WHERE a.custodiado.id = :custodiadoId AND a.dataHoraAgendamento BETWEEN :inicio AND :fim")
    long countByCustodiadoIdAndDataHoraAgendamentoBetween(
            @Param("custodiadoId") Long custodiadoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);

    /**
     * Lista agendamentos de visitante em um determinado período.
     * 
     * @param visitanteId ID do visitante
     * @param inicio Data e hora de início do período
     * @param fim Data e hora de fim do período
     * @return Lista de agendamentos do visitante no período especificado
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE a.visitante.id = :visitanteId AND a.dataHoraAgendamento BETWEEN :inicio AND :fim ORDER BY a.dataHoraAgendamento ASC")
    List<AgendamentoVisita> findByVisitanteIdAndDataHoraAgendamenteBetween(
            @Param("visitanteId") Long visitanteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
    
    /**
     * Lista agendamentos conflitantes para um mesmo custodiado.
     * 
     * @param custodiadoId ID do custodiado
     * @param inicio Data e hora de início do período
     * @param fim Data e hora de fim do período
     * @param statusCanceladoId ID do status CANCELADO
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
     * Busca agendamentos com filtros combinados.
     * 
     * @param custodiado Custodiado para filtrar (opcional)
     * @param visitante Visitante para filtrar (opcional)
     * @param inicio Data e hora de início do período (opcional)
     * @param fim Data e hora de fim do período (opcional)
     * @param status Status para filtrar (opcional)
     * @return Lista de agendamentos que atendem aos critérios especificados
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE " +
           "(:custodiado IS NULL OR a.custodiado = :custodiado) AND " +
           "(:visitante IS NULL OR a.visitante = :visitante) AND " +
           "(:inicio IS NULL OR a.dataHoraAgendamento >= :inicio) AND " +
           "(:fim IS NULL OR a.dataHoraAgendamento <= :fim) AND " +
           "(:status IS NULL OR a.status = :status) " +
           "ORDER BY a.dataHoraAgendamento DESC")
    List<AgendamentoVisita> findComFiltros(
            @Param("custodiado") Custodiado custodiado,
            @Param("visitante") Visitante visitante,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim,
            @Param("status") Status status);
    
    /**
     * Busca agendamentos com filtros combinados e paginação.
     * 
     * @param custodiadoId ID do custodiado para filtrar (opcional)
     * @param visitanteId ID do visitante para filtrar (opcional)
     * @param inicio Data e hora de início do período (opcional)
     * @param fim Data e hora de fim do período (opcional)
     * @param statusId ID do status para filtrar (opcional)
     * @param pageable Informações de paginação
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
     * Lista IDs de visitantes que visitaram um determinado custodiado.
     * 
     * @param custodiadoId ID do custodiado
     * @return Lista de IDs de visitantes
     */
    @Query("SELECT DISTINCT a.visitante.id FROM AgendamentoVisita a " +
           "WHERE a.custodiado.id = :custodiadoId AND a.status.descricao <> 'CANCELADO'")
    List<Long> findVisitanteIdsByCustodiadoId(@Param("custodiadoId") Long custodiadoId);
}