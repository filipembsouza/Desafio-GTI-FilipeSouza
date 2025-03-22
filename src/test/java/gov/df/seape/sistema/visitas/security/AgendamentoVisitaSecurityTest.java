package gov.df.seape.sistema.visitas.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest // Carrega apenas os controllers (limitado aos endpoints)
class AgendamentoVisitaSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void acessoSemAutenticacaoDeveFalhar() throws Exception {
        AgendamentoVisitaRequestDTO request = new AgendamentoVisitaRequestDTO();
        request.setCustodiadoId(1L);
        request.setVisitanteId(2L);
        request.setDataHoraAgendamento(LocalDateTime.now().plusDays(1));
        request.setObservacoes("Teste segurança sem autenticação");
        
        // Sem autenticação, espera 401 (Unauthorized)
        mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void acessoComUsuarioAutenticadoDeveFuncionar() throws Exception {
        AgendamentoVisitaRequestDTO request = new AgendamentoVisitaRequestDTO();
        request.setCustodiadoId(1L);
        request.setVisitanteId(2L);
        request.setDataHoraAgendamento(LocalDateTime.now().plusDays(1));
        request.setObservacoes("Teste segurança com usuário");
        
        // Com autenticação, espera que o endpoint retorne sucesso (por exemplo, 201 Created)
        mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void acessoComPerfilAdminDeveFuncionar() throws Exception {
        // Testa com perfil ADMIN (caso haja regras diferenciadas)
        AgendamentoVisitaRequestDTO request = new AgendamentoVisitaRequestDTO();
        request.setCustodiadoId(1L);
        request.setVisitanteId(2L);
        request.setDataHoraAgendamento(LocalDateTime.now().plusDays(1));
        request.setObservacoes("Teste segurança com ADMIN");
        
        mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());
    }
}
