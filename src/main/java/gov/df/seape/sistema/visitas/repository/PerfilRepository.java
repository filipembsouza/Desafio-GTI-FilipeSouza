package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Perfil;
import gov.df.seape.sistema.visitas.model.Funcionalidade;
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
 * Repositório para a entidade Perfil.
 * Fornece métodos para realizar operações de banco de dados relacionadas a Perfis de usuário.
 */
@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    
    /**
     * Busca um perfil pela descrição exata, ignorando maiúsculas e minúsculas.
     * Útil para encontrar perfis como "ADMINISTRADOR", "AGENTE", etc.
     * 
     * @param descricao A descrição do perfil a ser buscado
     * @return O perfil encontrado, ou vazio se não existir
     */
    @Query("SELECT p FROM Perfil p WHERE LOWER(p.descricao) = LOWER(:descricao)")
    Optional<Perfil> findByDescricaoIgnoreCase(@Param("descricao") String descricao);

    /**
     * Lista todos os perfis cadastrados, ordenados alfabeticamente pela descrição.
     *
     * @return Lista de todos os perfis disponíveis no sistema, ordenados por descrição
     */
    @Query("SELECT p FROM Perfil p ORDER BY p.descricao ASC")
    List<Perfil> findAllOrderByDescricao();
    
    /**
     * Busca perfis cuja descrição contenha o termo especificado, ignorando maiúsculas e minúsculas.
     * Útil para campos de pesquisa onde o usuário digita parte do nome do perfil.
     * 
     * @param descricao Termo de busca para a descrição do perfil
     * @return Lista de perfis que contêm o termo na descrição, ordenados alfabeticamente
     */
    @Query("SELECT p FROM Perfil p WHERE LOWER(p.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')) ORDER BY p.descricao ASC")
    List<Perfil> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);
    
    /**
     * Busca perfis cuja descrição contenha o termo especificado, com suporte a paginação.
     * Esta versão paginada facilita a implementação de interfaces com grande volume de dados.
     * 
     * @param descricao Termo de busca para a descrição do perfil
     * @param pageable Objeto com informações de paginação (página, tamanho, ordenação)
     * @return Página de perfis que contêm o termo na descrição
     */
    @Query("SELECT p FROM Perfil p WHERE LOWER(p.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    @NonNull
    Page<Perfil> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao, @NonNull Pageable pageable);
    
    /**
     * Lista todos os perfis com suporte a paginação.
     * Mantém consistência com outros repositórios e facilita interfaces padronizadas.
     * 
     * @param pageable Objeto com informações de paginação
     * @return Página de perfis
     */
    @Override
    @NonNull
    Page<Perfil> findAll(@NonNull Pageable pageable);
    
    /**
     * Lista perfis que possuem uma determinada funcionalidade associada.
     * Útil para identificar quais perfis têm acesso a uma funcionalidade específica.
     * 
     * @param funcionalidade A funcionalidade para buscar perfis associados
     * @return Lista de perfis que possuem a funcionalidade especificada
     */
    @Query("SELECT p FROM Perfil p JOIN p.funcionalidades f WHERE f = :funcionalidade ORDER BY p.descricao ASC")
    List<Perfil> findByFuncionalidade(@Param("funcionalidade") Funcionalidade funcionalidade);
    
    /**
     * Lista perfis que possuem uma determinada funcionalidade, usando o ID diretamente.
     * Evita a necessidade de carregar a entidade Funcionalidade completa.
     * 
     * @param funcionalidadeId O ID da funcionalidade para buscar perfis associados
     * @return Lista de perfis que possuem a funcionalidade especificada
     */
    @Query("SELECT p FROM Perfil p JOIN p.funcionalidades f WHERE f.id = :funcionalidadeId ORDER BY p.descricao ASC")
    List<Perfil> findByFuncionalidadeId(@Param("funcionalidadeId") Long funcionalidadeId);
    
    /**
     * Verifica se um perfil possui uma determinada funcionalidade.
     * Método útil para validações rápidas de permissão.
     * 
     * @param perfilId O ID do perfil a ser verificado
     * @param funcionalidadeId O ID da funcionalidade a ser verificada
     * @return true se o perfil possuir a funcionalidade, false caso contrário
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Perfil p JOIN p.funcionalidades f " +
           "WHERE p.id = :perfilId AND f.id = :funcionalidadeId")
    boolean hasPermission(@Param("perfilId") Long perfilId, @Param("funcionalidadeId") Long funcionalidadeId);
    
    /**
     * Conta quantos usuários têm cada perfil.
     * Útil para relatórios administrativos e análise de segurança.
     * 
     * @return Lista de arrays contendo [ID do perfil, descrição, quantidade de usuários]
     */
    @Query("SELECT p.id, p.descricao, COUNT(u) FROM Perfil p LEFT JOIN Usuario u ON u.perfil.id = p.id " +
           "GROUP BY p.id, p.descricao ORDER BY COUNT(u) DESC")
    List<Object[]> countUsuariosByPerfil();
    
    /**
     * Lista perfis que não têm nenhum usuário associado.
     * Útil para identificar perfis que podem ser removidos.
     * 
     * @return Lista de perfis sem usuários
     */
    @Query("SELECT p FROM Perfil p WHERE NOT EXISTS (SELECT 1 FROM Usuario u WHERE u.perfil = p)")
    List<Perfil> findPerfisWithoutUsuarios();
}