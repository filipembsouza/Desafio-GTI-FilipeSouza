package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.VisitanteRequestDTO;
import gov.df.seape.sistema.visitas.dto.VisitanteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para gerenciamento de Visitantes.
 * Define operações para manipulação de visitantes no sistema prisional.
 */
public interface VisitanteService {
    
    /**
     * Cria um novo visitante.
     * 
     * @param visitanteRequestDTO Dados para criação do visitante
     * @return Visitante criado
     */
    VisitanteResponseDTO criarVisitante(VisitanteRequestDTO visitanteRequestDTO);
    
    /**
     * Atualiza um visitante existente.
     * 
     * @param id Identificador do visitante
     * @param visitanteRequestDTO Novos dados do visitante
     * @return Visitante atualizado
     */
    VisitanteResponseDTO atualizarVisitante(Long id, VisitanteRequestDTO visitanteRequestDTO);
    
    /**
     * Busca um visitante pelo seu identificador.
     * 
     * @param id Identificador do visitante
     * @return Optional com o visitante encontrado
     */
    Optional<VisitanteResponseDTO> buscarVisitantePorId(Long id);
    
    /**
     * Busca um visitante pelo CPF.
     * 
     * @param cpf CPF do visitante
     * @return Optional com o visitante encontrado
     */
    Optional<VisitanteResponseDTO> buscarVisitantePorCpf(String cpf);
    
    /**
     * Lista todos os visitantes cadastrados.
     * 
     * @return Lista de todos os visitantes
     */
    List<VisitanteResponseDTO> listarTodosVisitantes();
    
    /**
     * Lista visitantes com suporte a paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de visitantes
     */
    Page<VisitanteResponseDTO> listarVisitantesPaginado(Pageable pageable);
    
    /**
     * Remove um visitante pelo seu identificador.
     * 
     * @param id Identificador do visitante a ser removido
     */
    void excluirVisitante(Long id);
    
    /**
     * Busca visitantes por nome.
     * 
     * @param nome Termo de busca no nome
     * @return Lista de visitantes correspondentes
     */
    List<VisitanteResponseDTO> buscarVisitantePorNome(String nome);
}