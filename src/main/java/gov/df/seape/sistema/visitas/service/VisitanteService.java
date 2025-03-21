package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.VisitanteRequestDTO;
import gov.df.seape.sistema.visitas.dto.VisitanteResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Visitantes.
 */
public interface VisitanteService {
    
    /**
     * Criar um novo visitante.
     * 
     * @param requestDTO Dados do visitante a ser criado
     * @return O visitante criado, com ID gerado
     */
    VisitanteResponseDTO criarVisitante(VisitanteRequestDTO requestDTO);
    
    /**
     * Atualizar um visitante existente.
     * 
     * @param id ID do visitante a ser atualizado
     * @param requestDTO Novos dados do visitante
     * @return O visitante atualizado
     */
    VisitanteResponseDTO atualizarVisitante(Long id, VisitanteRequestDTO requestDTO);
    
    /**
     * Buscar todos os visitantes com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de visitantes
     */
    PageResponseDTO<VisitanteResponseDTO> listarVisitantesPaginados(Pageable pageable);
    
    /**
     * Buscar todos os visitantes.
     * 
     * @return Lista de todos os visitantes
     */
    List<VisitanteResponseDTO> listarVisitantes();
    
    /**
     * Buscar um visitante específico pelo ID.
     * 
     * @param id ID do visitante a ser buscado
     * @return O visitante encontrado
     */
    VisitanteResponseDTO buscarVisitantePorId(Long id);
    
    /**
     * Buscar um visitante pelo CPF.
     * 
     * @param cpf CPF do visitante
     * @return O visitante encontrado
     */
    VisitanteResponseDTO buscarPorCpf(String cpf);
    
    /**
     * Buscar visitantes por nome.
     * 
     * @param nome Nome ou parte do nome
     * @param pageable Configurações de paginação
     * @return Página de visitantes que contêm o nome especificado
     */
    PageResponseDTO<VisitanteResponseDTO> buscarPorNome(String nome, Pageable pageable);
    
    /**
     * Buscar visitantes que visitam um determinado custodiado.
     * 
     * @param custodiadoId ID do custodiado
     * @param pageable Configurações de paginação
     * @return Página de visitantes que visitam o custodiado especificado
     */
    PageResponseDTO<VisitanteResponseDTO> buscarVisitantesPorCustodiado(Long custodiadoId, Pageable pageable);
}