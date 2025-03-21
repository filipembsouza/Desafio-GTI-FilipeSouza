package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.UnidadePenalRequestDTO;
import gov.df.seape.sistema.visitas.dto.UnidadePenalResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Unidades Penais.
 */
public interface UnidadePenalService {
    
    /**
     * Criar uma nova unidade penal.
     * 
     * @param requestDTO Dados da unidade penal a ser criada
     * @return A unidade penal criada, com ID gerado
     */
    UnidadePenalResponseDTO criarUnidadePenal(UnidadePenalRequestDTO requestDTO);
    
    /**
     * Atualizar uma unidade penal existente.
     * 
     * @param id ID da unidade penal a ser atualizada
     * @param requestDTO Novos dados da unidade penal
     * @return A unidade penal atualizada
     */
    UnidadePenalResponseDTO atualizarUnidadePenal(Long id, UnidadePenalRequestDTO requestDTO);
    
    /**
     * Buscar todas as unidades penais com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de unidades penais
     */
    PageResponseDTO<UnidadePenalResponseDTO> listarUnidadesPenaisPaginadas(Pageable pageable);
    
    /**
     * Buscar todas as unidades penais.
     * 
     * @return Lista de todas as unidades penais
     */
    List<UnidadePenalResponseDTO> listarUnidadesPenais();
    
    /**
     * Buscar uma unidade penal específica pelo ID.
     * 
     * @param id ID da unidade penal a ser buscada
     * @return A unidade penal encontrada
     */
    UnidadePenalResponseDTO buscarUnidadePenalPorId(Long id);
    
    /**
     * Buscar unidades penais por nome.
     * 
     * @param nome Nome ou parte do nome
     * @param pageable Configurações de paginação
     * @return Página de unidades penais que contêm o nome especificado
     */
    PageResponseDTO<UnidadePenalResponseDTO> buscarPorNome(String nome, Pageable pageable);
}