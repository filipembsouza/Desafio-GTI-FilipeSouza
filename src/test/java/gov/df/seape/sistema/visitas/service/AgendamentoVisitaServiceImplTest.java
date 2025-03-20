package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaDTO;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.AgendamentoVisita;
import gov.df.seape.sistema.visitas.repository.AgendamentoVisitaRepository;
import gov.df.seape.sistema.visitas.service.impl.AgendamentoVisitaServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
     AgendamentoVisita agendamento = new AgendamentoVisita();
     agendamento.setId(1L);
    
     AgendamentoVisitaDTO dto = new AgendamentoVisitaDTO(agendamento);

     assertNotNull(dto);
     assertEquals(agendamento.getId(), dto.getId());
    
   }

}
