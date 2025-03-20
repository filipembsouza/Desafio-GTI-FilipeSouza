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
 * Repositório para operações de persistência e consulta da entidade AgendamentoVisita.
 * Gerencia os agendamentos de visitas no sistema prisional.
 */
@Repository
public interface AgendamentoVisitaRepository extends JpaRepository<AgendamentoVisita, Long> {
    
    /**
     * Lista agendamentos de um custodiado.
     * 
     * @param custodiado Custodiado para filtrar agendamentos
     * @return Lista de agendamentos do custodiado
     */
    List<AgendamentoVisita> findByCustodiado(Custodiado custodiado);
    
    /**
     * Lista agendamentos de um visitante.
     * 
     * @param visitante Visitante para filtrar agendamentos
     * @return Lista de agendamentos do visitante
     */
    List<AgendamentoVisita> findByVisitante(Visitante visitante);
    
    /**
     * Lista agendamentos por status.
     * 
     * @param status Status para filtrar agendamentos
     * @return Lista de agendamentos com o status especificado
     */
    List<AgendamentoVisita> findByStatus(Status status);
    
    /**
     * Busca agendamentos para uma data específica.
     * 
     * @param data Data para filtrar agendamentos
     * @return Lista de agendamentos na data
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE FUNCTION('DATE', a.dataHoraAgendamento) = :data")
    List<AgendamentoVisita> findByData(@Param("data") LocalDate data);
    
    /**
     * Busca agendamentos dentro de um período específico.
     * 
     * @param inicio Data e hora de início
     * @param fim Data e hora de fim
     * @return Lista de agendamentos no período
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE a.dataHoraAgendamento BETWEEN :inicio AND :fim")
    List<AgendamentoVisita> findByPeriodo(
        @Param("inicio") LocalDateTime inicio, 
        @Param("fim") LocalDateTime fim
    );
    
    /**
     * Busca agendamentos conflitantes para um custodiado.
     * 
     * @param custodiado Custodiado
     * @param inicio Data e hora de início
     * @param fim Data e hora de fim
     * @param statusCancelado Status de agendamento cancelado
     * @return Lista de agendamentos conflitantes
     */
    @Query("SELECT a FROM AgendamentoVisita a WHERE a.custodiado = :custodiado " +
           "AND a.status != :statusCancelado " +
           "AND a.dataHoraAgendamento BETWEEN :inicio AND :fim " +
           "ORDER BY a.dataHoraAgendamento ASC")
    List<AgendamentoVisita> findAgendamentosConflitantes(
        @Param("custodiado") Custodiado custodiado,
        @Param("inicio") LocalDateTime inicio,
        @Param("fim") LocalDateTime fim,
        @Param("statusCancelado") Status statusCancelado
    );
    
    /**
     * Conta visitas agendadas para um custodiado em um dia.
     * 
     * @param custodiadoId ID do custodiado
     * @param inicioDia Início do dia
     * @param fimDia Fim do dia
     * @return Número de visitas agendadas
     */
    @Query("SELECT COUNT(a) FROM AgendamentoVisita a WHERE a.custodiado.id = :custodiadoId " +
           "AND a.dataHoraAgendamento BETWEEN :inicioDia AND :fimDia")
    long countByCustodiadoIdAndDataHoraAgendamentoBetween(
        @Param("custodiadoId") Long custodiadoId,
        @Param("inicioDia") LocalDateTime inicioDia,
        @Param("fimDia") LocalDateTime fimDia
    );
}