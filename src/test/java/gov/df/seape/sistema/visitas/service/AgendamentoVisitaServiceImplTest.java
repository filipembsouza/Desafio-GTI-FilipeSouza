package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaDTO;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.*;
import gov.df.seape.sistema.visitas.repository.AgendamentoVisitaRepository;
import gov.df.seape.sistema.visitas.service.impl.AgendamentoVisitaServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendamentoVisitaServiceImplTest {

    @Mock
    private AgendamentoVisitaRepository agendamentoVisitaRepository;

    @InjectMocks
    private AgendamentoVisitaServiceImpl agendamentoVisitaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarExcecaoQuandoAgendamentoNaoExiste() {
        // Simulando um ID inexistente
        Long idInexistente = 999L;

        when(agendamentoVisitaRepository.findById(idInexistente)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            agendamentoVisitaService.cancelarVisita(idInexistente);
        });
    }

    @Test
    void deveConverterAgendamentoParaDTO() {
        // Criar objetos necessários para evitar NullPointer
        
        // Criar pessoa para o custodiado
        Pessoa pessoaCustodiado = new Pessoa();
        pessoaCustodiado.setId(10L);
        pessoaCustodiado.setNome("João da Silva");
        pessoaCustodiado.setCpf("12345678901");
        pessoaCustodiado.setDataNascimento(LocalDate.of(1985, 5, 15));
        
        // Criar unidade penal
        UnidadePenal unidadePenal = new UnidadePenal();
        unidadePenal.setId(5L);
        unidadePenal.setNome("Penitenciária Central");
        
        // Criar custodiado
        Custodiado custodiado = new Custodiado();
        custodiado.setId(1L);
        custodiado.setPessoa(pessoaCustodiado);
        custodiado.setNumeroProntuario("C12345");
        custodiado.setUnidadePenal(unidadePenal);
        
        // Criar pessoa para o visitante
        Pessoa pessoaVisitante = new Pessoa();
        pessoaVisitante.setId(20L);
        pessoaVisitante.setNome("Maria Oliveira");
        pessoaVisitante.setCpf("98765432101");
        pessoaVisitante.setDataNascimento(LocalDate.of(1990, 8, 20));
        
        // Criar visitante
        Visitante visitante = new Visitante();
        visitante.setId(2L);
        visitante.setPessoa(pessoaVisitante);
        
        // Criar status
        Status status = new Status();
        status.setId(1L);
        status.setDescricao("AGENDADO");
        
        // Criar agendamento completo
        AgendamentoVisita agendamento = new AgendamentoVisita();
        agendamento.setId(1L);
        agendamento.setCustodiado(custodiado);
        agendamento.setVisitante(visitante);
        agendamento.setStatus(status);
        agendamento.setDataHoraAgendamento(LocalDateTime.now().plusDays(1));
       
        // Converter para DTO
        AgendamentoVisitaDTO dto = new AgendamentoVisitaDTO(agendamento);

        // Verificações
        assertNotNull(dto);
        assertEquals(agendamento.getId(), dto.getId());
        assertEquals(custodiado.getId(), dto.getCustodiadoId());
        assertEquals(visitante.getId(), dto.getVisitanteId());
        assertEquals(status.getId(), dto.getStatusId());
        assertEquals("João da Silva", dto.getNomeCustodiado());
        assertEquals("Maria Oliveira", dto.getNomeVisitante());
        assertEquals("AGENDADO", dto.getDescricaoStatus());
    }
}