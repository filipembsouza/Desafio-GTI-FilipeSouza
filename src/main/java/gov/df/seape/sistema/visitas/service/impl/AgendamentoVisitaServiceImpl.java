package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaDTO;
import gov.df.seape.sistema.visitas.dto.FiltroAgendamentoDTO;
import gov.df.seape.sistema.visitas.exception.AgendamentoConflitanteException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.AgendamentoVisita;
import gov.df.seape.sistema.visitas.model.Custodiado;
import gov.df.seape.sistema.visitas.model.Status;
import gov.df.seape.sistema.visitas.model.Visitante;
import gov.df.seape.sistema.visitas.repository.AgendamentoVisitaRepository;
import gov.df.seape.sistema.visitas.repository.CustodiadoRepository;
import gov.df.seape.sistema.visitas.repository.StatusRepository;
import gov.df.seape.sistema.visitas.repository.VisitanteRepository;
import gov.df.seape.sistema.visitas.service.AgendamentoVisitaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final CustodiadoRepository custodiadoRepository;
    private final VisitanteRepository visitanteRepository;

    @Override
    @Transactional
    public AgendamentoVisitaDTO criarAgendamento(AgendamentoVisitaDTO dto) {
        // Verificar limite de visitas por dia
        LocalDate dataVisita = dto.getDataHoraAgendamento().toLocalDate();
        long visitasNoMesmoDia = agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamentoBetween(
                dto.getCustodiadoId(), 
                dataVisita.atStartOfDay(), 
                dataVisita.atTime(23, 59, 59));

        if (visitasNoMesmoDia >= 2) {
            throw new AgendamentoConflitanteException("Este custodiado já atingiu o limite de 2 visitantes para este dia.");
        }

        // Buscar entidades relacionadas
        Custodiado custodiado = custodiadoRepository.findById(dto.getCustodiadoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + dto.getCustodiadoId()));
        
        Visitante visitante = visitanteRepository.findById(dto.getVisitanteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Visitante não encontrado com ID: " + dto.getVisitanteId()));
        
        Status statusAgendado = statusRepository.findByDescricaoIgnoreCase("AGENDADO")
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status AGENDADO não encontrado"));

        // Criar agendamento
        AgendamentoVisita agendamento = new AgendamentoVisita();
        agendamento.setCustodiado(custodiado);
        agendamento.setVisitante(visitante);
        agendamento.setDataHoraAgendamento(dto.getDataHoraAgendamento());
        agendamento.setStatus(statusAgendado);

        return new AgendamentoVisitaDTO(agendamentoRepository.save(agendamento));
    }

    @Override
    @Transactional
    public AgendamentoVisitaDTO atualizarAgendamento(Long id, AgendamentoVisitaDTO dto) {
        AgendamentoVisita agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado com ID: " + id));

        // Verificar se a data foi alterada e se precisamos fazer nova validação de limite
        if (!agendamento.getDataHoraAgendamento().toLocalDate().equals(dto.getDataHoraAgendamento().toLocalDate())) {
            LocalDate novaDataVisita = dto.getDataHoraAgendamento().toLocalDate();
            long visitasNoMesmoDia = agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamentoBetween(
                    dto.getCustodiadoId(), 
                    novaDataVisita.atStartOfDay(), 
                    novaDataVisita.atTime(23, 59, 59));

            // Não contamos o próprio agendamento
            if (agendamento.getCustodiado().getId().equals(dto.getCustodiadoId())) {
                visitasNoMesmoDia--;
            }

            if (visitasNoMesmoDia >= 2) {
                throw new AgendamentoConflitanteException("Este custodiado já atingiu o limite de 2 visitantes para este dia.");
            }
        }

        // Atualizar dados do agendamento
        if (dto.getCustodiadoId() != null && !dto.getCustodiadoId().equals(agendamento.getCustodiado().getId())) {
            Custodiado custodiado = custodiadoRepository.findById(dto.getCustodiadoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + dto.getCustodiadoId()));
            agendamento.setCustodiado(custodiado);
        }

        if (dto.getVisitanteId() != null && !dto.getVisitanteId().equals(agendamento.getVisitante().getId())) {
            Visitante visitante = visitanteRepository.findById(dto.getVisitanteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Visitante não encontrado com ID: " + dto.getVisitanteId()));
            agendamento.setVisitante(visitante);
        }

        if (dto.getStatusId() != null && !dto.getStatusId().equals(agendamento.getStatus().getId())) {
            Status status = statusRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Status não encontrado com ID: " + dto.getStatusId()));
            agendamento.setStatus(status);
        }

        agendamento.setDataHoraAgendamento(dto.getDataHoraAgendamento());

        return new AgendamentoVisitaDTO(agendamentoRepository.save(agendamento));
    }

    @Override
    public List<AgendamentoVisitaDTO> listarAgendamentos() {
        return agendamentoRepository.findAll()
                .stream()
                .map(AgendamentoVisitaDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public AgendamentoVisitaDTO buscarAgendamentoPorId(Long id) {
        AgendamentoVisita agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado com ID: " + id));
        return new AgendamentoVisitaDTO(agendamento);
    }

    @Override
    @Transactional
    public void cancelarAgendamento(Long id) {
        AgendamentoVisita agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado com ID: " + id));

        Status statusCancelado = statusRepository.findByDescricaoIgnoreCase("CANCELADO")
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status CANCELADO não encontrado"));

        agendamento.setStatus(statusCancelado);
        agendamentoRepository.save(agendamento);
    }

    @Override
    public List<AgendamentoVisitaDTO> filtrarAgendamentos(FiltroAgendamentoDTO filtro) {
        // Construir consulta baseada nos filtros
        LocalDateTime dataInicio = filtro.getDataInicio() != null ? filtro.getDataInicio().atStartOfDay() : null;
        LocalDateTime dataFim = filtro.getDataFim() != null ? filtro.getDataFim().atTime(23, 59, 59) : null;

        return agendamentoRepository.findComFiltrosPaginado(
                filtro.getCustodiadoId(),
                filtro.getVisitanteId(),
                dataInicio,
                dataFim,
                filtro.getStatusId(),
                null) // Sem paginação
                .stream()
                .map(AgendamentoVisitaDTO::new)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public void cancelarVisita(Long id) {
        // Este método é chamado pelo código legado ou internamente
        // Simplesmente delega para o método cancelarAgendamento
        cancelarAgendamento(id);
    }
}