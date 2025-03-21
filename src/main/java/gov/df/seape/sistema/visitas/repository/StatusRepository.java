package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade Status.
 * Fornece métodos para consulta e gerenciamento de status no sistema.
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    /**
     * Busca um status pela descrição, ignorando maiúsculas e minúsculas.
     *
     * @param descricao Nome do status a ser buscado
     * @return Optional contendo o status correspondente
     */
    @Query("SELECT s FROM Status s WHERE LOWER(s.descricao) = LOWER(:descricao)")
    Optional<Status> findByDescricaoIgnoreCase(@Param("descricao") String descricao);

    /**
     * Lista todos os status cadastrados, ordenados alfabeticamente.
     *
     * @return Lista de status ordenada por descrição
     */
    @Query("SELECT s FROM Status s ORDER BY s.descricao ASC")
    @NonNull
    List<Status> findAllOrderByDescricao();
    
    /**
     * Busca status pela descrição parcial, ignorando maiúsculas e minúsculas.
     * 
     * @param descricao Termo de busca para a descrição do status
     * @return Lista de status correspondentes
     */
    @Query("SELECT s FROM Status s WHERE LOWER(s.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')) ORDER BY s.descricao ASC")
    @NonNull
    List<Status> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);
    
    /**
     * Busca status paginados por descrição parcial.
     * 
     * @param descricao Termo de busca para a descrição do status
     * @param pageable Configurações de paginação
     * @return Página de status correspondentes
     */
    @Query("SELECT s FROM Status s WHERE LOWER(s.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    @NonNull
    Page<Status> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao, @NonNull Pageable pageable);
    
    /**
     * Lista todos os status com suporte a paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de status
     */
    @NonNull
    Page<Status> findAll(@NonNull Pageable pageable);
    
    /**
     * Verifica se um status está em uso em agendamentos.
     * 
     * @param statusId Identificador do status
     * @return Indicador de uso do status
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM AgendamentoVisita a WHERE a.status.id = :statusId")
    boolean isStatusInUso(@Param("statusId") Long statusId);
    
    /**
     * Conta agendamentos por status.
     * 
     * @return Lista com contagem de agendamentos por status
     */
    @Query("SELECT s.id, s.descricao, COUNT(a) FROM Status s LEFT JOIN AgendamentoVisita a ON a.status.id = s.id " +
           "GROUP BY s.id, s.descricao ORDER BY COUNT(a) DESC")
    @NonNull
    List<Object[]> countAgendamentosByStatus();
}