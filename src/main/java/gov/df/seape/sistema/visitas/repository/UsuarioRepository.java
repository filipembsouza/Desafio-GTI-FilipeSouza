package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Perfil;
import gov.df.seape.sistema.visitas.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repositório para a entidade Usuario.
 * Fornece métodos para realizar operações de banco de dados relacionadas a Usuários.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    /**
     * Busca um usuário pelo email.
     * Método essencial para o processo de autenticação.
     * 
     * @param email O email do usuário a ser buscado
     * @return O usuário encontrado, ou vazio se não existir
     */
    Optional<Usuario> findByEmail(String email);
    
    /**
     * Verifica se existe um usuário com o email fornecido.
     * Útil para validações durante o cadastro de novos usuários.
     * 
     * @param email O email a ser verificado
     * @return true se existir um usuário com o email informado, false caso contrário
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuários pelo perfil associado.
     * Permite listar todos os usuários com um determinado perfil.
     * 
     * @param perfil O perfil para filtrar os usuários
     * @return Lista de usuários com o perfil especificado
     */
    List<Usuario> findByPerfil(Perfil perfil);
    
    /**
     * Busca usuários pelo perfil associado, com suporte a paginação.
     * 
     * @param perfil O perfil para filtrar os usuários
     * @param pageable Objeto com informações de paginação
     * @return Página de usuários com o perfil especificado
     */
    Page<Usuario> findByPerfil(Perfil perfil, Pageable pageable);
    
    /**
     * Busca usuários pelo ID do perfil diretamente.
     * Evita a necessidade de carregar a entidade Perfil completa antes.
     * 
     * @param perfilId O ID do perfil para filtrar os usuários
     * @return Lista de usuários com o perfil especificado
     */
    @Query("SELECT u FROM Usuario u JOIN u.perfil p WHERE p.id = :perfilId")
    List<Usuario> findByPerfilId(@Param("perfilId") Long perfilId);
    
    /**
     * Busca usuários pelo ID do perfil, com suporte a paginação.
     * 
     * @param perfilId O ID do perfil para filtrar os usuários
     * @param pageable Objeto com informações de paginação
     * @return Página de usuários com o perfil especificado
     */
    @Query("SELECT u FROM Usuario u JOIN u.perfil p WHERE p.id = :perfilId")
    Page<Usuario> findByPerfilId(@Param("perfilId") Long perfilId, Pageable pageable);
    
    /**
     * Busca usuários pelo ID da pessoa associada.
     * 
     * @param pessoaId O ID da pessoa associada ao usuário
     * @return O usuário encontrado, ou vazio se não existir
     */
    Optional<Usuario> findByPessoaId(Long pessoaId);
    
    /**
     * Busca usuários cujo nome da pessoa contenha o termo especificado.
     * Útil para campos de pesquisa em interfaces administrativas.
     * 
     * @param nome Termo de busca para o nome da pessoa
     * @return Lista de usuários que contêm o termo no nome da pessoa
     */
    @Query("SELECT u FROM Usuario u JOIN u.pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY p.nome ASC")
    List<Usuario> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca usuários cujo nome da pessoa contenha o termo especificado, com suporte a paginação.
     * 
     * @param nome Termo de busca para o nome da pessoa
     * @param pageable Objeto com informações de paginação
     * @return Página de usuários que contêm o termo no nome da pessoa
     */
    @Query("SELECT u FROM Usuario u JOIN u.pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Usuario> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Busca avançada de usuários por múltiplos critérios combinados.
     * Os parâmetros são opcionais - quando null, não são aplicados como filtro.
     * 
     * @param nome Nome ou parte do nome da pessoa (opcional)
     * @param email Email ou parte do email (opcional)
     * @param perfilId ID do perfil (opcional)
     * @param pageable Objeto com informações de paginação
     * @return Página de usuários que atendem a todos os critérios fornecidos
     */
    @Query("SELECT DISTINCT u FROM Usuario u JOIN u.pessoa p WHERE " +
           "(:nome IS NULL OR LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:email IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
           "(:perfilId IS NULL OR u.perfil.id = :perfilId)")
    Page<Usuario> buscarPorMultiplosCriterios(
            @Param("nome") String nome,
            @Param("email") String email,
            @Param("perfilId") Long perfilId,
            Pageable pageable);
    
    /**
     * Lista usuários com um determinado conjunto de perfis.
     * Útil para localizar usuários que tenham qualquer um dos perfis especificados.
     * 
     * @param perfilIds Conjunto de IDs de perfis
     * @param pageable Objeto com informações de paginação
     * @return Página de usuários que possuem qualquer um dos perfis informados
     */
    @Query("SELECT DISTINCT u FROM Usuario u WHERE u.perfil.id IN :perfilIds")
    Page<Usuario> findByPerfilIdIn(@Param("perfilIds") Set<Long> perfilIds, Pageable pageable);
    
    /**
     * Verifica se um usuário tem um determinado perfil.
     * Método útil para validações rápidas de acesso.
     * 
     * @param usuarioId O ID do usuário a ser verificado
     * @param perfilId O ID do perfil a ser verificado
     * @return true se o usuário possui o perfil, false caso contrário
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Usuario u " +
           "WHERE u.id = :usuarioId AND u.perfil.id = :perfilId")
    boolean hasPerfil(@Param("usuarioId") Long usuarioId, @Param("perfilId") Long perfilId);
    
    /**
     * Verifica se um usuário tem acesso a uma determinada funcionalidade.
     * Este método verifica se o perfil do usuário possui a funcionalidade.
     * 
     * @param usuarioId O ID do usuário a ser verificado
     * @param funcionalidadeId O ID da funcionalidade a ser verificada
     * @return true se o usuário tem acesso à funcionalidade, false caso contrário
     */
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Usuario u " +
           "JOIN u.perfil p JOIN VincPerfilFuncionalidade v ON v.perfil.id = p.id " +
           "WHERE u.id = :usuarioId AND v.funcionalidade.id = :funcionalidadeId")
    boolean hasAcessoAFuncionalidade(
            @Param("usuarioId") Long usuarioId, 
            @Param("funcionalidadeId") Long funcionalidadeId);
    
    /**
     * Conta usuários por perfil.
     * Útil para estatísticas de segurança e relatórios administrativos.
     * 
     * @return Lista contendo perfil e quantidade de usuários
     */
    @Query("SELECT p.descricao AS perfil, COUNT(u) AS quantidade FROM Usuario u " +
           "JOIN u.perfil p GROUP BY p.descricao ORDER BY COUNT(u) DESC")
    List<Object[]> contarUsuariosPorPerfil();
    
    /**
     * Busca usuários por data de último acesso.
     * Útil para identificar usuários inativos ou para auditoria.
     * 
     * @param dataInicio Data de início do período
     * @param dataFim Data de fim do período
     * @param pageable Objeto com informações de paginação
     * @return Página de usuários que acessaram o sistema no período especificado
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "u.ultimoAcesso BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY u.ultimoAcesso DESC")
    Page<Usuario> findByUltimoAcessoBetween(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            Pageable pageable);
    
    /**
     * Busca usuários que nunca acessaram o sistema ou não acessam há muito tempo.
     * Útil para identificar contas potencialmente inativas.
     * 
     * @param dataLimite Data limite para considerar um usuário como inativo
     * @param pageable Objeto com informações de paginação
     * @return Página de usuários considerados inativos
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "u.ultimoAcesso IS NULL OR u.ultimoAcesso < :dataLimite " +
           "ORDER BY u.ultimoAcesso ASC NULLS FIRST")
    Page<Usuario> findUsuariosInativos(
            @Param("dataLimite") LocalDateTime dataLimite,
            Pageable pageable);
}