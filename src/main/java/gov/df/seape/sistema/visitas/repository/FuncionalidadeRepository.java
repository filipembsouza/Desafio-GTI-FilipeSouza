package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.model.Perfil;
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
 * Repositório para operações de persistência e consulta da entidade Funcionalidade.
 * Gerencia as funcionalidades do sistema de segurança.
 */
@Repository
public interface FuncionalidadeRepository extends JpaRepository<Funcionalidade, Long> {
    
    /**
     * Busca uma funcionalidade pela descrição exata, ignorando maiúsculas/minúsculas.
     * 
     * @param descricao Descrição da funcionalidade
     * @return Optional contendo a funcionalidade, se encontrada
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.descricao) = LOWER(:descricao)")
    Optional<Funcionalidade> findByDescricaoIgnoreCase(@Param("descricao") String descricao);
    
    /**
     * Busca uma funcionalidade pela authority exata, ignorando maiúsculas/minúsculas.
     * 
     * @param authority Identificador técnico da funcionalidade
     * @return Optional contendo a funcionalidade, se encontrada
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.authority) = LOWER(:authority)")
    Optional<Funcionalidade> findByAuthorityIgnoreCase(@Param("authority") String authority);
    
    /**
     * Lista todas as funcionalidades, ordenadas alfabeticamente.
     * 
     * @return Lista de funcionalidades ordenada
     */
    @Query("SELECT f FROM Funcionalidade f ORDER BY f.descricao ASC")
    List<Funcionalidade> findAllOrderByDescricao();
    
    /**
     * Busca funcionalidades que contenham a descrição especificada.
     * 
     * @param descricao Termo de busca
     * @return Lista de funcionalidades correspondentes
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')) ORDER BY f.descricao ASC")
    List<Funcionalidade> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);
    
    /**
     * Busca funcionalidades com suporte a paginação.
     * 
     * @param descricao Termo de busca
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades correspondentes
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    @NonNull
    Page<Funcionalidade> findByDescricaoContainingIgnoreCase(
        @Param("descricao") String descricao, 
        @NonNull Pageable pageable
    );
    
    /**
     * Lista funcionalidades associadas a um determinado perfil.
     * 
     * @param perfil Perfil para buscar funcionalidades
     * @return Lista de funcionalidades do perfil
     */
    @Query("SELECT f FROM Funcionalidade f JOIN f.perfis p WHERE p = :perfil ORDER BY f.descricao ASC")
    List<Funcionalidade> findByPerfil(@Param("perfil") Perfil perfil);
    
    /**
     * Lista funcionalidades não associadas a um determinado perfil.
     * 
     * @param perfilId ID do perfil
     * @return Lista de funcionalidades não associadas
     */
    @Query("SELECT f FROM Funcionalidade f WHERE f NOT IN " +
           "(SELECT f FROM Funcionalidade f JOIN f.perfis p WHERE p.id = :perfilId) " +
           "ORDER BY f.descricao ASC")
    List<Funcionalidade> findNotInPerfil(@Param("perfilId") Long perfilId);
}