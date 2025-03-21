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
 * Repositório para gerenciamento de Funcionalidades no sistema.
 * 
 * Fornece métodos especializados para consulta e manipulação 
 * de funcionalidades, com suporte a diversos tipos de busca.
 */
@Repository
public interface FuncionalidadeRepository extends JpaRepository<Funcionalidade, Long> {
    
    /**
     * Busca uma funcionalidade pela descrição exata, ignorando case.
     * 
     * @param descricao Descrição da funcionalidade
     * @return Funcionalidade encontrada ou Optional vazio
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.descricao) = LOWER(:descricao)")
    Optional<Funcionalidade> findByDescricaoIgnoreCase(@Param("descricao") String descricao);
    
    /**
     * Busca uma funcionalidade pela authority exata, ignorando case.
     * 
     * @param authority Identificador técnico da funcionalidade
     * @return Funcionalidade encontrada ou Optional vazio
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.authority) = LOWER(:authority)")
    Optional<Funcionalidade> findByAuthorityIgnoreCase(@Param("authority") String authority);
    
    /**
     * Lista todas as funcionalidades, ordenadas alfabeticamente.
     * 
     * @return Lista de funcionalidades ordenadas
     */
    @Query("SELECT f FROM Funcionalidade f ORDER BY f.descricao ASC")
    List<Funcionalidade> findAllOrderByDescricao();
    
    /**
     * Busca funcionalidades por descrição parcial, sem paginação.
     * 
     * @param descricao Termo de busca
     * @return Lista de funcionalidades correspondentes
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')) ORDER BY f.descricao ASC")
    List<Funcionalidade> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);
    
    /**
     * Busca funcionalidades por descrição parcial, com suporte a paginação.
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
     * Sobrescreve método de listagem com suporte a paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades
     */
    @Override
    @NonNull
    Page<Funcionalidade> findAll(@NonNull Pageable pageable);
    
    /**
     * Busca funcionalidades associadas a um perfil específico.
     * 
     * @param perfil Perfil para busca
     * @return Lista de funcionalidades do perfil
     */
    @Query("SELECT f FROM Funcionalidade f JOIN f.perfis p WHERE p = :perfil ORDER BY f.descricao ASC")
    List<Funcionalidade> findByPerfil(@Param("perfil") Perfil perfil);
    
    /**
     * Busca funcionalidades por ID do perfil.
     * 
     * @param perfilId ID do perfil
     * @return Lista de funcionalidades do perfil
     */
    @Query("SELECT f FROM Funcionalidade f JOIN f.perfis p WHERE p.id = :perfilId ORDER BY f.descricao ASC")
    List<Funcionalidade> findByPerfilId(@Param("perfilId") Long perfilId);
    
    /**
     * Busca funcionalidades não associadas a um perfil específico.
     * 
     * @param perfilId ID do perfil
     * @return Lista de funcionalidades não associadas
     */
    @Query("SELECT f FROM Funcionalidade f WHERE f NOT IN " +
           "(SELECT f FROM Funcionalidade f JOIN f.perfis p WHERE p.id = :perfilId) " +
           "ORDER BY f.descricao ASC")
    List<Funcionalidade> findNotInPerfil(@Param("perfilId") Long perfilId);
    
    /**
     * Busca funcionalidades por authority parcial, sem paginação.
     * 
     * @param authority Termo de busca na authority
     * @return Lista de funcionalidades correspondentes
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.authority) LIKE LOWER(CONCAT('%', :authority, '%')) ORDER BY f.authority ASC")
    List<Funcionalidade> findByAuthorityContainingIgnoreCase(@Param("authority") String authority);

    /**
     * Busca funcionalidades por authority parcial, com paginação.
     * 
     * @param authority Termo de busca na authority
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades correspondentes
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.authority) LIKE LOWER(CONCAT('%', :authority, '%'))")
    Page<Funcionalidade> findByAuthorityContainingIgnoreCase(
        @Param("authority") String authority, 
        Pageable pageable
    );
    
    /**
     * Conta quantidade de perfis associados a cada funcionalidade.
     * 
     * @return Lista de arrays com [ID, descrição, quantidade de perfis]
     */
    @Query("SELECT f.id, f.descricao, COUNT(p) FROM Funcionalidade f LEFT JOIN f.perfis p " +
           "GROUP BY f.id, f.descricao ORDER BY COUNT(p) DESC")
    List<Object[]> countPerfisByFuncionalidade();
    
    /**
     * Busca funcionalidades sem perfis associados.
     * 
     * @return Lista de funcionalidades sem associação
     */
    @Query("SELECT f FROM Funcionalidade f WHERE NOT EXISTS (SELECT 1 FROM f.perfis)")
    List<Funcionalidade> findFuncionalidadesWithoutPerfis();
}