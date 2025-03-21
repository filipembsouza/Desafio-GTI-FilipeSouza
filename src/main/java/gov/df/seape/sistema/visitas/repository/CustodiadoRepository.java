package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Custodiado;
import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.model.UnidadePenal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade Custodiado.
 */
@Repository
public interface CustodiadoRepository extends JpaRepository<Custodiado, Long> {
    
    /**
     * Busca um custodiado pelo número de prontuário.
     * 
     * @param numeroProntuario Número de prontuário a ser buscado
     * @return O custodiado encontrado ou vazio
     */
    Optional<Custodiado> findByNumeroProntuario(String numeroProntuario);
    
    /**
     * Busca um custodiado pela pessoa associada.
     * 
     * @param pessoa Pessoa associada ao custodiado
     * @return O custodiado encontrado ou vazio
     */
    Optional<Custodiado> findByPessoa(Pessoa pessoa);
    
    /**
     * Busca um custodiado pelo ID da pessoa associada.
     * 
     * @param pessoaId ID da pessoa associada ao custodiado
     * @return O custodiado encontrado ou vazio
     */
    Optional<Custodiado> findByPessoaId(Long pessoaId);
    
    /**
     * Lista todos os custodiados de uma determinada unidade penal.
     * 
     * @param unidadePenal Unidade penal a ser filtrada
     * @return Lista de custodiados da unidade penal especificada
     */
    List<Custodiado> findByUnidadePenal(UnidadePenal unidadePenal);
    
    /**
     * Lista todos os custodiados de uma determinada unidade penal por ID.
     * 
     * @param unidadePenalId ID da unidade penal a ser filtrada
     * @return Lista de custodiados da unidade penal especificada
     */
    @Query("SELECT c FROM Custodiado c WHERE c.unidadePenal.id = :unidadePenalId")
    List<Custodiado> findByUnidadePenalId(@Param("unidadePenalId") Long unidadePenalId);
    
    /**
     * Lista todos os custodiados de uma determinada unidade penal com paginação.
     * 
     * @param unidadePenalId ID da unidade penal a ser filtrada
     * @param pageable Informações de paginação
     * @return Página de custodiados da unidade penal especificada
     */
    @Query("SELECT c FROM Custodiado c WHERE c.unidadePenal.id = :unidadePenalId")
    Page<Custodiado> findByUnidadePenalId(@Param("unidadePenalId") Long unidadePenalId, Pageable pageable);
    
    /**
     * Busca custodiados pelo vulgo, ignorando maiúsculas e minúsculas.
     * 
     * @param vulgo Vulgo ou parte do vulgo a ser buscado
     * @return Lista de custodiados que contêm o vulgo especificado
     */
    @Query("SELECT c FROM Custodiado c WHERE LOWER(c.vulgo) LIKE LOWER(CONCAT('%', :vulgo, '%')) ORDER BY c.pessoa.nome ASC")
    List<Custodiado> findByVulgoContainingIgnoreCase(@Param("vulgo") String vulgo);
    
    /**
     * Busca custodiados pelo vulgo, ignorando maiúsculas e minúsculas, com paginação.
     * 
     * @param vulgo Vulgo ou parte do vulgo a ser buscado
     * @param pageable Informações de paginação
     * @return Página de custodiados que contêm o vulgo especificado
     */
    @Query("SELECT c FROM Custodiado c WHERE LOWER(c.vulgo) LIKE LOWER(CONCAT('%', :vulgo, '%'))")
    Page<Custodiado> findByVulgoContainingIgnoreCase(@Param("vulgo") String vulgo, Pageable pageable);
    
    /**
     * Busca custodiados pelo nome, ignorando maiúsculas e minúsculas.
     * 
     * @param nome Nome ou parte do nome a ser buscado
     * @return Lista de custodiados que contêm o nome especificado
     */
    @Query("SELECT c FROM Custodiado c WHERE LOWER(c.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY c.pessoa.nome ASC")
    List<Custodiado> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca custodiados pelo nome, ignorando maiúsculas e minúsculas, com paginação.
     * 
     * @param nome Nome ou parte do nome a ser buscado
     * @param pageable Informações de paginação
     * @return Página de custodiados que contêm o nome especificado
     */
    @Query("SELECT c FROM Custodiado c WHERE LOWER(c.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Custodiado> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Conta o número de custodiados por unidade penal.
     * 
     * @return Lista contendo unidade penal e contagem de custodiados
     */
    @Query("SELECT c.unidadePenal.nome as unidade, COUNT(c) as quantidade FROM Custodiado c GROUP BY c.unidadePenal.nome ORDER BY COUNT(c) DESC")
    List<Object[]> countByUnidadePenal();
    
    /**
     * Conta o número de custodiados em uma unidade penal específica.
     * 
     * @param unidadePenalId ID da unidade penal
     * @return Número de custodiados na unidade penal
     */
    @Query("SELECT COUNT(c) FROM Custodiado c WHERE c.unidadePenal.id = :unidadePenalId")
    long countByUnidadePenalId(@Param("unidadePenalId") Long unidadePenalId);
}
