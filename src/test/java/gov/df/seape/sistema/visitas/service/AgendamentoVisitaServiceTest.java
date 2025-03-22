package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaRequestDTO;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaResponseDTO;
import gov.df.seape.sistema.visitas.exception.AgendamentoConflitanteException;
import gov.df.seape.sistema.visitas.exception.HorarioNaoPermitidoException;
import gov.df.seape.sistema.visitas.model.AgendamentoVisita;
import gov.df.seape.sistema.visitas.model.Custodiado;
import gov.df.seape.sistema.visitas.model.Status;
import gov.df.seape.sistema.visitas.model.Visitante;
import gov.df.seape.sistema.visitas.repository.AgendamentoVisitaRepository;
import gov.df.seape.sistema.visitas.repository.CustodiadoRepository;
import gov.df.seape.sistema.visitas.repository.StatusRepository;
import gov.df.seape.sistema.visitas.repository.VisitanteRepository;
import gov.df.seape.sistema.visitas.service.impl.AgendamentoVisitaServiceImpl;
import gov.df.seape.sistema.visitas.util.HorarioVisitaUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoVisitaServiceTest {

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
    
    // Variáveis para teste
    private AgendamentoVisitaRequestDTO request;
    private Custodiado custodiado;
    private Visitante visitante;
    private Status statusAgendado;
    private AgendamentoVisita agendamentoSalvo;
    private LocalDateTime dataHoraFutura;
    
    @BeforeEach
    void setUp() {
        // Define uma data futura válida (por exemplo, 10h de um dia futuro)
        dataHoraFutura = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(10, 0));
        
        // Cria o DTO de requisição
        request = new AgendamentoVisitaRequestDTO();
        request.setCustodiadoId(1L);
        request.setVisitanteId(2L);
        request.setDataHoraAgendamento(dataHoraFutura);
        request.setObservacoes("Teste de agendamento");
        
        // Cria um custodiado simulado
        custodiado = new Custodiado();
        custodiado.setId(1L);
        
        // Cria um visitante simulado
        visitante = new Visitante();
        visitante.setId(2L);
        
        // Cria o status "AGENDADO"
        statusAgendado = new Status();
        statusAgendado.setId(10L);
        statusAgendado.setDescricao("AGENDADO");
        
        // Cria o agendamento que será retornado no salvamento
        agendamentoSalvo = new AgendamentoVisita();
        agendamentoSalvo.setId(100L);
        agendamentoSalvo.setDataHoraAgendamento(dataHoraFutura);
        agendamentoSalvo.setCustodiado(custodiado);
        agendamentoSalvo.setVisitante(visitante);
        agendamentoSalvo.setStatus(statusAgendado);
    }
    
    @Test
    void criarAgendamentoSucesso() {
        // Simula que o horário é permitido
        when(horarioVisitaUtil.isHorarioPermitido(dataHoraFutura)).thenReturn(true);
        
        // Simula a busca por custodiado, visitante e status
        when(custodiadoRepository.findById(request.getCustodiadoId())).thenReturn(Optional.of(custodiado));
        when(visitanteRepository.findById(request.getVisitanteId())).thenReturn(Optional.of(visitante));
        when(statusRepository.findByDescricaoIgnoreCase("AGENDADO")).thenReturn(Optional.of(statusAgendado));
        
        // Simula que não há agendamentos no mesmo dia
        when(agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamentoBetween(
                eq(custodiado.getId()),
                any(),
                any())).thenReturn(0L);
        
        // Simula que não há conflitos para o custodiado
        when(agendamentoRepository.findAgendamentosConflitantes(
                eq(custodiado.getId()),
                any(),
                any(),
                anyLong())).thenReturn(new ArrayList<>());
        
        // Simula que o visitante não possui conflito
        when(agendamentoRepository.findByVisitanteIdAndDataHoraAgendamentoBetween(
                eq(visitante.getId()),
                any(),
                any())).thenReturn(new ArrayList<>());
        
        // Simula o salvamento do agendamento
        when(agendamentoRepository.save(any(AgendamentoVisita.class))).thenReturn(agendamentoSalvo);
        
        // Chama o método de criação
        AgendamentoVisitaResponseDTO response = agendamentoService.criarAgendamento(request);
        
        // Verifica o resultado
        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("AGENDADO", response.getDescricaoStatus());
    }
    
    @Test
    void criarAgendamentoHorarioNaoPermitido() {
        // Simula que o horário não é permitido
        when(horarioVisitaUtil.isHorarioPermitido(dataHoraFutura)).thenReturn(false);
        
        // Espera que seja lançada a exceção
        assertThrows(HorarioNaoPermitidoException.class, () -> {
            agendamentoService.criarAgendamento(request);
        });
        
        // Verifica que o repositório não foi chamado para salvar
        verify(agendamentoRepository, never()).save(any(AgendamentoVisita.class));
    }
    
    @Test
    void criarAgendamentoConflito() {
        // Simula horário permitido e entidades encontradas
        when(horarioVisitaUtil.isHorarioPermitido(dataHoraFutura)).thenReturn(true);
        when(custodiadoRepository.findById(request.getCustodiadoId())).thenReturn(Optional.of(custodiado));
        when(visitanteRepository.findById(request.getVisitanteId())).thenReturn(Optional.of(visitante));
        when(statusRepository.findByDescricaoIgnoreCase("AGENDADO")).thenReturn(Optional.of(statusAgendado));
        when(agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamentoBetween(
                eq(custodiado.getId()),
                any(),
                any())).thenReturn(0L);
        
        // Simula que já existe um agendamento conflitante
        List<AgendamentoVisita> conflito = new ArrayList<>();
        conflito.add(agendamentoSalvo);
        when(agendamentoRepository.findAgendamentosConflitantes(
                eq(custodiado.getId()),
                any(),
                any(),
                anyLong())).thenReturn(conflito);
        
        // Espera que a exceção de conflito seja lançada
        assertThrows(AgendamentoConflitanteException.class, () -> {
            agendamentoService.criarAgendamento(request);
        });
        
        verify(agendamentoRepository, never()).save(any(AgendamentoVisita.class));
    }
    
    @Test
    void cancelarAgendamentoSucesso() {
        // Simula que o agendamento existe e está em estado "AGENDADO"
        when(agendamentoRepository.findById(100L)).thenReturn(Optional.of(agendamentoSalvo));
        // Cria um status "CANCELADO" para simulação
        Status statusCancelado = new Status();
        statusCancelado.setId(20L);
        statusCancelado.setDescricao("CANCELADO");
        when(statusRepository.findByDescricaoIgnoreCase("CANCELADO")).thenReturn(Optional.of(statusCancelado));
        
        // Ao cancelar, simula a atualização do agendamento com o novo status
        agendamentoSalvo.setStatus(statusCancelado);
        when(agendamentoRepository.save(any(AgendamentoVisita.class))).thenReturn(agendamentoSalvo);
        
        // Executa o cancelamento
        agendamentoService.cancelarAgendamento(100L);
        
        // Verifica se o status foi atualizado
        assertEquals("CANCELADO", agendamentoSalvo.getStatus().getDescricao());
    }
}
