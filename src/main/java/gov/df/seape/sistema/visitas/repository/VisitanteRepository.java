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
import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de persistência e consulta da entidade Visitante.
 * Gerencia as informações dos visitantes no sistema prisional.
 */
@Repository
public interface VisitanteRepository extends JpaRepository<Visitante, Long> {
    
    /**
     * Busca um visitante pela pessoa associada.
     * 
     * @param pessoa Pessoa associada ao visitante
     * @return Optional contendo o visitante, se encontrado
     */
    Optional<Visitante> findByPessoa(Pessoa pessoa);
    
    /**
     * Busca um visitante pelo CPF da pessoa.
     * 
     * @param cpf CPF da pessoa
     * @return Optional contendo o visitante, se encontrado
     */
    @Query("SELECT v FROM Visitante v WHERE v.pessoa.cpf = :cpf")
    Optional<Visitante> findByCpf(@Param("cpf") String cpf);
    
    /**
     * Busca visitantes pelo nome, ignorando maiúsculas/minúsculas.
     * 
     * @param nome Nome ou parte do nome
     * @return Lista de visitantes correspondentes
     */
    @Query("SELECT v FROM Visitante v WHERE LOWER(v.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY v.pessoa.nome ASC")
    List<Visitante> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    /**
     * Busca avançada de visitantes por múltiplos critérios.
     * 
     * @param nome Nome (opcional)
     * @param cpf CPF (opcional)
     * @param dataNascimento Data de nascimento (opcional)
     * @param pageable Configurações de paginação
     * @return Página de visitantes correspondentes
     */
    @Query("SELECT v FROM Visitante v WHERE " +
           "(:nome IS NULL OR LOWER(v.pessoa.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
           "(:cpf IS NULL OR v.pessoa.cpf = :cpf) AND " +
           "(:dataNascimento IS NULL OR v.pessoa.dataNascimento = :dataNascimento)")
    Page<Visitante> buscarPorMultiplosCriterios(
        @Param("nome") String nome,
        @Param("cpf") String cpf,
        @Param("dataNascimento") LocalDate dataNascimento,
        Pageable pageable
    );
    
    /**
     * Lista visitantes que possuem agendamentos em uma data específica.
     * 
     * @param data Data para verificar agendamentos
     * @return Lista de visitantes com agendamentos na data
     */
    @Query("SELECT DISTINCT v FROM Visitante v JOIN AgendamentoVisita a ON v.id = a.visitante.id " +
           "WHERE CAST(a.dataHoraAgendamento AS LocalDate) = :data AND a.status.descricao <> 'CANCELADO'")
    List<Visitante> findComAgendamentoNaData(@Param("data") LocalDate data);
}