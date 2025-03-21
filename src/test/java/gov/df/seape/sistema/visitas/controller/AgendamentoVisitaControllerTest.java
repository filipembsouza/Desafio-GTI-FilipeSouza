package gov.df.seape.sistema.visitas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaRequestDTO;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaResponseDTO;
import gov.df.seape.sistema.visitas.dto.FiltroAgendamentoDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.exception.AgendamentoConflitanteException;
import gov.df.seape.sistema.visitas.exception.HorarioNaoPermitidoException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.service.AgendamentoVisitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AgendamentoVisitaController.class)
class AgendamentoVisitaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgendamentoVisitaService agendamentoVisitaService;

    private AgendamentoVisitaRequestDTO requestDTO;
    private AgendamentoVisitaResponseDTO responseDTO;
    private List<AgendamentoVisitaResponseDTO> agendamentoList;
    private PageResponseDTO<AgendamentoVisitaResponseDTO> pageResponse;
    private FiltroAgendamentoDTO filtroDTO;

    @BeforeEach
    void setUp() {
        // Configurar data e hora válidas (quarta-feira às 10h)
        LocalDateTime dataHoraValida = LocalDateTime.of(
            LocalDate.now().with(DayOfWeek.WEDNESDAY), 
            LocalTime.of(10, 0)
        );

        // Configurar DTO de requisição
        requestDTO = new AgendamentoVisitaRequestDTO();
        requestDTO.setCustodiadoId(1L);
        requestDTO.setVisitanteId(1L);
        requestDTO.setDataHoraAgendamento(dataHoraValida);
        requestDTO.setObservacoes("Observação de teste");

        // Configurar DTO de resposta
        responseDTO = new AgendamentoVisitaResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCustodiadoId(1L);
        responseDTO.setNomeCustodiado("Custodiado Teste");
        responseDTO.setNumeroProntuarioCustodiado("C12345");
        responseDTO.setVisitanteId(1L);
        responseDTO.setNomeVisitante("Visitante Teste");
        responseDTO.setCpfVisitante("12345678901");
        responseDTO.setDataHoraAgendamento(dataHoraValida);
        responseDTO.setDataHoraFormatada(dataHoraValida.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        responseDTO.setStatusId(1L);
        responseDTO.setDescricaoStatus("AGENDADO");
        responseDTO.setUnidadePenalId(1L);
        responseDTO.setNomeUnidadePenal("Unidade Teste");
        responseDTO.setObservacoes("Observação de teste");

        // Configurar lista e página de agendamentos
        agendamentoList = Arrays.asList(responseDTO);
        Page<AgendamentoVisitaResponseDTO> page = new PageImpl<>(agendamentoList);
        pageResponse = new PageResponseDTO<>(page);

        // Configurar DTO de filtro
        filtroDTO = new FiltroAgendamentoDTO();
        filtroDTO.setCustodiadoId(1L);
        filtroDTO.setDataInicio(LocalDate.now().minusDays(7));
        filtroDTO.setDataFim(LocalDate.now().plusDays(7));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve criar um agendamento com sucesso")
    void criarAgendamento() throws Exception {
        when(agendamentoVisitaService.criarAgendamento(any(AgendamentoVisitaRequestDTO.class)))
            .thenReturn(responseDTO);

        mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nomeCustodiado", is("Custodiado Teste")))
                .andExpect(jsonPath("$.nomeVisitante", is("Visitante Teste")))
                .andExpect(jsonPath("$.descricaoStatus", is("AGENDADO")));

        verify(agendamentoVisitaService).criarAgendamento(any(AgendamentoVisitaRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve retornar erro ao tentar criar agendamento em horário não permitido")
    void criarAgendamentoHorarioNaoPermitido() throws Exception {
        when(agendamentoVisitaService.criarAgendamento(any(AgendamentoVisitaRequestDTO.class)))
            .thenThrow(new HorarioNaoPermitidoException("Horário não permitido para visitas"));

        mockMvc.perform(post("/api/agendamentos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("Horário não permitido")));

        verify(agendamentoVisitaService).criarAgendamento(any(AgendamentoVisitaRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve atualizar um agendamento com sucesso")
    void atualizarAgendamento() throws Exception {
        when(agendamentoVisitaService.atualizarAgendamento(eq(1L), any(AgendamentoVisitaRequestDTO.class)))
            .thenReturn(responseDTO);

        mockMvc.perform(put("/api/agendamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nomeCustodiado", is("Custodiado Teste")))
                .andExpect(jsonPath("$.nomeVisitante", is("Visitante Teste")));

        verify(agendamentoVisitaService).atualizarAgendamento(eq(1L), any(AgendamentoVisitaRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve retornar erro ao tentar atualizar agendamento inexistente")
    void atualizarAgendamentoInexistente() throws Exception {
        when(agendamentoVisitaService.atualizarAgendamento(eq(99L), any(AgendamentoVisitaRequestDTO.class)))
            .thenThrow(new RecursoNaoEncontradoException("Agendamento não encontrado com ID: 99"));

        mockMvc.perform(put("/api/agendamentos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Agendamento não encontrado")));

        verify(agendamentoVisitaService).atualizarAgendamento(eq(99L), any(AgendamentoVisitaRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve retornar erro ao tentar atualizar agendamento com conflito")
    void atualizarAgendamentoConflitante() throws Exception {
        when(agendamentoVisitaService.atualizarAgendamento(eq(1L), any(AgendamentoVisitaRequestDTO.class)))
            .thenThrow(new AgendamentoConflitanteException("Já existe um agendamento para este custodiado próximo a este horário"));

        mockMvc.perform(put("/api/agendamentos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("Já existe um agendamento")));

        verify(agendamentoVisitaService).atualizarAgendamento(eq(1L), any(AgendamentoVisitaRequestDTO.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve listar agendamentos paginados")
    void listarAgendamentosPaginados() throws Exception {
        when(agendamentoVisitaService.listarAgendamentosPaginados(any(Pageable.class)))
            .thenReturn(pageResponse);

        mockMvc.perform(get("/api/agendamentos/paginado")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.totalElements", is(1)));

        verify(agendamentoVisitaService).listarAgendamentosPaginados(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve listar todos os agendamentos")
    void listarAgendamentos() throws Exception {
        when(agendamentoVisitaService.listarAgendamentos())
            .thenReturn(agendamentoList);

        mockMvc.perform(get("/api/agendamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nomeCustodiado", is("Custodiado Teste")));

        verify(agendamentoVisitaService).listarAgendamentos();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve buscar agendamento por ID")
    void buscarAgendamentoPorId() throws Exception {
        when(agendamentoVisitaService.buscarAgendamentoPorId(1L))
            .thenReturn(responseDTO);

        mockMvc.perform(get("/api/agendamentos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nomeCustodiado", is("Custodiado Teste")))
                .andExpect(jsonPath("$.nomeVisitante", is("Visitante Teste")));

        verify(agendamentoVisitaService).buscarAgendamentoPorId(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve retornar erro ao buscar agendamento inexistente")
    void buscarAgendamentoInexistente() throws Exception {
        when(agendamentoVisitaService.buscarAgendamentoPorId(99L))
            .thenThrow(new RecursoNaoEncontradoException("Agendamento não encontrado com ID: 99"));

        mockMvc.perform(get("/api/agendamentos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Agendamento não encontrado")));

        verify(agendamentoVisitaService).buscarAgendamentoPorId(99L);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve filtrar agendamentos")
    void filtrarAgendamentos() throws Exception {
        when(agendamentoVisitaService.filtrarAgendamentos(any(FiltroAgendamentoDTO.class), any(Pageable.class)))
            .thenReturn(pageResponse);

        mockMvc.perform(post("/api/agendamentos/filtro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filtroDTO))
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)));

        verify(agendamentoVisitaService).filtrarAgendamentos(any(FiltroAgendamentoDTO.class), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve buscar agendamentos por data")
    void buscarAgendamentosPorData() throws Exception {
        when(agendamentoVisitaService.buscarAgendamentosPorData(any(LocalDate.class), any(Pageable.class)))
            .thenReturn(pageResponse);

        mockMvc.perform(get("/api/agendamentos/data/2023-08-01")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)));

        verify(agendamentoVisitaService).buscarAgendamentosPorData(any(LocalDate.class), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve buscar agendamentos por custodiado")
    void buscarAgendamentosPorCustodiado() throws Exception {
        when(agendamentoVisitaService.buscarAgendamentosPorCustodiado(eq(1L), any(Pageable.class)))
            .thenReturn(pageResponse);

        mockMvc.perform(get("/api/agendamentos/custodiado/1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)));

        verify(agendamentoVisitaService).buscarAgendamentosPorCustodiado(eq(1L), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve buscar agendamentos por visitante")
    void buscarAgendamentosPorVisitante() throws Exception {
        when(agendamentoVisitaService.buscarAgendamentosPorVisitante(eq(1L), any(Pageable.class)))
            .thenReturn(pageResponse);

        mockMvc.perform(get("/api/agendamentos/visitante/1")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)));

        verify(agendamentoVisitaService).buscarAgendamentosPorVisitante(eq(1L), any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve cancelar agendamento com sucesso")
    void cancelarAgendamento() throws Exception {
        doNothing().when(agendamentoVisitaService).cancelarAgendamento(1L);

        mockMvc.perform(delete("/api/agendamentos/1"))
                .andExpect(status().isNoContent());

        verify(agendamentoVisitaService).cancelarAgendamento(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Deve retornar erro ao cancelar agendamento inexistente")
    void cancelarAgendamentoInexistente() throws Exception {
        doThrow(new RecursoNaoEncontradoException("Agendamento não encontrado com ID: 99"))
            .when(agendamentoVisitaService).cancelarAgendamento(99L);

        mockMvc.perform(delete("/api/agendamentos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Agendamento não encontrado")));

        verify(agendamentoVisitaService).cancelarAgendamento(99L);
    }
}