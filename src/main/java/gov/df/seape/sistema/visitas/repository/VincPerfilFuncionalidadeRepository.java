package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.model.Perfil;
import gov.df.seape.sistema.visitas.model.VincPerfilFuncionalidade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repositório para a entidade VincPerfilFuncionalidade.
 * Fornece métodos para realizar operações de banco de dados relacionadas 
 * à associação entre Perfis e Funcionalidades.
 */
@Repository
public interface VincPerfilFuncionalidadeRepository extends JpaRepository<VincPerfilFuncionalidade, Long> {
    
    /**
     * Lista todos os vínculos para um determinado perfil, ordenados alfabeticamente pela funcionalidade.
     * Permite obter todas as funcionalidades associadas a um perfil.
     * 
     * @param perfil O perfil para filtrar os vínculos
     * @return Lista de vínculos do perfil especificado, ordenados por funcionalidade
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.perfil = :perfil ORDER BY v.funcionalidade.descricao ASC")
    List<VincPerfilFuncionalidade> findByPerfil(@Param("perfil") Perfil perfil);
    
    /**
     * Lista todos os vínculos para um determinado perfil usando o ID diretamente.
     * Esta versão evita a necessidade de carregar a entidade Perfil completa.
     * 
     * @param perfilId O ID do perfil para filtrar os vínculos
     * @return Lista de vínculos do perfil especificado, ordenados por funcionalidade
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId ORDER BY v.funcionalidade.descricao ASC")
    List<VincPerfilFuncionalidade> findByPerfilId(@Param("perfilId") Long perfilId);
    
    /**
     * Lista todos os vínculos para um determinado perfil com suporte a paginação.
     * 
     * @param perfilId O ID do perfil para filtrar os vínculos
     * @param pageable Objeto com informações de paginação
     * @return Página de vínculos do perfil especificado
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId")
    Page<VincPerfilFuncionalidade> findByPerfilId(@Param("perfilId") Long perfilId, Pageable pageable);
    
    /**
     * Lista todos os vínculos para uma determinada funcionalidade, ordenados alfabeticamente pelo perfil.
     * Permite saber quais perfis possuem uma determinada funcionalidade.
     * 
     * @param funcionalidade A funcionalidade para filtrar os vínculos
     * @return Lista de vínculos da funcionalidade especificada, ordenados por perfil
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.funcionalidade = :funcionalidade ORDER BY v.perfil.descricao ASC")
    List<VincPerfilFuncionalidade> findByFuncionalidade(@Param("funcionalidade") Funcionalidade funcionalidade);
    
    /**
     * Lista todos os vínculos para uma determinada funcionalidade usando o ID diretamente.
     * 
     * @param funcionalidadeId O ID da funcionalidade para filtrar os vínculos
     * @return Lista de vínculos da funcionalidade especificada, ordenados por perfil
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.funcionalidade.id = :funcionalidadeId ORDER BY v.perfil.descricao ASC")
    List<VincPerfilFuncionalidade> findByFuncionalidadeId(@Param("funcionalidadeId") Long funcionalidadeId);
    
    /**
     * Lista todos os vínculos para uma determinada funcionalidade com suporte a paginação.
     * 
     * @param funcionalidadeId O ID da funcionalidade para filtrar os vínculos
     * @param pageable Objeto com informações de paginação
     * @return Página de vínculos da funcionalidade especificada
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.funcionalidade.id = :funcionalidadeId")
    Page<VincPerfilFuncionalidade> findByFuncionalidadeId(@Param("funcionalidadeId") Long funcionalidadeId, Pageable pageable);
    
    /**
     * Busca um vínculo específico entre um perfil e uma funcionalidade.
     * Útil para verificar se um perfil possui uma determinada funcionalidade.
     * 
     * @param perfil O perfil para buscar
     * @param funcionalidade A funcionalidade para buscar
     * @return O vínculo encontrado, ou vazio se não existir
     */
    Optional<VincPerfilFuncionalidade> findByPerfilAndFuncionalidade(Perfil perfil, Funcionalidade funcionalidade);
    
    /**
     * Busca um vínculo específico usando IDs diretamente.
     * 
     * @param perfilId O ID do perfil para buscar
     * @param funcionalidadeId O ID da funcionalidade para buscar
     * @return O vínculo encontrado, ou vazio se não existir
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId AND v.funcionalidade.id = :funcionalidadeId")
    Optional<VincPerfilFuncionalidade> findByPerfilIdAndFuncionalidadeId(
            @Param("perfilId") Long perfilId, 
            @Param("funcionalidadeId") Long funcionalidadeId);
    
    /**
     * Verifica se existe um vínculo entre um perfil e uma funcionalidade.
     * 
     * @param perfil O perfil para verificar
     * @param funcionalidade A funcionalidade para verificar
     * @return true se existir um vínculo, false caso contrário
     */
    boolean existsByPerfilAndFuncionalidade(Perfil perfil, Funcionalidade funcionalidade);
    
    /**
     * Verifica se existe um vínculo entre um perfil e uma funcionalidade usando IDs.
     * 
     * @param perfilId O ID do perfil para verificar
     * @param funcionalidadeId O ID da funcionalidade para verificar
     * @return true se existir um vínculo, false caso contrário
     */
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM VincPerfilFuncionalidade v " +
           "WHERE v.perfil.id = :perfilId AND v.funcionalidade.id = :funcionalidadeId")
    boolean existsByPerfilIdAndFuncionalidadeId(
            @Param("perfilId") Long perfilId, 
            @Param("funcionalidadeId") Long funcionalidadeId);

    /**
     * Lista todos os vínculos entre Perfis e Funcionalidades,
     * ordenados alfabeticamente pelo perfil e pela funcionalidade.
     *
     * @return Lista de todos os vínculos existentes no sistema, ordenados por perfil e funcionalidade
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v ORDER BY v.perfil.descricao ASC, v.funcionalidade.descricao ASC")
    List<VincPerfilFuncionalidade> findAllOrderByPerfilAndFuncionalidade();
    
    /**
     * Lista todos os vínculos com suporte a paginação, mantendo a ordenação por perfil e funcionalidade.
     * 
     * @param pageable Objeto com informações de paginação
     * @return Página de vínculos
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v ORDER BY v.perfil.descricao ASC, v.funcionalidade.descricao ASC")
    Page<VincPerfilFuncionalidade> findAllOrderByPerfilAndFuncionalidade(Pageable pageable);
    
    /**
     * Remove todos os vínculos de um perfil específico.
     * Útil para redefinir as permissões de um perfil ou antes de excluí-lo.
     * 
     * @param perfilId O ID do perfil para remover os vínculos
     * @return O número de vínculos removidos
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId")
    int deleteByPerfilId(@Param("perfilId") Long perfilId);
    
    /**
     * Remove todos os vínculos de uma funcionalidade específica.
     * Útil antes de excluir uma funcionalidade do sistema.
     * 
     * @param funcionalidadeId O ID da funcionalidade para remover os vínculos
     * @return O número de vínculos removidos
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM VincPerfilFuncionalidade v WHERE v.funcionalidade.id = :funcionalidadeId")
    int deleteByFuncionalidadeId(@Param("funcionalidadeId") Long funcionalidadeId);
    
    /**
     * Remove vínculos específicos entre um perfil e múltiplas funcionalidades.
     * Útil para revogar vários acessos de uma vez.
     * 
     * @param perfilId O ID do perfil
     * @param funcionalidadeIds Lista de IDs das funcionalidades a serem desvinculadas
     * @return O número de vínculos removidos
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId AND v.funcionalidade.id IN :funcionalidadeIds")
    int deleteByPerfilIdAndFuncionalidadeIdIn(
            @Param("perfilId") Long perfilId, 
            @Param("funcionalidadeIds") Set<Long> funcionalidadeIds);
    
    /**
     * Conta quantos perfis têm uma determinada funcionalidade.
     * Útil para verificar o impacto de remover uma funcionalidade.
     * 
     * @param funcionalidadeId O ID da funcionalidade
     * @return O número de perfis associados à funcionalidade
     */
    @Query("SELECT COUNT(DISTINCT v.perfil.id) FROM VincPerfilFuncionalidade v WHERE v.funcionalidade.id = :funcionalidadeId")
    long countPerfisByFuncionalidadeId(@Param("funcionalidadeId") Long funcionalidadeId);
    
    /**
     * Conta quantas funcionalidades um perfil possui.
     * Útil para análise de permissões e verificações de segurança.
     * 
     * @param perfilId O ID do perfil
     * @return O número de funcionalidades associadas ao perfil
     */
    @Query("SELECT COUNT(DISTINCT v.funcionalidade.id) FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId")
    long countFuncionalidadesByPerfilId(@Param("perfilId") Long perfilId);
}