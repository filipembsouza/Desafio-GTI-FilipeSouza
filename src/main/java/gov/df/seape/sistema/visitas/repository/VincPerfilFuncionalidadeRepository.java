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
 * Repositório para operações de persistência e consulta da entidade VincPerfilFuncionalidade.
 * Gerencia os vínculos entre perfis e funcionalidades.
 */
@Repository
public interface VincPerfilFuncionalidadeRepository extends JpaRepository<VincPerfilFuncionalidade, Long> {
    
    /**
     * Lista vínculos para um determinado perfil.
     * 
     * @param perfil Perfil para buscar vínculos
     * @return Lista de vínculos do perfil
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.perfil = :perfil ORDER BY v.funcionalidade.descricao ASC")
    List<VincPerfilFuncionalidade> findByPerfil(@Param("perfil") Perfil perfil);
    
    /**
     * Lista vínculos para um determinado perfil usando ID.
     * 
     * @param perfilId ID do perfil
     * @return Lista de vínculos do perfil
     */
    @Query("SELECT v FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId ORDER BY v.funcionalidade.descricao ASC")
    List<VincPerfilFuncionalidade> findByPerfilId(@Param("perfilId") Long perfilId);
    
    /**
     * Busca vínculo específico entre perfil e funcionalidade.
     * 
     * @param perfil Perfil
     * @param funcionalidade Funcionalidade
     * @return Optional do vínculo encontrado
     */
    Optional<VincPerfilFuncionalidade> findByPerfilAndFuncionalidade(Perfil perfil, Funcionalidade funcionalidade);
    
    /**
     * Verifica se existe um vínculo entre perfil e funcionalidade.
     * 
     * @param perfil Perfil
     * @param funcionalidade Funcionalidade
     * @return true se o vínculo existir, false caso contrário
     */
    boolean existsByPerfilAndFuncionalidade(Perfil perfil, Funcionalidade funcionalidade);
    
    /**
     * Remove todos os vínculos de um perfil.
     * 
     * @param perfilId ID do perfil
     * @return Número de vínculos removidos
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId")
    int deleteByPerfilId(@Param("perfilId") Long perfilId);
    
    /**
     * Remove vínculos específicos entre um perfil e funcionalidades.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeIds IDs das funcionalidades
     * @return Número de vínculos removidos
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM VincPerfilFuncionalidade v WHERE v.perfil.id = :perfilId AND v.funcionalidade.id IN :funcionalidadeIds")
    int deleteByPerfilIdAndFuncionalidadeIdIn(
        @Param("perfilId") Long perfilId, 
        @Param("funcionalidadeIds") Set<Long> funcionalidadeIds
    );
}