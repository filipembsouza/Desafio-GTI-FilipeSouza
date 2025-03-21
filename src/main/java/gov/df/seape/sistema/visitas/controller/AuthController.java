package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.LoginRequestDTO;
import gov.df.seape.sistema.visitas.dto.LoginResponseDTO;
import gov.df.seape.sistema.visitas.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para operações relacionadas à autenticação.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para autenticação e autorização")
public class AuthController {

    private final AuthService authService;

    /**
     * Realiza o login do usuário.
     * 
     * @param loginRequestDTO Credenciais de login
     * @return Resposta contendo token JWT e informações do usuário
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Autentica um usuário e retorna um token JWT")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO responseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Gera um novo token de acesso a partir do token de atualização.
     * 
     * @param refreshToken Token de atualização
     * @return Resposta contendo o novo token JWT
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Gera um novo token a partir do refresh token")
    public ResponseEntity<LoginResponseDTO> refreshToken(@RequestParam String refreshToken) {
        LoginResponseDTO responseDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Realiza o logout do usuário.
     * 
     * @param token Token JWT a ser invalidado
     * @return Status 204 (No Content)
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalida o token do usuário (logout)")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        // Remove o prefixo "Bearer " do token
        String jwtToken = token.replace("Bearer ", "");
        authService.logout(jwtToken);
        return ResponseEntity.noContent().build();
    }
}