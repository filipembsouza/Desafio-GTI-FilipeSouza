package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.PerfilRequestDTO;
import gov.df.seape.sistema.visitas.dto.PerfilResponseDTO;
import gov.df.seape.sistema.visitas.dto.FuncionalidadeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para gerenciamento de Perfis.
 * Define operações para manipulação de perfis de usuário no sistema.
 */
public interface PerfilService {
    
    /**
     * Cria um novo perfil.
     * 
     * @param perfilRequestDTO Dados para criação do perfil
     * @return Perfil criado
     */
    PerfilResponseDTO criarPerfil(PerfilRequestDTO perfilRequestDTO);
    
    /**
     * Atualiza um perfil existente.
     * 
     * @param id Identificador do perfil
     * @param perfilRequestDTO Novos dados do perfil
     * @return Perfil atualizado
     */
    PerfilResponseDTO atualizarPerfil(Long id, PerfilRequestDTO perfilRequestDTO);
    
    /**
     * Busca um perfil pelo seu identificador.
     * 
     * @param id Identificador do perfil
     * @return Optional com o perfil encontrado
     */
    Optional<PerfilResponseDTO> buscarPerfilPorId(Long id);
    
    /**
     * Busca um perfil pela descrição.
     * 
     * @param descricao Descrição do perfil
     * @return Optional com o perfil encontrado
     */
    Optional<PerfilResponseDTO> buscarPerfilPorDescricao(String descricao);
    
    /**
     * Lista todos os perfis cadastrados.
     * 
     * @return Lista de todos os perfis
     */
    List<PerfilResponseDTO> listarTodosPerfis();
    
    /**
     * Lista perfis com suporte a paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de perfis
     */
    Page<PerfilResponseDTO> listarPerfisPaginado(Pageable pageable);
    
    /**
     * Remove um perfil pelo seu identificador.
     * 
     * @param id Identificador do perfil a ser removido
     */
    void excluirPerfil(Long id);
    
    /**
     * Busca perfis por descrição.
     * 
     * @param descricao Termo de busca na descrição
     * @return Lista de perfis correspondentes
     */
    List<PerfilResponseDTO> buscarPerfilPorDescricaoContendo(String descricao);
    
    /**
     * Adiciona uma funcionalidade a um perfil.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeId ID da funcionalidade
     * @return Perfil atualizado
     */
    PerfilResponseDTO adicionarFuncionalidade(Long perfilId, Long funcionalidadeId);
    
    /**
     * Remove uma funcionalidade de um perfil.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeId ID da funcionalidade
     * @return Perfil atualizado
     */
    PerfilResponseDTO removerFuncionalidade(Long perfilId, Long funcionalidadeId);
    
    /**
     * Lista funcionalidades de um perfil.
     * 
     * @param perfilId ID do perfil
     * @return Lista de funcionalidades do perfil
     */
    List<FuncionalidadeResponseDTO> listarFuncionalidadesDoPerfil(Long perfilId);
}