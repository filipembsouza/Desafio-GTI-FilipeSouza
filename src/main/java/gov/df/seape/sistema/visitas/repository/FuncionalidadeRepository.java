package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.model.Perfil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade Funcionalidade.
 * Fornece métodos para realizar operações de banco de dados relacionadas a Funcionalidades.
 */
@Repository
public interface FuncionalidadeRepository extends JpaRepository<Funcionalidade, Long> {
    
    /**
     * Busca uma funcionalidade pela descrição exata, ignorando maiúsculas e minúsculas.
     * 
     * @param descricao A descrição da funcionalidade a ser buscada
     * @return A funcionalidade encontrada, ou vazia se não existir
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.descricao) = LOWER(:descricao)")
    Optional<Funcionalidade> findByDescricaoIgnoreCase(@Param("descricao") String descricao);
    
    /**
     * Busca uma funcionalidade pela authority exata, ignorando maiúsculas e minúsculas.
     * Útil para integração com o Spring Security.
     * 
     * @param authority O identificador técnico da funcionalidade
     * @return A funcionalidade encontrada, ou vazia se não existir
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.authority) = LOWER(:authority)")
    Optional<Funcionalidade> findByAuthorityIgnoreCase(@Param("authority") String authority);
    
    /**
     * Lista todas as funcionalidades cadastradas, ordenadas alfabeticamente pela descrição.
     *
     * @return Lista de todas as funcionalidades disponíveis no sistema, ordenadas por descrição
     */
    @Query("SELECT f FROM Funcionalidade f ORDER BY f.descricao ASC")
    List<Funcionalidade> findAllOrderByDescricao();
    
    /**
     * Busca funcionalidades cuja descrição contenha o termo especificado.
     * Útil para pesquisas em telas de gerenciamento de funcionalidades,
     * onde o usuário pode digitar parte do nome para localizar uma funcionalidade.
     * 
     * @param descricao Termo de busca para a descrição da funcionalidade
     * @return Lista de funcionalidades que contêm o termo na descrição, ordenadas alfabeticamente
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')) ORDER BY f.descricao ASC")
    List<Funcionalidade> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);
    
    /**
     * Busca funcionalidades cuja descrição contenha o termo especificado, com suporte a paginação.
     * Esta versão paginada é útil para interfaces que precisam mostrar resultados
     * em blocos gerenciáveis e oferecer navegação entre páginas.
     * 
     * @param descricao Termo de busca para a descrição da funcionalidade
     * @param pageable  Objeto com informações de paginação (página, tamanho, ordenação)
     * @return Página de funcionalidades que contêm o termo na descrição
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    Page<Funcionalidade> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao, Pageable pageable);
    
    /**
     * Lista todas as funcionalidades com suporte a paginação.
     * Embora o número de funcionalidades seja geralmente pequeno,
     * manter o padrão de paginação traz consistência com outros repositórios
     * e prepara o sistema para possível crescimento futuro.
     * 
     * @param pageable Objeto com informações de paginação
     * @return Página de funcionalidades
     */
    Page<Funcionalidade> findAll(Pageable pageable);
    
    /**
     * Lista todas as funcionalidades associadas a um determinado perfil.
     * Útil para exibir quais permissões um perfil específico possui.
     * 
     * @param perfil O perfil para buscar funcionalidades associadas
     * @return Lista de funcionalidades associadas ao perfil, ordenadas por descrição
     */
    @Query("SELECT f FROM Funcionalidade f JOIN f.perfis p WHERE p = :perfil ORDER BY f.descricao ASC")
    List<Funcionalidade> findByPerfil(@Param("perfil") Perfil perfil);
    
    /**
     * Lista todas as funcionalidades associadas a um determinado perfil, usando o ID diretamente.
     * Esta versão evita a necessidade de carregar a entidade Perfil completa antes.
     * 
     * @param perfilId O ID do perfil para buscar funcionalidades associadas
     * @return Lista de funcionalidades associadas ao perfil, ordenadas por descrição
     */
    @Query("SELECT f FROM Funcionalidade f JOIN f.perfis p WHERE p.id = :perfilId ORDER BY f.descricao ASC")
    List<Funcionalidade> findByPerfilId(@Param("perfilId") Long perfilId);
    
    /**
     * Lista funcionalidades que não estão associadas a um determinado perfil.
     * Útil para exibir funcionalidades disponíveis para adicionar a um perfil.
     * 
     * @param perfilId O ID do perfil
     * @return Lista de funcionalidades não associadas ao perfil
     */
    @Query("SELECT f FROM Funcionalidade f WHERE f NOT IN " +
           "(SELECT f FROM Funcionalidade f JOIN f.perfis p WHERE p.id = :perfilId) " +
           "ORDER BY f.descricao ASC")
    List<Funcionalidade> findNotInPerfil(@Param("perfilId") Long perfilId);
    
    /**
     * Busca funcionalidades por authority ou parte dela, ignorando maiúsculas e minúsculas.
     * Útil para integração com o Spring Security.
     * 
     * @param authority Parte da authority para busca
     * @return Lista de funcionalidades com authority contendo o termo especificado
     */
    @Query("SELECT f FROM Funcionalidade f WHERE LOWER(f.authority) LIKE LOWER(CONCAT('%', :authority, '%')) ORDER BY f.authority ASC")
    List<Funcionalidade> findByAuthorityContainingIgnoreCase(@Param("authority") String authority);
    
    /**
     * Conta quantos perfis estão associados a cada funcionalidade.
     * Útil para relatórios de segurança e auditoria.
     * 
     * @return Lista de arrays contendo [ID da funcionalidade, descrição, quantidade de perfis]
     */
    @Query("SELECT f.id, f.descricao, COUNT(p) FROM Funcionalidade f LEFT JOIN f.perfis p " +
           "GROUP BY f.id, f.descricao ORDER BY COUNT(p) DESC")
    List<Object[]> countPerfisByFuncionalidade();
    
    /**
     * Lista funcionalidades que não estão associadas a nenhum perfil.
     * Útil para identificar funcionalidades não utilizadas.
     * 
     * @return Lista de funcionalidades sem perfis associados
     */
    @Query("SELECT f FROM Funcionalidade f WHERE NOT EXISTS (SELECT 1 FROM f.perfis)")
    List<Funcionalidade> findFuncionalidadesWithoutPerfis();
}
