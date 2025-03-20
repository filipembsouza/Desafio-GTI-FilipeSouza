package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.StatusRequestDTO;
import gov.df.seape.sistema.visitas.dto.StatusResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para gerenciamento de Status no sistema de visitas prisionais.
 * Define operações para manipulação dos estados de agendamentos de visitas.
 */
public interface StatusService {
    
    /**
     * Cria um novo status de agendamento.
     * 
     * @param statusRequestDTO Dados para criação do novo status
     * @return Status criado com suas informações
     */
    StatusResponseDTO criarStatus(StatusRequestDTO statusRequestDTO);
    
    /**
     * Atualiza um status existente.
     * 
     * @param id Identificador do status a ser atualizado
     * @param statusRequestDTO Novos dados do status
     * @return Status atualizado
     */
    StatusResponseDTO atualizarStatus(Long id, StatusRequestDTO statusRequestDTO);
    
    /**
     * Busca um status pelo seu identificador único.
     * 
     * @param id Identificador do status
     * @return Status encontrado, se existir
     */
    Optional<StatusResponseDTO> buscarStatusPorId(Long id);
    
    /**
     * Lista todos os status cadastrados.
     * 
     * @return Lista de todos os status
     */
    List<StatusResponseDTO> listarTodosStatus();
    
    /**
     * Lista status com suporte a paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de status
     */
    Page<StatusResponseDTO> listarStatusPaginado(Pageable pageable);
    
    /**
     * Remove um status pelo seu identificador.
     * 
     * @param id Identificador do status a ser removido
     */
    void excluirStatus(Long id);
    
    /**
     * Busca status que contenham a descrição especificada.
     * 
     * @param descricao Termo de busca na descrição do status
     * @return Lista de status que correspondem à descrição
     */
    List<StatusResponseDTO> buscarStatusPorDescricao(String descricao);
}