package gov.df.seape.sistema.visitas.repository;

import gov.df.seape.sistema.visitas.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repositório para a entidade Status.
 * Fornece métodos para buscar status pelo nome e listar todos os status disponíveis.
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    /**
     * Busca um status pela descrição, ignorando maiúsculas e minúsculas.
     *
     * @param descricao Nome do status a ser buscado (exemplo: "AGENDADO", "CANCELADO").
     * @return O status correspondente, se existir.
     */
    @Query("SELECT s FROM Status s WHERE LOWER(s.descricao) = LOWER(:descricao)")
    Optional<Status> findByDescricaoIgnoreCase(@Param("descricao") String descricao);

    /**
     * Lista todos os status cadastrados, ordenados alfabeticamente.
     *
     * @return Lista de status ordenada por descrição.
     */
    @Query("SELECT s FROM Status s ORDER BY s.descricao ASC")
    List<Status> findAllOrderByDescricao();
}
