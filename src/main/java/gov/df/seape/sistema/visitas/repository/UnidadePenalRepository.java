package gov.df.seape.sistema.visitas.repository;

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
 * Repositório para a entidade UnidadePenal.
 * Fornece métodos para realizar operações de banco de dados relacionadas a Unidades Penais.
 */
@Repository
public interface UnidadePenalRepository extends JpaRepository<UnidadePenal, Long> {
    
    /**
     * Busca uma unidade penal pelo nome exato.
     * 
     * @param nome O nome completo da unidade penal
     * @return A unidade penal encontrada, ou vazia se não existir
     */
    Optional<UnidadePenal> findByNome(String nome);
    
    /**
     * Busca unidades penais cujo nome contenha o termo especificado.
     * Esta consulta ignora maiúsculas/minúsculas para facilitar a busca.
     * Os resultados são retornados ordenados alfabeticamente.
     * 
     * @param nome Termo de busca para o nome
     * @return Lista de unidades penais que atendem ao critério, ordenadas por nome
     */
    @Query("SELECT u FROM UnidadePenal u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY u.nome ASC")
    List<UnidadePenal> findByNomeContainingIgnoreCase(@Param("nome") String nome);

    /**
     * Lista todas as unidades penais ordenadas alfabeticamente pelo nome.
     *
     * @return Lista de todas as unidades penais, ordenadas por nome
     */
    @Query("SELECT u FROM UnidadePenal u ORDER BY u.nome ASC")
    List<UnidadePenal> findAllOrderByNome();
    
    /**
     * Busca unidades penais cujo nome contenha o termo especificado, com suporte a paginação.
     * Esta versão do método permite dividir os resultados em páginas, facilitando a 
     * implementação de interfaces que precisam exibir grandes conjuntos de dados de forma
     * controlada (como tabelas com paginação).
     * 
     * Embora unidades penais normalmente não alcancem grandes volumes, este método
     * proporciona consistência com outros repositórios e prepara o sistema para
     * possível expansão futura, além de permitir um padrão uniforme na interface
     * de usuário para todas as listagens.
     * 
     * @param nome Termo de busca para o nome
     * @param pageable Objeto com informações de paginação (página, tamanho, ordenação)
     * @return Página de unidades penais que atendem ao critério
     */
    @Query("SELECT u FROM UnidadePenal u WHERE LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    Page<UnidadePenal> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
}