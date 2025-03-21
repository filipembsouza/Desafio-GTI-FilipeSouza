package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.model.Visitante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Repositório para a entidade Visitante.
 */
@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    
    /**
     * Busca um visitante pela pessoa associada.
     * 
     * @param pessoa Pessoa associada ao visitante
     * @return O visitante encontrado ou vazio
     */
    Optional<Visitante> findByPessoa(Pessoa pessoa);
    
    /**
     * Busca um visitante pelo ID da pessoa associada.
     * 
     * @param pessoaId ID da pessoa associada ao visitante
     * @return O visitante encontrado ou vazio
     */
    @Query("SELECT v FROM Visitante v WHERE v.pessoa.id = :pessoaId")
    Optional<Visitante> findByPessoaId(@Param("pessoaId") Long pessoaId);
    
    /**
     * Busca um visitante pelo CPF.
     * 
     * @param cpf CPF do visitante
     * @return O visitante encontrado ou vazio
     */
    @Query("SELECT v FROM Visitante v WHERE v.pessoa.cpf = :cpf")
    Optional<Visitante> findByCpf(@Param("cpf") String cpf);
    
    /**
     * Busca visitantes pelo nome, ignorando maiúsculas e minúsculas.
     * 
     * @param nome Nome ou parte do nome a ser buscado
     * @return Lista de visitantes que contêm o nome especificado
     */
    @Query("SELECT v FROM Visitante v WHERE LOWER(v.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY v.pessoa.nome ASC")
    List<Visitante> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca visitantes pelo nome, ignorando maiúsculas e minúsculas, com paginação.
     * 
     * @param nome Nome ou parte do nome a ser buscado
     * @param pageable Informações de paginação
     * @return Página de visitantes que contêm o nome especificado
     */
    @Query("SELECT v FROM Visitante v WHERE LOWER(v.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<Visitante> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
    
    /**
     * Busca visitantes por IDs.
     * 
     * @param ids Lista de IDs de visitantes
     * @param pageable Informações de paginação
     * @return Página de visitantes com os IDs especificados
     */
    Page<Visitante> findByIdIn(List<Long> ids, Pageable pageable);
}
