package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.model.Perfil;
import gov.df.seape.sistema.visitas.model.VincPerfilFuncionalidade;
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
 * Repositório para operações de persistência e consulta da entidade VincPerfilFuncionalidade.
 * Gerencia os vínculos entre perfis e funcionalidades.
 */
@Repository
public interface VincPerfilFuncionalidadeRepository extends JpaRepository<VincPerfilFuncionalidade, Long> {
    
    /**
     * Lista vínculos para um determinado perfil.
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.perfil = :perfil ORDER BY v.funcionalidade.descricao ASC")
    List<VincPerfilFuncionalidade> findByPerfil(@Param("perfil") Perfil perfil);
    
    /**
     * Lista vínculos para um determinado perfil usando ID.
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId ORDER BY v.funcionalidade.descricao ASC")
    List<VincPerfilFuncionalidade> findByPerfilId(@Param("perfilId") Long perfilId);
    
    /**
     * Busca vínculo específico entre perfil e funcionalidade.
     */
    Optional<VincPerfilFuncionalidade> findByPerfilAndFuncionalidade(Perfil perfil, Funcionalidade funcionalidade);
    
    /**
     *  **Novo método: Verifica se existe um vínculo entre perfil e funcionalidade.**
     */
    boolean existsByPerfilAndFuncionalidade(Perfil perfil, Funcionalidade funcionalidade);
    
    /**
     * Verifica se existe um vínculo entre perfil e funcionalidade usando ID.
     */
    boolean existsByPerfilIdAndFuncionalidadeId(Long perfilId, Long funcionalidadeId);
    
    /**
     * Lista todos os vínculos ordenados por perfil e funcionalidade.
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v ORDER BY v.perfil.descricao, v.funcionalidade.descricao")
    List<VincPerfilFuncionalidade> findAllOrderByPerfilAndFuncionalidade();
    
    /**
     * Lista vínculos pelo ID da funcionalidade.
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.funcionalidade.id = :funcionalidadeId ORDER BY v.perfil.descricao")
    List<VincPerfilFuncionalidade> findByFuncionalidadeId(@Param("funcionalidadeId") Long funcionalidadeId);
    
    /**
     * Remove todos os vínculos de um perfil.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId")
    void deleteByPerfilId(@Param("perfilId") Long perfilId);
    
    /**
     * Remove vínculos específicos entre um perfil e funcionalidades.
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId AND v.funcionalidade.id IN (:funcionalidadeIds)")
    void deleteByPerfilIdAndFuncionalidadeIdIn(
        @Param("perfilId") Long perfilId, 
        @Param("funcionalidadeIds") Set<Long> funcionalidadeIds
    );
}
