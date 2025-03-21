package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaRequestDTO;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaResponseDTO;
import gov.df.seape.sistema.visitas.dto.FiltroAgendamentoDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.exception.AgendamentoConflitanteException;
import gov.df.seape.sistema.visitas.exception.HorarioNaoPermitidoException;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.*;
import gov.df.seape.sistema.visitas.repository.AgendamentoVisitaRepository;
import gov.df.seape.sistema.visitas.repository.CustodiadoRepository;
import gov.df.seape.sistema.visitas.repository.StatusRepository;
import gov.df.seape.sistema.visitas.repository.VisitanteRepository;
import gov.df.seape.sistema.visitas.service.impl.AgendamentoVisitaServiceImpl;
import gov.df.seape.sistema.visitas.util.HorarioVisitaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AgendamentoVisitaServiceImplTest {

    @Mock
    private AgendamentoVisitaRepository agendamentoRepository;
    
    @Mock
    private CustodiadoRepository custodiadoRepository;
    
    @Mock
    private VisitanteRepository visitanteRepository;
    
    @Mock
    private StatusRepository statusRepository;
    
    @Mock
    private HorarioVisitaUtil horarioVisitaUtil;
    
    @InjectMocks
    private AgendamentoVisitaServiceImpl agendamentoService;
    
    private Pessoa pessoaCustodiado;
    private Pessoa pessoaVisitante;
    private Custodiado custodiado;
    private Visitante visitante;
    private UnidadePenal unidadePenal;
    private Status statusAgendado;
    private Status statusConfirmado;
    private Status statusRealizado;
    private Status statusCancelado;
    private AgendamentoVisita agendamento;
    private AgendamentoVisitaRequestDTO requestDTO;
    private LocalDateTime dataHoraValida;
    
    @BeforeEach
    void setUp() {
        // Configurar data e hora válidas (quarta-feira às 10h)
        dataHoraValida = LocalDateTime.of(
            LocalDate.now().with(DayOfWeek.WEDNESDAY), 
            LocalTime.of(10, 0)
        );
        
        // Configurar entidade UnidadePenal
        unidadePenal = new UnidadePenal();
        unidadePenal.setId(1L);
        unidadePenal.setNome("Unidade Teste");
        
        // Configurar entidades Pessoa
        pessoaCustodiado = new Pessoa();
        pessoaCustodiado.setId(1L);
        pessoaCustodiado.setNome("Custodiado Teste");
        pessoaCustodiado.setCpf("12345678901");
        
        pessoaVisitante = new Pessoa();
        pessoaVisitante.setId(2L);
        pessoaVisitante.setNome("Visitante Teste");
        pessoaVisitante.setCpf("98765432101");
        
        // Configurar entidade Custodiado
        custodiado = new Custodiado();
        custodiado.setId(1L);
        custodiado.setPessoa(pessoaCustodiado);
        custodiado.setNumeroProntuario("C12345");
        custodiado.setUnidadePenal(unidadePenal);
        
        // Configurar entidade Visitante
        visitante = new Visitante();
        visitante.setId(1L);
        visitante.setPessoa(pessoaVisitante);
        
        // Configurar entidades Status
        statusAgendado = new Status();
        statusAgendado.setId(1L);
        statusAgendado.setDescricao("AGENDADO");
        
        statusConfirmado = new Status();
        statusConfirmado.setId(2L);
        statusConfirmado.setDescricao("CONFIRMADO");
        
        statusRealizado = new Status();
        statusRealizado.setId(3L);
        statusRealizado.setDescricao("REALIZADO");
        
        statusCancelado = new Status();
        statusCancelado.setId(4L);
        statusCancelado.setDescricao("CANCELADO");
        
        // Configurar entidade AgendamentoVisita
        agendamento = new AgendamentoVisita();
        agendamento.setId(1L);
        agendamento.setCustodiado(custodiado);
        agendamento.setVisitante(visitante);
        agendamento.setDataHoraAgendamento(dataHoraValida);
        agendamento.setStatus(statusAgendado);
        
        // Configurar DTO de requisição
        requestDTO = new AgendamentoVisitaRequestDTO();
        requestDTO.setCustodiadoId(1L);
        requestDTO.setVisitanteId(1L);
        requestDTO.setDataHoraAgendamento(dataHoraValida);
    }
    
    @Test
    @DisplayName("Deve criar um agendamento com sucesso")
    void criarAgendamentoSucesso() {
        // Configurar mocks
        when(horarioVisitaUtil.isHorarioPermitido(any(LocalDateTime.class))).thenReturn(true);
        when(custodiadoRepository.findById(anyLong())).thenReturn(Optional.of(custodiado));
        when(visitanteRepository.findById(anyLong())).thenReturn(Optional.of(visitante));
        when(statusRepository.findByDescricaoIgnoreCase("AGENDADO")).thenReturn(Optional.of(statusAgendado));
        when(agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamentoBetween(anyLong(), any(), any())).thenReturn(0L);
        when(agendamentoRepository.findAgendamentosConflitantes(anyLong(), any(), any(), anyLong())).thenReturn(new ArrayList<>());
        when(agendamentoRepository.findByVisitanteIdAndDataHoraAgendamentoBetween(anyLong(), any(), any())).thenReturn(new ArrayList<>());
        when(agendamentoRepository.save(any(AgendamentoVisita.class))).thenReturn(agendamento);
        
        // Executar método a ser testado
        AgendamentoVisitaResponseDTO resultado = agendamentoService.criarAgendamento(requestDTO);
        
        // Verificar resultado
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getCustodiadoId());
        assertEquals(1L, resultado.getVisitanteId());
        assertEquals(dataHoraValida, resultado.getDataHoraAgendamento());
        assertEquals("AGENDADO", resultado.getDescricaoStatus());
        
        // Verificar chamadas aos mocks
        verify(horarioVisitaUtil).isHorarioPermitido(dataHoraValida);
        verify(custodiadoRepository).findById(1L);
        verify(visitanteRepository).findById(1L);
        verify(statusRepository).findByDescricaoIgnoreCase("AGENDADO");
        verify(agendamentoRepository).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar criar agendamento em horário não permitido")
    void criarAgendamentoHorarioNaoPermitido() {
        // Configurar mocks
        when(horarioVisitaUtil.isHorarioPermitido(any(LocalDateTime.class))).thenReturn(false);
        
        // Executar método e verificar exceção
        assertThrows(HorarioNaoPermitidoException.class, () -> {
            agendamentoService.criarAgendamento(requestDTO);
        });
        
        // Verificar chamadas aos mocks
        verify(horarioVisitaUtil).isHorarioPermitido(dataHoraValida);
        verify(custodiadoRepository, never()).findById(anyLong());
        verify(agendamentoRepository, never()).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar criar agendamento para custodiado inexistente")
    void criarAgendamentoCustodiadoNaoEncontrado() {
        // Configurar mocks
        when(horarioVisitaUtil.isHorarioPermitido(any(LocalDateTime.class))).thenReturn(true);
        when(custodiadoRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // Executar método e verificar exceção
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            agendamentoService.criarAgendamento(requestDTO);
        });
        
        // Verificar chamadas aos mocks
        verify(horarioVisitaUtil).isHorarioPermitido(dataHoraValida);
        verify(custodiadoRepository).findById(1L);
        verify(agendamentoRepository, never()).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar criar agendamento para visitante inexistente")
    void criarAgendamentoVisitanteNaoEncontrado() {
        // Configurar mocks
        when(horarioVisitaUtil.isHorarioPermitido(any(LocalDateTime.class))).thenReturn(true);
        when(custodiadoRepository.findById(anyLong())).thenReturn(Optional.of(custodiado));
        when(visitanteRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // Executar método e verificar exceção
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            agendamentoService.criarAgendamento(requestDTO);
        });
        
        // Verificar chamadas aos mocks
        verify(horarioVisitaUtil).isHorarioPermitido(dataHoraValida);
        verify(custodiadoRepository).findById(1L);
        verify(visitanteRepository).findById(1L);
        verify(agendamentoRepository, never()).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar criar agendamento com limite de visitantes excedido")
    void criarAgendamentoLimiteVisitantesExcedido() {
        // Configurar mocks
        when(horarioVisitaUtil.isHorarioPermitido(any(LocalDateTime.class))).thenReturn(true);
        when(custodiadoRepository.findById(anyLong())).thenReturn(Optional.of(custodiado));
        when(visitanteRepository.findById(anyLong())).thenReturn(Optional.of(visitante));
        when(statusRepository.findByDescricaoIgnoreCase("AGENDADO")).thenReturn(Optional.of(statusAgendado));
        when(agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamentoBetween(anyLong(), any(), any())).thenReturn(2L);
        
        // Executar método e verificar exceção
        assertThrows(AgendamentoConflitanteException.class, () -> {
            agendamentoService.criarAgendamento(requestDTO);
        });
        
        // Verificar chamadas aos mocks
        verify(custodiadoRepository).findById(1L);
        verify(visitanteRepository).findById(1L);
        verify(agendamentoRepository).countByCustodiadoIdAndDataHoraAgendamenteBetween(eq(1L), any(), any());
        verify(agendamentoRepository, never()).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar criar agendamento com conflito para o custodiado")
    void criarAgendamentoConflitoCustodiado() {
        // Configurar mocks
        when(horarioVisitaUtil.isHorarioPermitido(any(LocalDateTime.class))).thenReturn(true);
        when(custodiadoRepository.findById(anyLong())).thenReturn(Optional.of(custodiado));
        when(visitanteRepository.findById(anyLong())).thenReturn(Optional.of(visitante));
        when(statusRepository.findByDescricaoIgnoreCase("AGENDADO")).thenReturn(Optional.of(statusAgendado));
        when(statusRepository.findByDescricaoIgnoreCase("CANCELADO")).thenReturn(Optional.of(statusCancelado));
        when(agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamenteBetween(anyLong(), any(), any())).thenReturn(0L);
        when(agendamentoRepository.findAgendamentosConflitantes(anyLong(), any(), any(), anyLong())).thenReturn(List.of(agendamento));
        
        // Executar método e verificar exceção
        assertThrows(AgendamentoConflitanteException.class, () -> {
            agendamentoService.criarAgendamento(requestDTO);
        });
        
        // Verificar chamadas aos mocks
        verify(custodiadoRepository).findById(1L);
        verify(visitanteRepository).findById(1L);
        verify(agendamentoRepository).findAgendamentosConflitantes(eq(1L), any(), any(), anyLong());
        verify(agendamentoRepository, never()).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve atualizar um agendamento com sucesso")
    void atualizarAgendamentoSucesso() {
        // Preparar novos dados para atualização
        AgendamentoVisitaRequestDTO atualizacaoDTO = new AgendamentoVisitaRequestDTO();
        atualizacaoDTO.setCustodiadoId(1L);
        atualizacaoDTO.setVisitanteId(1L);
        atualizacaoDTO.setDataHoraAgendamento(dataHoraValida);
        atualizacaoDTO.setStatusId(2L); // Status CONFIRMADO
        
        // Configurar mocks
        when(horarioVisitaUtil.isHorarioPermitido(any(LocalDateTime.class))).thenReturn(true);
        when(agendamentoRepository.findById(anyLong())).thenReturn(Optional.of(agendamento));
        when(custodiadoRepository.findById(anyLong())).thenReturn(Optional.of(custodiado));
        when(visitanteRepository.findById(anyLong())).thenReturn(Optional.of(visitante));
        when(statusRepository.findById(eq(2L))).thenReturn(Optional.of(statusConfirmado));
        when(agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamenteBetween(anyLong(), any(), any())).thenReturn(0L);
        when(agendamentoRepository.findAgendamentosConflitantes(anyLong(), any(), any(), anyLong())).thenReturn(new ArrayList<>());
        when(agendamentoRepository.findByVisitanteIdAndDataHoraAgendamenteBetween(anyLong(), any(), any())).thenReturn(new ArrayList<>());
        
        // Mock para o agendamento atualizado
        AgendamentoVisita agendamentoAtualizado = new AgendamentoVisita();
        agendamentoAtualizado.setId(1L);
        agendamentoAtualizado.setCustodiado(custodiado);
        agendamentoAtualizado.setVisitante(visitante);
        agendamentoAtualizado.setDataHoraAgendamento(dataHoraValida);
        agendamentoAtualizado.setStatus(statusConfirmado);
        
        when(agendamentoRepository.save(any(AgendamentoVisita.class))).thenReturn(agendamentoAtualizado);
        
        // Executar método a ser testado
        AgendamentoVisitaResponseDTO resultado = agendamentoService.atualizarAgendamento(1L, atualizacaoDTO);
        
        // Verificar resultado
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("CONFIRMADO", resultado.getDescricaoStatus());
        
        // Verificar chamadas aos mocks
        verify(agendamentoRepository).findById(1L);
        verify(statusRepository).findById(2L);
        verify(agendamentoRepository).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve buscar agendamento por ID com sucesso")
    void buscarAgendamentoPorIdSucesso() {
        // Configurar mocks
        when(agendamentoRepository.findById(anyLong())).thenReturn(Optional.of(agendamento));
        
        // Executar método a ser testado
        AgendamentoVisitaResponseDTO resultado = agendamentoService.buscarAgendamentoPorId(1L);
        
        // Verificar resultado
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1L, resultado.getCustodiadoId());
        assertEquals(1L, resultado.getVisitanteId());
        
        // Verificar chamadas aos mocks
        verify(agendamentoRepository).findById(1L);
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao buscar agendamento inexistente")
    void buscarAgendamentoInexistente() {
        // Configurar mocks
        when(agendamentoRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        // Executar método e verificar exceção
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            agendamentoService.buscarAgendamentoPorId(1L);
        });
        
        // Verificar chamadas aos mocks
        verify(agendamentoRepository).findById(1L);
    }
    
    @Test
    @DisplayName("Deve listar agendamentos paginados")
    void listarAgendamentosPaginados() {
        // Configurar mocks
        Pageable pageable = PageRequest.of(0, 10);
        Page<AgendamentoVisita> pageAgendamentos = new PageImpl<>(List.of(agendamento));
        when(agendamentoRepository.findAll(any(Pageable.class))).thenReturn(pageAgendamentos);
        
        // Executar método a ser testado
        PageResponseDTO<AgendamentoVisitaResponseDTO> resultado = agendamentoService.listarAgendamentosPaginados(pageable);
        
        // Verificar resultado
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());
        
        // Verificar chamadas aos mocks
        verify(agendamentoRepository).findAll(pageable);
    }
    
    @Test
    @DisplayName("Deve cancelar agendamento com sucesso")
    void cancelarAgendamentoSucesso() {
        // Configurar mocks
        when(agendamentoRepository.findById(anyLong())).thenReturn(Optional.of(agendamento));
        when(statusRepository.findByDescricaoIgnoreCase("CANCELADO")).thenReturn(Optional.of(statusCancelado));
        
        // Executar método a ser testado
        agendamentoService.cancelarAgendamento(1L);
        
        // Verificar chamadas aos mocks
        verify(agendamentoRepository).findById(1L);
        verify(statusRepository).findByDescricaoIgnoreCase("CANCELADO");
        verify(agendamentoRepository).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar agendamento já cancelado")
    void cancelarAgendamentoJaCancelado() {
        // Configurar agendamento já cancelado
        AgendamentoVisita agendamentoCancelado = new AgendamentoVisita();
        agendamentoCancelado.setId(1L);
        agendamentoCancelado.setCustodiado(custodiado);
        agendamentoCancelado.setVisitante(visitante);
        agendamentoCancelado.setDataHoraAgendamento(dataHoraValida);
        agendamentoCancelado.setStatus(statusCancelado);
        
        // Configurar mocks
        when(agendamentoRepository.findById(anyLong())).thenReturn(Optional.of(agendamentoCancelado));
        
        // Executar método e verificar exceção
        assertThrows(OperacaoInvalidaException.class, () -> {
            agendamentoService.cancelarAgendamento(1L);
        });
        
        // Verificar chamadas aos mocks
        verify(agendamentoRepository).findById(1L);
        verify(agendamentoRepository, never()).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve lançar exceção ao tentar cancelar agendamento já realizado")
    void cancelarAgendamentoRealizado() {
        // Configurar agendamento já realizado
        AgendamentoVisita agendamentoRealizado = new AgendamentoVisita();
        agendamentoRealizado.setId(1L);
        agendamentoRealizado.setCustodiado(custodiado);
        agendamentoRealizado.setVisitante(visitante);
        agendamentoRealizado.setDataHoraAgendamento(dataHoraValida);
        agendamentoRealizado.setStatus(statusRealizado);
        
        // Configurar mocks
        when(agendamentoRepository.findById(anyLong())).thenReturn(Optional.of(agendamentoRealizado));
        
        // Executar método e verificar exceção
        assertThrows(OperacaoInvalidaException.class, () -> {
            agendamentoService.cancelarAgendamento(1L);
        });
        
        // Verificar chamadas aos mocks
        verify(agendamentoRepository).findById(1L);
        verify(agendamentoRepository, never()).save(any(AgendamentoVisita.class));
    }
    
    @Test
    @DisplayName("Deve filtrar agendamentos corretamente")
    void filtrarAgendamentos() {
        // Configurar DTO de filtro
        FiltroAgendamentoDTO filtro = new FiltroAgendamentoDTO();
        filtro.setCustodiadoId(1L);
        filtro.setDataInicio(LocalDate.now().minusDays(7));
        filtro.setDataFim(LocalDate.now().plusDays(7));
        
        // Configurar mocks
        Pageable pageable = PageRequest.of(0, 10);
        Page<AgendamentoVisita> pageAgendamentos = new PageImpl<>(List.of(agendamento));
        when(agendamentoRepository.findComFiltrosPaginado(
                anyLong(), any(), any(), any(), any(), any(Pageable.class)))
            .thenReturn(pageAgendamentos);
        
        // Executar método a ser testado
        PageResponseDTO<AgendamentoVisitaResponseDTO> resultado = agendamentoService.filtrarAgendamentos(filtro, pageable);
        
        // Verificar resultado
        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals(1, resultado.getContent().size());
        
        // Verificar chamadas aos mocks
        verify(agendamentoRepository).findComFiltrosPaginado(
            eq(1L), isNull(), any(LocalDateTime.class), any(LocalDateTime.class), isNull(), eq(pageable));
    }
}