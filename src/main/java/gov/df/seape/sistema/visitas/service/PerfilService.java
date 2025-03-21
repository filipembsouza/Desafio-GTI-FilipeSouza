package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.PerfilRequestDTO;
import gov.df.seape.sistema.visitas.dto.PerfilResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Perfis de usuário.
 */
public interface PerfilService {
    
    /**
     * Criar um novo perfil.
     * 
     * @param requestDTO Dados do perfil a ser criado
     * @return O perfil criado, com ID gerado
     */
    PerfilResponseDTO criarPerfil(PerfilRequestDTO requestDTO);
    
    /**
     * Atualizar um perfil existente.
     * 
     * @param id ID do perfil a ser atualizado
     * @param requestDTO Novos dados do perfil
     * @return O perfil atualizado
     */
    PerfilResponseDTO atualizarPerfil(Long id, PerfilRequestDTO requestDTO);
    
    /**
     * Buscar todos os perfis com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de perfis
     */
    PageResponseDTO<PerfilResponseDTO> listarPerfisPaginados(Pageable pageable);
    
    /**
     * Buscar todos os perfis.
     * 
     * @return Lista de todos os perfis
     */
    List<PerfilResponseDTO> listarPerfis();
    
    /**
     * Buscar um perfil específico pelo ID.
     * 
     * @param id ID do perfil a ser buscado
     * @return O perfil encontrado
     */
    PerfilResponseDTO buscarPerfilPorId(Long id);
    
    /**
     * Buscar perfis por descrição.
     * 
     * @param descricao Descrição ou parte da descrição
     * @param pageable Configurações de paginação
     * @return Página de perfis que contêm a descrição especificada
     */
    PageResponseDTO<PerfilResponseDTO> buscarPorDescricao(String descricao, Pageable pageable);
    
    /**
     * Adicionar funcionalidades a um perfil.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeIds Lista de IDs de funcionalidades a serem adicionadas
     * @return O perfil atualizado com as novas funcionalidades
     */
    PerfilResponseDTO adicionarFuncionalidades(Long perfilId, List<Long> funcionalidadeIds);
    
    /**
     * Remover funcionalidades de um perfil.
     * 
     * @param perfilId ID do perfil
     * @param funcionalidadeIds Lista de IDs de funcionalidades a serem removidas
     * @return O perfil atualizado
     */
    PerfilResponseDTO removerFuncionalidades(Long perfilId, List<Long> funcionalidadeIds);
}
