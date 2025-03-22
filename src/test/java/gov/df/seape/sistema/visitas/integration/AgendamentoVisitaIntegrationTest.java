package gov.df.seape.sistema.visitas.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaRequestDTO;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa as configurações de teste (H2, segurança ajustada, etc.)
class AgendamentoVisitaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @WithMockUser(roles = "USER")
    void criarAgendamentoIntegrationTest() throws Exception {
        // Cria o DTO de requisição
        AgendamentoVisitaRequestDTO request = new AgendamentoVisitaRequestDTO();
        request.setCustodiadoId(1L);
        request.setVisitanteId(2L);
        request.setDataHoraAgendamento(LocalDateTime.now().plusDays(1));
        request.setObservacoes("Agendamento de integração");
        
        // Realiza a chamada POST
        mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()))
            .andExpect(jsonPath("$.descricaoStatus").value("AGENDADO"));
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void atualizarAgendamentoIntegrationTest() throws Exception {
        // Cria um agendamento primeiro
        AgendamentoVisitaRequestDTO request = new AgendamentoVisitaRequestDTO();
        request.setCustodiadoId(1L);
        request.setVisitanteId(2L);
        request.setDataHoraAgendamento(LocalDateTime.now().plusDays(1));
        request.setObservacoes("Agendamento para atualização");
        
        String postResponse = mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        
        AgendamentoVisitaResponseDTO response = objectMapper.readValue(postResponse, AgendamentoVisitaResponseDTO.class);
        Long agendamentoId = response.getId();
        
        // Atualiza o agendamento (por exemplo, altera a data/hora e observações)
        request.setDataHoraAgendamento(LocalDateTime.now().plusDays(2));
        request.setObservacoes("Agendamento atualizado");
        
        mockMvc.perform(put("/api/agendamentos/" + agendamentoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(agendamentoId));
    }
    
    @Test
    @WithMockUser(roles = "USER")
    void cancelarAgendamentoIntegrationTest() throws Exception {
        // Cria um agendamento para cancelamento
        AgendamentoVisitaRequestDTO request = new AgendamentoVisitaRequestDTO();
        request.setCustodiadoId(1L);
        request.setVisitanteId(2L);
        request.setDataHoraAgendamento(LocalDateTime.now().plusDays(1));
        request.setObservacoes("Agendamento para cancelamento");
        
        String postResponse = mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();
        
        AgendamentoVisitaResponseDTO response = objectMapper.readValue(postResponse, AgendamentoVisitaResponseDTO.class);
        Long agendamentoId = response.getId();
        
        // Executa o cancelamento
        mockMvc.perform(delete("/api/agendamentos/" + agendamentoId))
            .andExpect(status().isNoContent());
    }
}
