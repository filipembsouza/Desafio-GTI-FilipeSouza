package gov.df.seape.sistema.visitas.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/diagnostico")
@Tag(name = "Diagnóstico")
public class DiagnosticoController {
    @GetMapping("/swagger-status")
    @Operation(summary = "Verificar status do Swagger")
    @SecurityRequirements() // Remove requisitos de segurança
    public Map<String, Object> getSwaggerStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "online");
        response.put("swagger-ui", "/swagger-ui/index.html");
        response.put("api-docs", "/v3/api-docs");
        return response;
    }
}

    @GetMapping("/api-info")
    @Operation(
        summary = "Informações da API",
        description = "Retorna informações básicas sobre a API"
    )
    @SecurityRequirements() // Remover requisitos de segurança para este endpoint
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "Sistema de Gestão de Visitas para Unidade Prisional");
        info.put("version", "1.0.0");
        info.put("description", "API REST para gerenciar o fluxo de visitas em uma unidade prisional");
        info.put("swaggerUrl", "/swagger-ui/index.html");
        info.put("apiDocsUrl", "/v3/api-docs");
        
        return ResponseEntity.ok(info);
    }
}