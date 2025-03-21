package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório para a entidade Pessoa.
 * Fornece métodos para realizar operações de banco de dados relacionadas a Pessoas.
 */
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
    /**
     * Verifica se existe uma pessoa com o CPF fornecido.
     * 
     * @param cpf O CPF a ser verificado
     * @return true se existir uma pessoa com o CPF informado, false caso contrário
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Busca uma pessoa pelo CPF.
     * 
     * @param cpf O CPF a ser buscado
     * @return A pessoa encontrada, ou vazia se não existir
     */
    Optional<Pessoa> findByCpf(String cpf);
    
    /**
     * Busca pessoas pelo nome, ignorando maiúsculas e minúsculas.
     * Útil para campos de pesquisa onde o usuário digita parte do nome.
     * 
     * @param nome Parte do nome a ser buscada
     * @return Lista de pessoas que contêm o nome especificado
     */
    @Query("SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Pessoa> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca pessoas pelo nome com suporte a paginação.
     * Útil para exibir resultados em páginas quando há muitos registros.
     * 
     * @param nome Parte do nome a ser buscada
     * @param pageable Configuração de paginação (página, tamanho, ordenação)
     * @return Página de pessoas que contêm o nome especificado
     */
    @Query("SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Pessoa> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Busca pessoas por intervalo de data de nascimento.
     * Útil para relatórios e filtros por faixa etária.
     * 
     * @param dataInicio Data de início do intervalo
     * @param dataFim Data de fim do intervalo
     * @return Lista de pessoas nascidas no intervalo especificado
     */
    @Query("SELECT p FROM Pessoa p WHERE p.dataNascimento BETWEEN :dataInicio AND :dataFim")
    List<Pessoa> findByDataNascimentoBetween(
            @Param("dataInicio") LocalDate dataInicio, 
            @Param("dataFim") LocalDate dataFim);
    
    /**
     * Verifica se uma pessoa já está cadastrada como Custodiado ou Visitante.
     * Usado para validar regras como "um usuário não pode ser visitante ou custodiado ao mesmo tempo"
     * 
     * @param pessoaId ID da pessoa a ser verificada
     * @return true se a pessoa já estiver cadastrada como custodiado ou visitante
     */
    @Query("SELECT CASE WHEN (COUNT(c) + COUNT(v)) > 0 THEN true ELSE false END FROM Custodiado c, Visitante v " +
           "WHERE c.pessoa.id = :pessoaId OR v.pessoa.id = :pessoaId")
    boolean isPessoaCustodiadoOuVisitante(@Param("pessoaId") Long pessoaId);
    
    /**
     * Conta o número de pessoas por mês de nascimento.
     * Útil para relatórios estatísticos.
     * 
     * @return Lista contendo mês e quantidade de pessoas nascidas em cada mês
     */
    @Query("SELECT FUNCTION('MONTH', p.dataNascimento) as mes, COUNT(p) as quantidade FROM Pessoa p " +
           "GROUP BY FUNCTION('MONTH', p.dataNascimento) ORDER BY mes")
    List<Object[]> contarPessoasPorMesNascimento();
}