package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.FuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.FuncionalidadeResponseDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Funcionalidades.
 */
public interface FuncionalidadeService {
    
    /**
     * Criar uma nova funcionalidade.
     * 
     * @param requestDTO Dados da funcionalidade a ser criada
     * @return A funcionalidade criada, com ID gerado
     */
    FuncionalidadeResponseDTO criarFuncionalidade(FuncionalidadeRequestDTO requestDTO);
    
    /**
     * Atualizar uma funcionalidade existente.
     * 
     * @param id ID da funcionalidade a ser atualizada
     * @param requestDTO Novos dados da funcionalidade
     * @return A funcionalidade atualizada
     */
    FuncionalidadeResponseDTO atualizarFuncionalidade(Long id, FuncionalidadeRequestDTO requestDTO);
    
    /**
     * Buscar todas as funcionalidades com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades
     */
    PageResponseDTO<FuncionalidadeResponseDTO> listarFuncionalidadesPaginadas(Pageable pageable);
    
    /**
     * Buscar todas as funcionalidades.
     * 
     * @return Lista de todas as funcionalidades
     */
    List<FuncionalidadeResponseDTO> listarFuncionalidades();
    
    /**
     * Buscar uma funcionalidade específica pelo ID.
     * 
     * @param id ID da funcionalidade a ser buscada
     * @return A funcionalidade encontrada
     */
    FuncionalidadeResponseDTO buscarFuncionalidadePorId(Long id);
    
    /**
     * Buscar funcionalidades por descrição.
     * 
     * @param descricao Descrição ou parte da descrição
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades que contêm a descrição especificada
     */
    PageResponseDTO<FuncionalidadeResponseDTO> buscarPorDescricao(String descricao, Pageable pageable);
    
    /**
     * Buscar funcionalidades por authority.
     * 
     * @param authority Authority ou parte da authority
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades que contêm a authority especificada
     */
    PageResponseDTO<FuncionalidadeResponseDTO> buscarPorAuthority(String authority, Pageable pageable);
    
    /**
     * Buscar funcionalidades por perfil.
     * 
     * @param perfilId ID do perfil
     * @return Lista de funcionalidades associadas ao perfil
     */
    List<FuncionalidadeResponseDTO> buscarPorPerfil(Long perfilId);
    
    /**
     * Buscar funcionalidades não associadas a um perfil.
     * 
     * @param perfilId ID do perfil
     * @return Lista de funcionalidades não associadas ao perfil
     */
    List<FuncionalidadeResponseDTO> buscarNaoAssociadasAoPerfil(Long perfilId);
}
