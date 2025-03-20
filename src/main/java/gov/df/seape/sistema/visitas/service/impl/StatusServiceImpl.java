package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.StatusRequestDTO;
import gov.df.seape.sistema.visitas.dto.StatusResponseDTO;
import gov.df.seape.sistema.visitas.model.Status;
import gov.df.seape.sistema.visitas.repository.StatusRepository;
import gov.df.seape.sistema.visitas.service.StatusService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    /**
     * Converte StatusRequestDTO para entidade Status
     */
    private Status convertToEntity(@Valid StatusRequestDTO dto) {
        Status status = new Status();
        status.setDescricao(dto.getDescricao());
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
    public StatusResponseDTO criarStatus(@Valid StatusRequestDTO statusRequestDTO) {
        Status status = convertToEntity(statusRequestDTO);
        status = statusRepository.save(status);
        return convertToResponseDTO(status);
    }

    @Override
    @Transactional
    public StatusResponseDTO atualizarStatus(Long id, @Valid StatusRequestDTO statusRequestDTO) {
        Status statusExistente = statusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status não encontrado com ID: " + id));
        
        statusExistente.setDescricao(statusRequestDTO.getDescricao());
        statusExistente = statusRepository.save(statusExistente);
        
        return convertToResponseDTO(statusExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StatusResponseDTO> buscarStatusPorId(Long id) {
        return statusRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusResponseDTO> listarTodosStatus() {
        return statusRepository.findAllOrderByDescricao().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StatusResponseDTO> listarStatusPaginado(Pageable pageable) {
        return statusRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public void excluirStatus(Long id) {
        Status status = statusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Status não encontrado com ID: " + id));
        
        statusRepository.delete(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatusResponseDTO> buscarStatusPorDescricao(String descricao) {
        return statusRepository.findAllOrderByDescricao().stream()
                .filter(status -> status.getDescricao().toLowerCase().contains(descricao.toLowerCase()))
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
}