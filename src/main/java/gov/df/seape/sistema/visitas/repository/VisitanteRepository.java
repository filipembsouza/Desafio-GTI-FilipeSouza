package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.model.Visitante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade Visitante.
 * Fornece métodos para realizar operações de banco de dados relacionadas a Visitantes.
 */
@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    
    /**
     * Busca um visitante pela pessoa associada.
     * 
     * @param pessoa A entidade Pessoa associada ao visitante
     * @return O visitante encontrado, ou vazio se não existir
     */
    Optional<Visitante> findByPessoa(Pessoa pessoa);

    /**
     * Busca um visitante pelo ID da pessoa associada.
     * 
     * @param pessoaId O ID da pessoa associada ao visitante
     * @return O visitante encontrado, ou vazio se não existir
     */
    @Query("SELECT v FROM Visitante v WHERE v.pessoa.id = :pessoaId")
    Optional<Visitante> findByPessoaId(@Param("pessoaId") Long pessoaId);
    
    /**
     * Busca um visitante pelo CPF da pessoa associada.
     * Método importante para validações e consultas rápidas por documento.
     * 
     * @param cpf O CPF exato a ser buscado
     * @return O visitante encontrado, ou vazio se não existir
     */
    @Query("SELECT v FROM Visitante v WHERE v.pessoa.cpf = :cpf")
    Optional<Visitante> findByCpf(@Param("cpf") String cpf);

    /**
     * Busca visitantes pelo nome da pessoa associada, ignorando maiúsculas e minúsculas.
     * Os resultados são ordenados pelo nome da pessoa.
     *
     * @param nome Nome ou parte do nome do visitante a ser buscado
     * @return Lista de visitantes que possuem o nome ou parte dele
     */
    @Query("SELECT v FROM Visitante v WHERE LOWER(v.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY v.pessoa.nome ASC")
    List<Visitante> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca visitantes pelo nome da pessoa associada, com suporte a paginação.
     * Esta versão paginada é útil para interfaces que precisam mostrar resultados
     * em blocos gerenciáveis, melhorando o desempenho e a experiência do usuário.
     *
     * @param nome Nome ou parte do nome do visitante
     * @param pageable Objeto com informações de paginação (página, tamanho, ordenação)
     * @return Página de visitantes que possuem o nome ou parte dele
     */
    @Query("SELECT v FROM Visitante v WHERE LOWER(v.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Visitante> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Busca avançada de visitantes por múltiplos critérios combinados.
     * Este método permite realizar pesquisas complexas usando diferentes filtros
     * simultaneamente. Os parâmetros são opcionais - quando null, não são aplicados.
     * 
     * Ideal para telas de pesquisa com múltiplos campos de filtro.
     * 
     * @param nome Nome ou parte do nome da pessoa (opcional)
     * @param cpf CPF exato da pessoa (opcional)
     * @param dataNascimento Data de nascimento exata da pessoa (opcional)
     * @param pageable Objeto com informações de paginação
     * @return Página de visitantes que atendem a todos os critérios fornecidos
     */
    @Query("SELECT v FROM Visitante v WHERE " +
           "(:nome IS NULL OR LOWER(v.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:cpf IS NULL OR v.pessoa.cpf = :cpf) AND " +
           "(:dataNascimento IS NULL OR v.pessoa.dataNascimento = :dataNascimento)")
    Page<Visitante> buscarPorMultiplosCriterios(
            @Param("nome") String nome,
            @Param("cpf") String cpf,
            @Param("dataNascimento") LocalDate dataNascimento,
            Pageable pageable);
    
    /**
     * Lista visitantes que possuem agendamentos em uma determinada data.
     * Útil para relatórios e controle de visitas diárias.
     * 
     * @param data A data específica para verificar agendamentos
     * @return Lista de visitantes com agendamentos na data especificada
     */
    @Query("SELECT DISTINCT v FROM Visitante v JOIN AgendamentoVisita a ON v.id = a.visitante.id " +
           "WHERE CAST(a.dataHoraAgendamento AS LocalDate) = :data AND a.status.descricao <> 'CANCELADO'")
    List<Visitante> findComAgendamentoNaData(@Param("data") LocalDate data);
    
    /**
     * Lista visitantes que realizaram visitas a um determinado custodiado.
     * Permite verificar o histórico de quem visitou um determinado detento.
     * 
     * @param custodiadoId ID do custodiado para filtrar as visitas
     * @return Lista de visitantes que visitaram o custodiado especificado
     */
    @Query("SELECT DISTINCT v FROM Visitante v JOIN AgendamentoVisita a ON v.id = a.visitante.id " +
           "WHERE a.custodiado.id = :custodiadoId AND a.status.descricao <> 'CANCELADO'")
    List<Visitante> findVisitantesPorCustodiado(@Param("custodiadoId") Long custodiadoId);
    
    /**
     * Conta o número de visitas realizadas por um visitante em um período.
     * Útil para relatórios estatísticos e controle de frequência.
     * 
     * @param visitanteId ID do visitante
     * @param inicio Data e hora de início do período
     * @param fim Data e hora de fim do período
     * @return Número de visitas realizadas no período
     */
    @Query("SELECT COUNT(a) FROM AgendamentoVisita a " +
           "WHERE a.visitante.id = :visitanteId AND " +
           "a.dataHoraAgendamento BETWEEN :inicio AND :fim AND " +
           "a.status.descricao <> 'CANCELADO'")
    long contarVisitasNoPeriodo(
            @Param("visitanteId") Long visitanteId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);
}