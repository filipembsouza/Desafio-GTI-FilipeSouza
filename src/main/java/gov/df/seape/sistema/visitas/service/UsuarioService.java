package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.UsuarioRequestDTO;
import gov.df.seape.sistema.visitas.dto.UsuarioResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Usuários.
 */
public interface UsuarioService {
    
    /**
     * Criar um novo usuário.
     * 
     * @param requestDTO Dados do usuário a ser criado
     * @return O usuário criado, com ID gerado
     */
    UsuarioResponseDTO criarUsuario(UsuarioRequestDTO requestDTO);
    
    /**
     * Atualizar um usuário existente.
     * 
     * @param id ID do usuário a ser atualizado
     * @param requestDTO Novos dados do usuário
     * @return O usuário atualizado
     */
    UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO requestDTO);
    
    /**
     * Buscar todos os usuários com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de usuários
     */
    PageResponseDTO<UsuarioResponseDTO> listarUsuariosPaginados(Pageable pageable);
    
    /**
     * Buscar todos os usuários.
     * 
     * @return Lista de todos os usuários
     */
    List<UsuarioResponseDTO> listarUsuarios();
    
    /**
     * Buscar um usuário específico pelo ID.
     * 
     * @param id ID do usuário a ser buscado
     * @return O usuário encontrado
     */
    UsuarioResponseDTO buscarUsuarioPorId(Long id);
    
    /**
     * Buscar um usuário pelo email.
     * 
     * @param email Email do usuário
     * @return O usuário encontrado
     */
    UsuarioResponseDTO buscarPorEmail(String email);
    
    /**
     * Buscar usuários por nome.
     * 
     * @param nome Nome ou parte do nome
     * @param pageable Configurações de paginação
     * @return Página de usuários que contêm o nome especificado
     */
    PageResponseDTO<UsuarioResponseDTO> buscarPorNome(String nome, Pageable pageable);
    
    /**
     * Buscar usuários por perfil.
     * 
     * @param perfilId ID do perfil
     * @param pageable Configurações de paginação
     * @return Página de usuários com o perfil especificado
     */
    PageResponseDTO<UsuarioResponseDTO> buscarPorPerfil(Long perfilId, Pageable pageable);
    
    /**
     * Alterar a senha de um usuário.
     * 
     * @param id ID do usuário
     * @param senhaAtual Senha atual
     * @param novaSenha Nova senha
     * @return O usuário com a senha atualizada
     */
    UsuarioResponseDTO alterarSenha(Long id, String senhaAtual, String novaSenha);
}