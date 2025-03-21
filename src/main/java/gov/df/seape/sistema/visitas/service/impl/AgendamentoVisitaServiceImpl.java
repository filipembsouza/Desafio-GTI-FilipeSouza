package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaRequestDTO;
import gov.df.seape.sistema.visitas.dto.AgendamentoVisitaResponseDTO;
import gov.df.seape.sistema.visitas.dto.FiltroAgendamentoDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.exception.AgendamentoConflitanteException;
import gov.df.seape.sistema.visitas.exception.HorarioNaoPermitidoException;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
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
import gov.df.seape.sistema.visitas.util.HorarioVisitaUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de agendamentos de visitas.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgendamentoVisitaServiceImpl implements AgendamentoVisitaService {

    private final AgendamentoVisitaRepository agendamentoRepository;
    private final CustodiadoRepository custodiadoRepository;
    private final VisitanteRepository visitanteRepository;
    private final StatusRepository statusRepository;

    @Override
    @Transactional
    public AgendamentoVisitaResponseDTO criarAgendamento(AgendamentoVisitaRequestDTO requestDTO) {
        log.info("Iniciando criação de agendamento de visita");
        
        // Validar se o horário está dentro dos períodos permitidos
        if (!HorarioVisitaUtil.isHorarioPermitido(requestDTO.getDataHoraAgendamento())) {
            log.warn("Tentativa de agendamento em horário não permitido: {}", requestDTO.getDataHoraAgendamento());
            throw new HorarioNaoPermitidoException("Horário não permitido para visitas. As visitas são permitidas de terça a domingo, das 9h às 17h.");
        }
        
        // Buscar entidades relacionadas
        Custodiado custodiado = custodiadoRepository.findById(requestDTO.getCustodiadoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + requestDTO.getCustodiadoId()));
        
        Visitante visitante = visitanteRepository.findById(requestDTO.getVisitanteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Visitante não encontrado com ID: " + requestDTO.getVisitanteId()));
        
        Status statusAgendado = statusRepository.findByDescricaoIgnoreCase("AGENDADO")
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status AGENDADO não encontrado"));
        
        // Verificar limite de visitantes por dia para o mesmo custodiado
        LocalDate dataVisita = requestDTO.getDataHoraAgendamento().toLocalDate();
        long visitasNoMesmoDia = agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamentoBetween(
                custodiado.getId(), 
                dataVisita.atStartOfDay(), 
                dataVisita.atTime(23, 59, 59));
        
        if (visitasNoMesmoDia >= 2) {
            log.warn("Limite de visitantes por dia excedido para o custodiado ID: {}", custodiado.getId());
            throw new AgendamentoConflitanteException("Este custodiado já atingiu o limite de 2 visitantes para este dia.");
        }
        
        // Verificar se o custodiado já tem agendamento no mesmo horário
        List<AgendamentoVisita> agendamentosConflitantes = agendamentoRepository.findAgendamentosConflitantes(
                custodiado.getId(),
                requestDTO.getDataHoraAgendamento().minusHours(1),
                requestDTO.getDataHoraAgendamento().plusHours(1),
                getStatusCanceladoId());
        
        if (!agendamentosConflitantes.isEmpty()) {
            log.warn("Conflito de agendamento detectado para o custodiado ID: {}", custodiado.getId());
            throw new AgendamentoConflitanteException("Já existe um agendamento para este custodiado próximo a este horário.");
        }
        
        // Verificar se o visitante já tem agendamento no mesmo horário
        List<AgendamentoVisita> agendamentosVisitante = agendamentoRepository.findByVisitanteIdAndDataHoraAgendamentoBetween(
                visitante.getId(),
                requestDTO.getDataHoraAgendamento().minusHours(1),
                requestDTO.getDataHoraAgendamento().plusHours(1));
        
        if (!agendamentosVisitante.isEmpty()) {
            log.warn("Conflito de agendamento detectado para o visitante ID: {}", visitante.getId());
            throw new AgendamentoConflitanteException("O visitante já possui outro agendamento próximo a este horário.");
        }
        
        // Criar e salvar o agendamento
        AgendamentoVisita agendamento = new AgendamentoVisita();
        agendamento.setCustodiado(custodiado);
        agendamento.setVisitante(visitante);
        agendamento.setDataHoraAgendamento(requestDTO.getDataHoraAgendamento());
        agendamento.setStatus(statusAgendado);
        agendamento.setObservacoes(requestDTO.getObservacoes());
        
        agendamento = agendamentoRepository.save(agendamento);
        log.info("Agendamento de visita criado com sucesso. ID: {}", agendamento.getId());
        
        return new AgendamentoVisitaResponseDTO(agendamento);
    }

    @Override
    @Transactional
    public AgendamentoVisitaResponseDTO atualizarAgendamento(Long id, AgendamentoVisitaRequestDTO requestDTO) {
        log.info("Iniciando atualização de agendamento. ID: {}", id);
        
        // Buscar o agendamento existente
        AgendamentoVisita agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado com ID: " + id));
        
        // Verificar se o agendamento não está cancelado
        if (agendamento.getStatus().getDescricao().equalsIgnoreCase("CANCELADO")) {
            log.warn("Tentativa de atualizar agendamento cancelado. ID: {}", id);
            throw new OperacaoInvalidaException("Não é possível atualizar um agendamento cancelado.");
        }
        
        // Validar se o horário está dentro dos períodos permitidos
        if (!HorarioVisitaUtil.isHorarioPermitido(requestDTO.getDataHoraAgendamento())) {
            log.warn("Tentativa de agendamento em horário não permitido: {}", requestDTO.getDataHoraAgendamento());
            throw new HorarioNaoPermitidoException("Horário não permitido para visitas. As visitas são permitidas de terça a domingo, das 9h às 17h.");
        }
        
        // Verificar se há alteração no custodiado ou visitante
        boolean alterouCustodiado = !agendamento.getCustodiado().getId().equals(requestDTO.getCustodiadoId());
        boolean alterouVisitante = !agendamento.getVisitante().getId().equals(requestDTO.getVisitanteId());
        boolean alterouDataHora = !agendamento.getDataHoraAgendamento().equals(requestDTO.getDataHoraAgendamento());
        
        // Se houve alteração, buscar as novas entidades
        Custodiado custodiado = agendamento.getCustodiado();
        if (alterouCustodiado) {
            custodiado = custodiadoRepository.findById(requestDTO.getCustodiadoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + requestDTO.getCustodiadoId()));
        }
        
        Visitante visitante = agendamento.getVisitante();
        if (alterouVisitante) {
            visitante = visitanteRepository.findById(requestDTO.getVisitanteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Visitante não encontrado com ID: " + requestDTO.getVisitanteId()));
        }
        
        // Verificar limite de visitantes por dia para o mesmo custodiado se houve alteração
        if (alterouCustodiado || alterouDataHora) {
            LocalDate dataVisita = requestDTO.getDataHoraAgendamento().toLocalDate();
            long visitasNoMesmoDia = agendamentoRepository.countByCustodiadoIdAndDataHoraAgendamentoBetween(
                    custodiado.getId(), 
                    dataVisita.atStartOfDay(), 
                    dataVisita.atTime(23, 59, 59));
            
            // Não contar o agendamento atual se for o mesmo dia
            if (agendamento.getDataHoraAgendamento().toLocalDate().equals(dataVisita)) {
                visitasNoMesmoDia--;
            }
            
            if (visitasNoMesmoDia >= 2) {
                log.warn("Limite de visitantes por dia excedido para o custodiado ID: {}", custodiado.getId());
                throw new AgendamentoConflitanteException("Este custodiado já atingiu o limite de 2 visitantes para este dia.");
            }
        }
        
        // Verificar conflitos de horário com outros agendamentos se houve alteração
        if (alterouCustodiado || alterouVisitante || alterouDataHora) {
            // Verificar conflitos para o custodiado
            List<AgendamentoVisita> agendamentosConflitantes = agendamentoRepository.findAgendamentosConflitantes(
                    custodiado.getId(),
                    requestDTO.getDataHoraAgendamento().minusHours(1),
                    requestDTO.getDataHoraAgendamento().plusHours(1),
                    getStatusCanceladoId());
            
            // Remover o agendamento atual da lista de conflitos
            agendamentosConflitantes = agendamentosConflitantes.stream()
                    .filter(a -> !a.getId().equals(id))
                    .collect(Collectors.toList());
            
            if (!agendamentosConflitantes.isEmpty()) {
                log.warn("Conflito de agendamento detectado para o custodiado ID: {}", custodiado.getId());
                throw new AgendamentoConflitanteException("Já existe um agendamento para este custodiado próximo a este horário.");
            }
            
            // Verificar conflitos para o visitante
            List<AgendamentoVisita> agendamentosVisitante = agendamentoRepository.findByVisitanteIdAndDataHoraAgendamentoBetween(
                    visitante.getId(),
                    requestDTO.getDataHoraAgendamento().minusHours(1),
                    requestDTO.getDataHoraAgendamento().plusHours(1));
            
            // Remover o agendamento atual da lista de conflitos
            agendamentosVisitante = agendamentosVisitante.stream()
                    .filter(a -> !a.getId().equals(id))
                    .collect(Collectors.toList());
            
            if (!agendamentosVisitante.isEmpty()) {
                log.warn("Conflito de agendamento detectado para o visitante ID: {}", visitante.getId());
                throw new AgendamentoConflitanteException("O visitante já possui outro agendamento próximo a este horário.");
            }
        }
        
        // Atualizar o agendamento
        agendamento.setCustodiado(custodiado);
        agendamento.setVisitante(visitante);
        agendamento.setDataHoraAgendamento(requestDTO.getDataHoraAgendamento());
        agendamento.setObservacoes(requestDTO.getObservacoes());
        
        // Se status for fornecido, atualizar
        if (requestDTO.getStatusId() != null && !requestDTO.getStatusId().equals(agendamento.getStatus().getId())) {
            Status novoStatus = statusRepository.findById(requestDTO.getStatusId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Status não encontrado com ID: " + requestDTO.getStatusId()));
            
            // Validar a transição de status
            validarTransicaoStatus(agendamento.getStatus().getDescricao(), novoStatus.getDescricao());
            
            agendamento.setStatus(novoStatus);
        }
        
        agendamento = agendamentoRepository.save(agendamento);
        log.info("Agendamento de visita atualizado com sucesso. ID: {}", agendamento.getId());
        
        return new AgendamentoVisitaResponseDTO(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<AgendamentoVisitaResponseDTO> listarAgendamentosPaginados(Pageable pageable) {
        log.info("Listando agendamentos com paginação");
        Page<AgendamentoVisita> pagina = agendamentoRepository.findAll(pageable);
        
        Page<AgendamentoVisitaResponseDTO> paginaDTO = pagina.map(AgendamentoVisitaResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AgendamentoVisitaResponseDTO> listarAgendamentos() {
        log.info("Listando todos os agendamentos");
        List<AgendamentoVisita> agendamentos = agendamentoRepository.findAll();
        
        return agendamentos.stream()
                .map(AgendamentoVisitaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AgendamentoVisitaResponseDTO buscarAgendamentoPorId(Long id) {
        log.info("Buscando agendamento por ID: {}", id);
        AgendamentoVisita agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado com ID: " + id));
        
        return new AgendamentoVisitaResponseDTO(agendamento);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<AgendamentoVisitaResponseDTO> filtrarAgendamentos(FiltroAgendamentoDTO filtro, Pageable pageable) {
        log.info("Filtrando agendamentos");
        
        // Validar o filtro
        if (!filtro.isValid()) {
            log.warn("Filtro de agendamento inválido");
            throw new OperacaoInvalidaException("Filtro inválido. Se dataFim for informada, dataInicio também deve ser. Se ambas forem informadas, dataFim deve ser >= dataInicio.");
        }
        
        // Converter datas para LocalDateTime
        LocalDateTime dataInicio = null;
        if (filtro.getDataInicio() != null) {
            dataInicio = filtro.getDataInicio().atStartOfDay();
        }
        
        LocalDateTime dataFim = null;
        if (filtro.getDataFim() != null) {
            dataFim = filtro.getDataFim().atTime(23, 59, 59);
        }
        
        // Buscar agendamentos com os filtros
        Page<AgendamentoVisita> pagina = agendamentoRepository.findComFiltrosPaginado(
                filtro.getCustodiadoId(),
                filtro.getVisitanteId(),
                dataInicio,
                dataFim,
                filtro.getStatusId(),
                pageable);
        
        Page<AgendamentoVisitaResponseDTO> paginaDTO = pagina.map(AgendamentoVisitaResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<AgendamentoVisitaResponseDTO> buscarAgendamentosPorData(LocalDate data, Pageable pageable) {
        log.info("Buscando agendamentos por data: {}", data);
        Page<AgendamentoVisita> pagina = agendamentoRepository.findByData(data, pageable);
        
        Page<AgendamentoVisitaResponseDTO> paginaDTO = pagina.map(AgendamentoVisitaResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<AgendamentoVisitaResponseDTO> buscarAgendamentosPorCustodiado(Long custodiadoId, Pageable pageable) {
        log.info("Buscando agendamentos por custodiado ID: {}", custodiadoId);
        
        // Verificar se o custodiado existe
        if (!custodiadoRepository.existsById(custodiadoId)) {
            throw new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + custodiadoId);
        }
        
        Page<AgendamentoVisita> pagina = agendamentoRepository.findByCustodiadoId(custodiadoId, pageable);
        
        Page<AgendamentoVisitaResponseDTO> paginaDTO = pagina.map(AgendamentoVisitaResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<AgendamentoVisitaResponseDTO> buscarAgendamentosPorVisitante(Long visitanteId, Pageable pageable) {
        log.info("Buscando agendamentos por visitante ID: {}", visitanteId);
        
        // Verificar se o visitante existe
        if (!visitanteRepository.existsById(visitanteId)) {
            throw new RecursoNaoEncontradoException("Visitante não encontrado com ID: " + visitanteId);
        }
        
        Page<AgendamentoVisita> pagina = agendamentoRepository.findByVisitanteId(visitanteId, pageable);
        
        Page<AgendamentoVisitaResponseDTO> paginaDTO = pagina.map(AgendamentoVisitaResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional
    public void cancelarAgendamento(Long id) {
        log.info("Iniciando cancelamento de agendamento. ID: {}", id);
        
        AgendamentoVisita agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado com ID: " + id));
        
        // Verificar se o agendamento já está cancelado
        if (agendamento.getStatus().getDescricao().equalsIgnoreCase("CANCELADO")) {
            log.warn("Tentativa de cancelar agendamento já cancelado. ID: {}", id);
            throw new OperacaoInvalidaException("Agendamento já está cancelado.");
        }
        
        // Verificar se o agendamento já foi realizado
        if (agendamento.getStatus().getDescricao().equalsIgnoreCase("REALIZADO")) {
            log.warn("Tentativa de cancelar agendamento já realizado. ID: {}", id);
            throw new OperacaoInvalidaException("Não é possível cancelar um agendamento já realizado.");
        }
        
        Status statusCancelado = statusRepository.findByDescricaoIgnoreCase("CANCELADO")
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status CANCELADO não encontrado"));
        
        agendamento.setStatus(statusCancelado);
        agendamentoRepository.save(agendamento);
        log.info("Agendamento cancelado com sucesso. ID: {}", id);
    }
    
    /**
     * Obtém o ID do status cancelado.
     * 
     * @return ID do status CANCELADO
     */
    private Long getStatusCanceladoId() {
        return statusRepository.findByDescricaoIgnoreCase("CANCELADO")
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status CANCELADO não encontrado")).getId();
    }
    
    /**
     * Valida se a transição de status é permitida.
     * 
     * @param statusAtual Descrição do status atual
     * @param novoStatus Descrição do novo status
     */
    private void validarTransicaoStatus(String statusAtual, String novoStatus) {
        switch (statusAtual.toUpperCase()) {
            case "AGENDADO":
                // De AGENDADO pode ir para CONFIRMADO ou CANCELADO
                if (!novoStatus.equalsIgnoreCase("CONFIRMADO") && !novoStatus.equalsIgnoreCase("CANCELADO")) {
                    throw new OperacaoInvalidaException("Transição de status inválida. De AGENDADO só é possível ir para CONFIRMADO ou CANCELADO.");
                }
                break;
            case "CONFIRMADO":
                // De CONFIRMADO pode ir para REALIZADO ou CANCELADO
                if (!novoStatus.equalsIgnoreCase("REALIZADO") && !novoStatus.equalsIgnoreCase("CANCELADO")) {
                    throw new OperacaoInvalidaException("Transição de status inválida. De CONFIRMADO só é possível ir para REALIZADO ou CANCELADO.");
                }
                break;
            case "REALIZADO":
                // De REALIZADO não pode mudar
                throw new OperacaoInvalidaException("Não é possível alterar o status de uma visita já REALIZADA.");
            case "CANCELADO":
                // De CANCELADO não pode mudar
                throw new OperacaoInvalidaException("Não é possível alterar o status de uma visita CANCELADA.");
            default:
                break;
        }
    }
}