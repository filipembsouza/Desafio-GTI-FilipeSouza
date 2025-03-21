package gov.df.seape.sistema.visitas.integration;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaRequestDTO;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaResponseDTO;
import gov.df.seape.sistema.visitas.model.*;
import gov.df.seape.sistema.visitas.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AgendamentoVisitaIntegrationTest {  // Removido "public"

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private VisitanteRepository visitanteRepository;

    @Autowired
    private UnidadePenalRepository unidadePenalRepository;

    @Autowired
    private CustodiadoRepository custodiadoRepository;
   
    @Autowired
    private AgendamentoVisitaRepository agendamentoVisitaRepository;

    @Autowired
    private StatusRepository statusRepository;

    private Pessoa pessoaCustodiado;
    private Pessoa pessoaVisitante;
    private Custodiado custodiado;
    private Visitante visitante;
    private UnidadePenal unidadePenal;
    // private Status statusAgendado; // Removido pois não estava em uso
    private LocalDateTime dataHoraValida;
    private AgendamentoVisitaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        LocalDate proximaQuarta = LocalDate.now();
        while (proximaQuarta.getDayOfWeek() != DayOfWeek.WEDNESDAY) {
            proximaQuarta = proximaQuarta.plusDays(1);
        }
        dataHoraValida = LocalDateTime.of(proximaQuarta, LocalTime.of(10, 0));

        unidadePenal = new UnidadePenal();
        unidadePenal.setNome("Unidade Teste Integração");
        unidadePenal.setDescricao("Unidade para testes de integração");
        unidadePenal = unidadePenalRepository.save(unidadePenal);

        pessoaCustodiado = new Pessoa();
        pessoaCustodiado.setNome("Custodiado Teste Integração");
        pessoaCustodiado.setCpf("11122233344");
        pessoaCustodiado.setDataNascimento(LocalDate.of(1985, 5, 15));
        pessoaCustodiado = pessoaRepository.save(pessoaCustodiado);

        custodiado = new Custodiado();
        custodiado.setPessoa(pessoaCustodiado);
        custodiado.setNumeroProntuario("C99999");
        custodiado.setVulgo("Teste");
        custodiado.setUnidadePenal(unidadePenal);
        custodiado = custodiadoRepository.save(custodiado);

        pessoaVisitante = new Pessoa();
        pessoaVisitante.setNome("Visitante Teste Integração");
        pessoaVisitante.setCpf("44433322211");
        pessoaVisitante.setDataNascimento(LocalDate.of(1990, 8, 10));
        pessoaVisitante = pessoaRepository.save(pessoaVisitante);

        visitante = new Visitante();
        visitante.setPessoa(pessoaVisitante);
        visitante = visitanteRepository.save(visitante);

        requestDTO = new AgendamentoVisitaRequestDTO();
        requestDTO.setCustodiadoId(custodiado.getId());
        requestDTO.setVisitanteId(visitante.getId());
        requestDTO.setDataHoraAgendamento(dataHoraValida);
        requestDTO.setObservacoes("Observação de teste de integração");
    }

    @Test
    @DisplayName("Deve criar um agendamento e recuperá-lo com sucesso")
    void criarERecuperarAgendamento() throws Exception {
        MvcResult postResult = mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
                .header("Authorization", "Bearer token"))
                .andExpect(status().isCreated())
                .andReturn();

        String postResponseString = postResult.getResponse().getContentAsString();
        AgendamentoVisitaResponseDTO createdAgendamento = objectMapper.readValue(postResponseString, AgendamentoVisitaResponseDTO.class);
        Long agendamentoId = createdAgendamento.getId();

        mockMvc.perform(get("/api/agendamentos/" + agendamentoId)
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(agendamentoId.intValue())))
                .andExpect(jsonPath("$.custodiadoId", is(custodiado.getId().intValue())))
                .andExpect(jsonPath("$.visitanteId", is(visitante.getId().intValue())))
                .andExpect(jsonPath("$.descricaoStatus", is("AGENDADO")));

        assertTrue(agendamentoVisitaRepository.findById(agendamentoId).isPresent());
    }
    @Test
@DisplayName("Deve atualizar o status de um agendamento existente")
void atualizarStatusAgendamento() throws Exception {
    // Primeiro criar um agendamento
    MvcResult postResult = mockMvc.perform(post("/api/agendamentos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO))
            .header("Authorization", "Bearer token"))
            .andExpect(status().isCreated())
            .andReturn();
    
    String postResponseString = postResult.getResponse().getContentAsString();
    AgendamentoVisitaResponseDTO createdAgendamento = objectMapper.readValue(postResponseString, AgendamentoVisitaResponseDTO.class);
    Long agendamentoId = createdAgendamento.getId();
    
    // Obter o ID do status CONFIRMADO
    Status statusConfirmado = statusRepository.findByDescricaoIgnoreCase("CONFIRMADO")
            .orElseThrow(() -> new RuntimeException("Status CONFIRMADO não encontrado"));
    
    // Preparar DTO para atualização
    AgendamentoVisitaRequestDTO updateDTO = new AgendamentoVisitaRequestDTO();
    updateDTO.setCustodiadoId(custodiado.getId());
    updateDTO.setVisitanteId(visitante.getId());
    updateDTO.setDataHoraAgendamento(dataHoraValida);
    updateDTO.setStatusId(statusConfirmado.getId());
    
    // Executar a atualização
    mockMvc.perform(put("/api/agendamentos/" + agendamentoId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateDTO))
            .header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descricaoStatus", is("CONFIRMADO")));
    
    // Verificar se a atualização foi salva no banco
    AgendamentoVisita agendamentoAtualizado = agendamentoVisitaRepository.findById(agendamentoId)
            .orElseThrow(() -> new RuntimeException("Agendamento não encontrado após atualização"));
    
    assertEquals(statusConfirmado.getId(), agendamentoAtualizado.getStatus().getId());
}
}
