package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para gerenciamento de vínculos entre Perfil e Funcionalidade.
 * Define operações para manipulação dos relacionamentos de permissões.
 */
public interface VincPerfilFuncionalidadeService {
    
    /**
     * Cria um novo vínculo entre Perfil e Funcionalidade.
     * 
     * @param vinculoRequestDTO Dados para criação do vínculo
     * @return Vínculo criado
     */
    VincPerfilFuncionalidadeResponseDTO criarVinculo(VincPerfilFuncionalidadeRequestDTO vinculoRequestDTO);
    
    /**
     * Busca um vínculo pelo seu identificador.
     * 
     * @param id Identificador do vínculo
     * @return Optional com o vínculo encontrado
     */
    Optional<VincPerfilFuncionalidadeResponseDTO> buscarVinculoPorId(Long id);
    
    /**
     * Lista todos os vínculos cadastrados.
     * 
     * @return Lista de todos os vínculos
     */
    List<VincPerfilFuncionalidadeResponseDTO> listarTodosVinculos();
    
    /**
     * Lista vínculos com suporte a paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de vínculos
     */
    Page<VincPerfilFuncionalidadeResponseDTO> listarVinculosPaginado(Pageable pageable);
    
    /**
     * Remove um vínculo pelo seu identificador.
     * 
     * @param id Identificador do vínculo a ser removido
     */
    void excluirVinculo(Long id);
    
    /**
     * Lista vínculos por perfil.
     * 
     * @param perfilId ID do perfil
     * @return Lista de vínculos do perfil
     */
    List<VincPerfilFuncionalidadeResponseDTO> listarVinculosPorPerfil(Long perfilId);
    
    /**
     * Lista vínculos por funcionalidade.
     * 
     * @param funcionalidadeId ID da funcionalidade
     * @return Lista de vínculos da funcionalidade
     */
    List<VincPerfilFuncionalidadeResponseDTO> listarVinculosPorFuncionalidade(Long funcionalidadeId);
    
    /**
     * Verifica se existe um vínculo entre perfil e funcionalidade.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeId ID da funcionalidade
     * @return true se o vínculo existir, false caso contrário
     */
    boolean existeVinculo(Long perfilId, Long funcionalidadeId);
}