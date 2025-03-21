package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade Status.
 * Fornece métodos para buscar status pelo nome e listar todos os status disponíveis.
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    /**
     * Busca um status pela descrição, ignorando maiúsculas e minúsculas.
     *
     * @param descricao Nome do status a ser buscado (exemplo: "AGENDADO", "CANCELADO").
     * @return O status correspondente, se existir.
     */
    @Query("SELECT s FROM Status s WHERE LOWER(s.descricao) = LOWER(:descricao)")
    Optional<Status> findByDescricaoIgnoreCase(@Param("descricao") String descricao);

    /**
     * Lista todos os status cadastrados, ordenados alfabeticamente.
     *
     * @return Lista de status ordenada por descrição.
     */
    @Query("SELECT s FROM Status s ORDER BY s.descricao ASC")
    List<Status> findAllOrderByDescricao();
    
    /**
     * Busca status cuja descrição contenha o termo especificado.
     * Útil para pesquisas em telas de gerenciamento de status.
     * 
     * @param descricao Termo de busca para a descrição do status
     * @return Lista de status que contêm o termo na descrição, ordenados alfabeticamente
     */
    @Query("SELECT s FROM Status s WHERE LOWER(s.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')) ORDER BY s.descricao ASC")
    List<Status> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);
    
    /**
     * Busca status cuja descrição contenha o termo especificado, com suporte a paginação.
     * 
     * @param descricao Termo de busca para a descrição do status
     * @param pageable Objeto com informações de paginação
     * @return Página de status que contêm o termo na descrição
     */
    @Query("SELECT s FROM Status s WHERE LOWER(s.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    Page<Status> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao, Pageable pageable);
    
    /**
     * Lista todos os status com suporte a paginação.
     * 
     * @param pageable Objeto com informações de paginação
     * @return Página de status
     */
    Page<Status> findAll(Pageable pageable);
    
    /**
     * Verifica se um status é usado em algum agendamento.
     * Útil para validar antes de remover um status.
     * 
     * @param statusId ID do status a ser verificado
     * @return true se o status estiver em uso, false caso contrário
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AgendamentoVisita a WHERE a.status.id = :statusId")
    boolean isStatusInUso(@Param("statusId") Long statusId);
    
    /**
     * Conta quantos agendamentos estão associados a cada status.
     * Útil para relatórios e dashboards.
     * 
     * @return Lista de arrays contendo [ID do status, descrição, quantidade de agendamentos]
     */
    @Query("SELECT s.id, s.descricao, COUNT(a) FROM Status s LEFT JOIN AgendamentoVisita a ON a.status.id = s.id " +
           "GROUP BY s.id, s.descricao ORDER BY COUNT(a) DESC")
    List<Object[]> countAgendamentosByStatus();
}