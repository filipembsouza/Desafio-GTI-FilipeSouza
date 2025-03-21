package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.StatusRequestDTO;
import gov.df.seape.sistema.visitas.dto.StatusResponseDTO;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Status;
import gov.df.seape.sistema.visitas.repository.AgendamentoVisitaRepository;
import gov.df.seape.sistema.visitas.repository.StatusRepository;
import gov.df.seape.sistema.visitas.service.StatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Implementação do serviço de Status
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;
    private final AgendamentoVisitaRepository agendamentoVisitaRepository;
    
    // Lista de status padrão do sistema que não devem ser modificados ou excluídos
    private static final List<String> STATUS_SISTEMA = Arrays.asList(
            "AGENDADO", "CONFIRMADO", "REALIZADO", "CANCELADO"
    );

    /**
     * Verifica se o status é um status do sistema.
     *
     * @param descricao Descrição do status
     * @return true se for um status do sistema, false caso contrário
     */
    private boolean isStatusSistema(String descricao) {
        return STATUS_SISTEMA.contains(descricao.toUpperCase());
    }

    /**
     * Converte StatusRequestDTO para entidade Status
     */
    private Status convertToEntity(StatusRequestDTO dto) {
        Status status = new Status();
        status.setDescricao(dto.getDescricao().toUpperCase());
        return status;
    }

    /**
     * Converte entidade Status para StatusResponseDTO
     */
    private StatusResponseDTO convertToResponseDTO(Status entity) {
        StatusResponseDTO dto = new StatusResponseDTO();
        dto.setId(entity.getId());
        dto.setDescricao(entity.getDescricao());
        return dto;
    }

    @Override
    @Transactional
    public StatusResponseDTO criarStatus(StatusRequestDTO statusRequestDTO) {
        log.info("Criando novo status: {}", statusRequestDTO.getDescricao());

        // Verificar se já existe um status com esta descrição
        Optional<Status> statusExistente = statusRepository.findByDescricaoIgnoreCase(statusRequestDTO.getDescricao());
        if (statusExistente.isPresent()) {
            log.warn("Tentativa de criar status duplicado: {}", statusRequestDTO.getDescricao());
            throw new OperacaoInvalidaException(
                    "Já existe um status com a descrição: " + statusRequestDTO.getDescricao()
            );
        }

        Status status = convertToEntity(statusRequestDTO);
        status = statusRepository.save(status);
        log.info("Status criado com sucesso. ID: {}", status.getId());

        return convertToResponseDTO(status);
    }

    @Override
    @Transactional
    public StatusResponseDTO atualizarStatus(Long id, StatusRequestDTO statusRequestDTO) {
        log.info("Atualizando status com ID: {}", id);

        Status statusExistente = statusRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status não encontrado com ID: " + id));

        // Verificar se já existe outro status com esta descrição
        Optional<Status> statusComMesmaDescricao =
                statusRepository.findByDescricaoIgnoreCase(statusRequestDTO.getDescricao());

        if (statusComMesmaDescricao.isPresent()
                && !statusComMesmaDescricao.get().getId().equals(id)) {
            log.warn("Tentativa de atualizar status com descrição duplicada: {}", statusRequestDTO.getDescricao());
            throw new OperacaoInvalidaException(
                    "Já existe outro status com a descrição: " + statusRequestDTO.getDescricao()
            );
        }

        // Verificar se é um status do sistema
        String descricaoOriginal = statusExistente.getDescricao();
        if (isStatusSistema(descricaoOriginal)
                && !statusRequestDTO.getDescricao().equalsIgnoreCase(descricaoOriginal)) {
            log.warn("Tentativa de modificar status do sistema: {} para {}", descricaoOriginal, statusRequestDTO.getDescricao());
            throw new OperacaoInvalidaException(
                    "Não é permitido alterar a descrição de um status do sistema."
            );
        }

        statusExistente.setDescricao(statusRequestDTO.getDescricao().toUpperCase());
        statusExistente = statusRepository.save(statusExistente);
        log.info("Status atualizado com sucesso. ID: {}", statusExistente.getId());

        return convertToResponseDTO(statusExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusResponseDTO> buscarStatusPorId(Long id) {
        log.info("Buscando status por ID: {}", id);
        return statusRepository.findById(id).map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusResponseDTO> listarTodosStatus() {
        log.info("Listando todos os status");
        // Substituir Collectors.toList() por Stream.toList()
        return statusRepository.findAllOrderByDescricao().stream()
                .map(this::convertToResponseDTO)
                .toList(); // Disponível em Java 16+
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<StatusResponseDTO> listarStatusPaginado(Pageable pageable) {
        log.info("Listando status com paginação");

        // Retorno imediato em vez de usar variável intermediária
        return new PageResponseDTO<>(
                statusRepository.findAll(pageable).map(this::convertToResponseDTO)
        );
    }

    @Override
    @Transactional
    public void excluirStatus(Long id) {
        log.info("Excluindo status com ID: {}", id);

        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Status não encontrado com ID: " + id));

        // Verificar se é um status do sistema
        if (isStatusSistema(status.getDescricao())) {
            log.warn("Tentativa de excluir status do sistema: {}", status.getDescricao());
            throw new OperacaoInvalidaException("Não é permitido excluir status do sistema.");
        }

        // Verificar se há agendamentos com este status
        // (Método countByStatusId(Long) precisa existir em AgendamentoVisitaRepository)
        if (agendamentoVisitaRepository.countByStatusId(id) > 0) {
            log.warn("Tentativa de excluir status em uso: {}", status.getDescricao());
            throw new OperacaoInvalidaException(
                    "Não é possível excluir este status pois existem agendamentos associados a ele. " +
                    "Atualize os agendamentos antes de excluir o status."
            );
        }

        statusRepository.delete(status);
        log.info("Status excluído com sucesso. ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusResponseDTO> buscarStatusPorDescricao(String descricao) {
        log.info("Buscando status por descrição contendo: {}", descricao);
        
        // Também substituindo .collect(Collectors.toList()) por .toList()
        return statusRepository.findAllOrderByDescricao().stream()
                .filter(status -> status.getDescricao().toLowerCase().contains(descricao.toLowerCase()))
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusResponseDTO> buscarPorDescricaoExata(String descricao) {
        log.info("Buscando status por descrição exata: {}", descricao);
        return statusRepository.findByDescricaoIgnoreCase(descricao)
                .map(this::convertToResponseDTO);
    }
}
