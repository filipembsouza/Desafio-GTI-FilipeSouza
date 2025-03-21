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
 * Fornece métodos para realizar operações de banco de dados relacionadas a Custodiados.
 */
@Repository
public interface CustodiadoRepository extends JpaRepository<Custodiado, Long> {
    
    /**
     * Busca um custodiado pelo número de prontuário.
     * 
     * @param numeroProntuario O número de prontuário a ser buscado
     * @return O custodiado encontrado, ou vazio se não existir
     */
    Optional<Custodiado> findByNumeroProntuario(String numeroProntuario);
    
    /**
     * Busca um custodiado pela pessoa associada.
     * 
     * @param pessoa A entidade Pessoa associada ao custodiado
     * @return O custodiado encontrado, ou vazio se não existir
     */
    Optional<Custodiado> findByPessoa(Pessoa pessoa);
    
    /**
     * Busca um custodiado diretamente pelo ID da pessoa associada.
     * Este método simplifica a busca quando só temos o ID da pessoa,
     * evitando a necessidade de buscar a entidade Pessoa completa antes.
     * 
     * @param pessoaId O ID da pessoa associada ao custodiado
     * @return O custodiado encontrado, ou vazio se não existir
     */
    Optional<Custodiado> findByPessoaId(Long pessoaId);
    
    /**
     * Lista todos os custodiados de uma determinada unidade penal.
     * ordenados pelo nome da pessoa associada.
     * 
     * @param unidadePenal A unidade penal para filtrar os custodiados
     * @return Lista de custodiados da unidade penal especificada
     */
    List<Custodiado> findByUnidadePenalOrderByPessoaNomeAsc(UnidadePenal unidadePenal);
    
    /**
     * Lista todos os custodiados de uma determinada unidade penal, usando o ID diretamente.
     * 
     * @param unidadePenalId O ID da unidade penal
     * @return Lista de custodiados da unidade penal especificada
     */
    @Query("SELECT c FROM Custodiado c WHERE c.unidadePenal.id = :unidadePenalId ORDER BY c.pessoa.nome ASC")
    List<Custodiado> findByUnidadePenal(@Param("unidadePenalId") Long unidadePenalId);
    
    /**
     * Lista todos os custodiados de uma determinada unidade penal com suporte a paginação.
     * Esta versão paginada é útil quando há muitos custodiados em uma unidade,
     * permitindo carregar os dados em partes para melhor desempenho.
     * 
     * @param unidadePenal A unidade penal para filtrar os custodiados
     * @param pageable Objeto com informações de paginação (página, tamanho, ordenação)
     * @return Página de custodiados da unidade penal especificada
     */
    @Query("SELECT c FROM Custodiado c WHERE c.unidadePenal = :unidadePenal ORDER BY c.pessoa.nome ASC")
    Page<Custodiado> findByUnidadePenal(@Param("unidadePenal") UnidadePenal unidadePenal, Pageable pageable);
    
    /**
     * Conta o número de custodiados em uma unidade penal específica.
     * 
     * @param unidadePenalId ID da unidade penal
     * @return Número de custodiados na unidade penal
     */
    long countByUnidadePenalId(Long unidadePenalId);
    
    /**
     * Conta o número de custodiados agrupados por unidade penal.
     * 
     * @return Lista de arrays contendo [id da unidade, nome da unidade, quantidade]
     */
    @Query("SELECT c.unidadePenal.id, c.unidadePenal.nome, COUNT(c) FROM Custodiado c GROUP BY c.unidadePenal.id, c.unidadePenal.nome")
    List<Object[]> countByUnidadePenal();
    
    /**
     * Busca custodiados cujo vulgo (apelido) contenha o termo especificado.
     * Esta consulta ignora maiúsculas/minúsculas para facilitar a busca.
     * Os resultados são retornados ordenados pelo nome da pessoa
     * 
     * @param vulgo Termo de busca para o vulgo
     * @return Lista de custodiados que atendem ao critério
     */
    @Query("SELECT c FROM Custodiado c WHERE LOWER(c.vulgo) LIKE LOWER(CONCAT('%', :vulgo, '%')) ORDER BY c.pessoa.nome ASC")
    List<Custodiado> findByVulgoContainingIgnoreCase(@Param("vulgo") String vulgo);
    
    /**
     * Busca custodiados cujo vulgo (apelido) contenha o termo especificado, com suporte a paginação.
     * 
     * @param vulgo Termo de busca para o vulgo
     * @param pageable Objeto com informações de paginação
     * @return Página de custodiados que atendem ao critério
     */
    @Query("SELECT c FROM Custodiado c WHERE LOWER(c.vulgo) LIKE LOWER(CONCAT('%', :vulgo, '%'))")
    Page<Custodiado> findByVulgoContainingIgnoreCase(@Param("vulgo") String vulgo, Pageable pageable);

    /**
     * Busca custodiados pelo nome, ignorando maiúsculas e minúsculas.
     * Os resultados são ordenados pelo nome da pessoa.
     *
     * @param nome Nome ou parte do nome do custodiado a ser buscado
     * @return Lista de custodiados que possuem o nome ou parte dele
     */
    @Query("SELECT c FROM Custodiado c WHERE LOWER(c.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY c.pessoa.nome ASC")
    List<Custodiado> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca custodiados pelo nome com suporte a paginação.
     * 
     * @param nome Nome ou parte do nome do custodiado
     * @param pageable Objeto com informações de paginação
     * @return Página de custodiados que possuem o nome ou parte dele
     */
    @Query("SELECT c FROM Custodiado c WHERE LOWER(c.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Custodiado> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Busca avançada de custodiados por múltiplos critérios combinados.
     * Este método permite realizar pesquisas complexas usando diferentes filtros
     * simultaneamente. Os parâmetros são opcionais - quando null, não são aplicados
     * como filtro.
     * 
     * Especialmente útil para telas de pesquisa avançada no sistema.
     * 
     * @param nome Nome ou parte do nome (opcional)
     * @param vulgo Vulgo ou parte do vulgo (opcional)
     * @param numeroProntuario Número do prontuário ou parte dele (opcional)
     * @param unidadePenalId ID da unidade penal (opcional)
     * @param pageable Objeto com informações de paginação
     * @return Página de custodiados que atendem a todos os critérios fornecidos
     */
    @Query("SELECT c FROM Custodiado c WHERE " +
           "(:nome IS NULL OR LOWER(c.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:vulgo IS NULL OR LOWER(c.vulgo) LIKE LOWER(CONCAT('%', :vulgo, '%'))) AND " +
           "(:numeroProntuario IS NULL OR LOWER(c.numeroProntuario) LIKE LOWER(CONCAT('%', :numeroProntuario, '%'))) AND " +
           "(:unidadePenalId IS NULL OR c.unidadePenal.id = :unidadePenalId)")
    Page<Custodiado> buscarPorMultiplosCriterios(
            @Param("nome") String nome,
            @Param("vulgo") String vulgo,
            @Param("numeroProntuario") String numeroProntuario,
            @Param("unidadePenalId") Long unidadePenalId,
            Pageable pageable);
    
    /**
     * Lista custodiados que têm agendamentos ativos (não cancelados).
     * 
     * @return Lista de custodiados com agendamentos ativos
     */
    @Query("SELECT DISTINCT c FROM Custodiado c JOIN AgendamentoVisita a ON a.custodiado.id = c.id " +
           "WHERE a.status.descricao <> 'CANCELADO' ORDER BY c.pessoa.nome ASC")
    List<Custodiado> findWithActiveAgendamentos();
    
    /**
     * Verifica se existem custodiados com o número de prontuário fornecido.
     * 
     * @param numeroProntuario O número de prontuário a ser verificado
     * @return true se existir um custodiado com o prontuário informado, false caso contrário
     */
    boolean existsByNumeroProntuario(String numeroProntuario);
    
    /**
     * Conta o número de agendamentos ativos por custodiado.
     * 
     * @return Lista de arrays com [id do custodiado, nome, número de agendamentos]
     */
    @Query("SELECT c.id, c.pessoa.nome, COUNT(a) FROM Custodiado c LEFT JOIN AgendamentoVisita a ON a.custodiado.id = c.id " +
           "AND a.status.descricao <> 'CANCELADO' GROUP BY c.id, c.pessoa.nome ORDER BY COUNT(a) DESC")
    List<Object[]> countAgendamentosByCustodiado();
}