package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.LoginRequestDTO;
import gov.df.seape.sistema.visitas.dto.LoginResponseDTO;

/**
 * Interface de serviço para operações relacionadas à autenticação.
 */
public interface AuthService {
    
    /**
     * Realiza o login do usuário.
     * 
     * @param loginRequestDTO Credenciais de login
     * @return Resposta contendo token JWT e informações do usuário
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    
    /**
     * Gera um novo token de acesso a partir do token de atualização.
     * 
     * @param refreshToken Token de atualização
     * @return Resposta contendo o novo token JWT
     */
    LoginResponseDTO refreshToken(String refreshToken);
    
    /**
     * Invalida o token de um usuário (logout).
     * 
     * @param token Token a ser invalidado
     */
    void logout(String token);
}