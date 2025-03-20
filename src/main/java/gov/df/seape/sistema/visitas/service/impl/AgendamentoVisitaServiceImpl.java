package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaDTO;
import gov.df.seape.sistema.visitas.dto.FiltroAgendamentoDTO;
import gov.df.seape.sistema.visitas.exception.AgendamentoConflitanteException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.AgendamentoVisita;
import gov.df.seape.sistema.visitas.model.Status;
import gov.df.seape.sistema.visitas.repository.AgendamentoVisitaRepository;
import gov.df.seape.sistema.visitas.repository.StatusRepository;
import gov.df.seape.sistema.visitas.service.AgendamentoVisitaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de agendamentos de visitas.
 */
@Service
@RequiredArgsConstructor
public class AgendamentoVisitaServiceImpl implements AgendamentoVisitaService {

    private final AgendamentoVisitaRepository agendamentoRepository;
    private final StatusRepository statusRepository;

    @Override
    @Transactional
    public AgendamentoVisitaDTO agendarVisita(AgendamentoVisitaDTO dto) {
         LocalDate dataVisita = dto.getDataHoraAgendamento().toLocalDate();
    long visitasNoMesmoDia = agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamentoBetween( //* Verifica quantas visitas já foram agendadas para este custodiado no mesmo dia

            dto.getCustodiadoId(), 
            dataVisita.atStartOfDay(), 
            dataVisita.atTime(23, 59));

    if (visitasNoMesmoDia >= 2) {
        throw new AgendamentoConflitanteException("Este custodiado já atingiu o limite de 2 visitantes para este dia.");
    }
        Status statusAgendado = statusRepository.findByDescricaoIgnoreCase("AGENDADO")
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status AGENDADO não encontrado"));

        AgendamentoVisita agendamento = new AgendamentoVisita();
        agendamento.setDataHoraAgendamento(dto.getDataHoraAgendamento());
        agendamento.setStatus(statusAgendado);

        return new AgendamentoVisitaDTO(agendamentoRepository.save(agendamento));
    }

    @Override
    public AgendamentoVisitaDTO atualizarVisita(Long id, AgendamentoVisitaDTO dto) {
        AgendamentoVisita agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));

        agendamento.setDataHoraAgendamento(dto.getDataHoraAgendamento());

        return new AgendamentoVisitaDTO(agendamentoRepository.save(agendamento));
    }

    @Override
    public List<AgendamentoVisitaDTO> buscarAgendamentosPorFiltros(FiltroAgendamentoDTO filtro) {
        return agendamentoRepository.findAll()
                .stream()
                .map(AgendamentoVisitaDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public void cancelarVisita(Long id) {
        AgendamentoVisita agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado"));

        Status statusCancelado = statusRepository.findByDescricaoIgnoreCase("CANCELADO")
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status CANCELADO não encontrado"));

        agendamento.setStatus(statusCancelado);
        agendamentoRepository.save(agendamento);
    }
}
