package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Vínculos entre Perfis e Funcionalidades.
 */
public interface VincPerfilFuncionalidadeService {
    
    /**
     * Criar um novo vínculo entre perfil e funcionalidade.
     * 
     * @param requestDTO Dados do vínculo a ser criado
     * @return O vínculo criado, com ID gerado
     */
    VincPerfilFuncionalidadeResponseDTO criarVinculo(VincPerfilFuncionalidadeRequestDTO requestDTO);
    
    /**
     * Excluir um vínculo existente.
     * 
     * @param id ID do vínculo a ser excluído
     */
    void excluirVinculo(Long id);
    
    /**
     * Buscar todos os vínculos com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de vínculos
     */
    PageResponseDTO<VincPerfilFuncionalidadeResponseDTO> listarVinculosPaginados(Pageable pageable);
    
    /**
     * Buscar todos os vínculos.
     * 
     * @return Lista de todos os vínculos
     */
    List<VincPerfilFuncionalidadeResponseDTO> listarVinculos();
    
    /**
     * Buscar um vínculo específico pelo ID.
     * 
     * @param id ID do vínculo a ser buscado
     * @return O vínculo encontrado
     */
    VincPerfilFuncionalidadeResponseDTO buscarVinculoPorId(Long id);
    
    /**
     * Buscar vínculos por perfil.
     * 
     * @param perfilId ID do perfil
     * @param pageable Configurações de paginação
     * @return Página de vínculos do perfil
     */
    PageResponseDTO<VincPerfilFuncionalidadeResponseDTO> buscarPorPerfil(Long perfilId, Pageable pageable);
    
    /**
     * Buscar vínculos por funcionalidade.
     * 
     * @param funcionalidadeId ID da funcionalidade
     * @param pageable Configurações de paginação
     * @return Página de vínculos da funcionalidade
     */
    PageResponseDTO<VincPerfilFuncionalidadeResponseDTO> buscarPorFuncionalidade(Long funcionalidadeId, Pageable pageable);
    
    /**
     * Verificar se um perfil possui uma determinada funcionalidade.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeId ID da funcionalidade
     * @return true se o perfil possui a funcionalidade, false caso contrário
     */
    boolean verificarVinculo(Long perfilId, Long funcionalidadeId);
}