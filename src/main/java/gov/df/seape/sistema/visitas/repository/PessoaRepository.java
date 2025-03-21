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
 */
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
    /**
     * Verifica se existe uma pessoa com o CPF informado.
     * 
     * @param cpf CPF a ser verificado
     * @return true se existir, false caso contrário
     */
    boolean existsByCpf(String cpf);
    
    /**
     * Busca uma pessoa pelo CPF.
     * 
     * @param cpf CPF da pessoa
     * @return A pessoa encontrada ou vazio
     */
    Optional<Pessoa> findByCpf(String cpf);
    
    /**
     * Busca pessoas pelo nome, ignorando maiúsculas e minúsculas.
     * 
     * @param nome Nome ou parte do nome a ser buscado
     * @return Lista de pessoas que contêm o nome especificado
     */
    @Query("SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY p.nome ASC")
    List<Pessoa> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca pessoas pelo nome, ignorando maiúsculas e minúsculas, com paginação.
     * 
     * @param nome Nome ou parte do nome a ser buscado
     * @param pageable Informações de paginação
     * @return Página de pessoas que contêm o nome especificado
     */
    @Query("SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Pessoa> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Busca pessoas por data de nascimento.
     * 
     * @param dataInicio Data inicial do intervalo
     * @param dataFim Data final do intervalo
     * @return Lista de pessoas nascidas no intervalo especificado
     */
    @Query("SELECT p FROM Pessoa p WHERE p.dataNascimento BETWEEN :dataInicio AND :dataFim ORDER BY p.dataNascimento ASC")
    List<Pessoa> findByDataNascimentoBetween(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim);
    
    /**
     * Busca pessoa associada a um custodiado, visitante ou usuário.
     * 
     * @param id ID da pessoa
     * @return true se a pessoa está em uso, false caso contrário
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true WHEN COUNT(v) > 0 THEN true WHEN COUNT(u) > 0 THEN true ELSE false END " +
           "FROM Pessoa p LEFT JOIN Custodiado c ON p.id = c.pessoa.id " +
           "LEFT JOIN Visitante v ON p.id = v.pessoa.id " +
           "LEFT JOIN Usuario u ON p.id = u.pessoa.id " +
           "WHERE p.id = :id")
    boolean isInUse(@Param("id") Long id);
}