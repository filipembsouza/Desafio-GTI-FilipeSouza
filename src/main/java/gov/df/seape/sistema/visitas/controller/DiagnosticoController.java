package gov.df.seape.sistema.visitas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/diagnostico")
@Tag(name = "Diagnóstico", description = "Endpoints para diagnóstico do sistema")
public class DiagnosticoController {

    @GetMapping("/status")
    @Operation(summary = "Verificar status da API", 
               description = "Endpoint para verificar se a API está funcionando corretamente")
    @ApiResponse(responseCode = "200", description = "API funcionando normalmente")
    public String getStatus() {
        return "API funcionando corretamente. Se você está vendo esta mensagem, o controller está respondendo.";
    }
    
    @GetMapping("/swagger-info")
    @Operation(summary = "Informações do Swagger", 
               description = "Informações para debug do Swagger")
    public String getSwaggerInfo() {
        return "Este endpoint é para verificar se as anotações do Swagger estão sendo processadas corretamente.";
    }
}