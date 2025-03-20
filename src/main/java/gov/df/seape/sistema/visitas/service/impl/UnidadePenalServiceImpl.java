package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.UnidadePenalRequestDTO;
import gov.df.seape.sistema.visitas.dto.UnidadePenalResponseDTO;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.UnidadePenal;
import gov.df.seape.sistema.visitas.repository.UnidadePenalRepository;
import gov.df.seape.sistema.visitas.service.UnidadePenalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Unidade Penal.
 * Gerencia operações relacionadas às unidades prisionais.
 */
@Service
@Transactional
public class UnidadePenalServiceImpl implements UnidadePenalService {

    private final UnidadePenalRepository unidadePenalRepository;

    @Autowired
    public UnidadePenalServiceImpl(UnidadePenalRepository unidadePenalRepository) {
        this.unidadePenalRepository = unidadePenalRepository;
    }

    /**
     * Converte UnidadePenalRequestDTO para entidade UnidadePenal
     */
    private UnidadePenal convertToEntity(@Valid UnidadePenalRequestDTO dto) {
        UnidadePenal unidadePenal = new UnidadePenal();
        unidadePenal.setNome(dto.getNome());
        unidadePenal.setDescricao(dto.getDescricao());
        return unidadePenal;
    }

    /**
     * Converte entidade UnidadePenal para UnidadePenalResponseDTO
     */
    private UnidadePenalResponseDTO convertToResponseDTO(UnidadePenal entity) {
        UnidadePenalResponseDTO dto = new UnidadePenalResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        return dto;
    }

    @Override
    @Transactional
    public UnidadePenalResponseDTO criarUnidadePenal(@Valid UnidadePenalRequestDTO unidadePenalRequestDTO) {
        UnidadePenal unidadePenal = convertToEntity(unidadePenalRequestDTO);
        unidadePenal = unidadePenalRepository.save(unidadePenal);
        return convertToResponseDTO(unidadePenal);
    }

    @Override
    @Transactional
    public UnidadePenalResponseDTO atualizarUnidadePenal(Long id, @Valid UnidadePenalRequestDTO unidadePenalRequestDTO) {
        UnidadePenal unidadePenalExistente = unidadePenalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade Penal não encontrada com ID: " + id));
        
        unidadePenalExistente.setNome(unidadePenalRequestDTO.getNome());
        unidadePenalExistente.setDescricao(unidadePenalRequestDTO.getDescricao());
        
        unidadePenalExistente = unidadePenalRepository.save(unidadePenalExistente);
        
        return convertToResponseDTO(unidadePenalExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UnidadePenalResponseDTO> buscarUnidadePenalPorId(Long id) {
        return unidadePenalRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadePenalResponseDTO> listarTodasUnidadesPenais() {
        return unidadePenalRepository.findAllOrderByNome().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnidadePenalResponseDTO> listarUnidadesPenaisPaginado(Pageable pageable) {
        return unidadePenalRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public void excluirUnidadePenal(Long id) {
        UnidadePenal unidadePenal = unidadePenalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade Penal não encontrada com ID: " + id));
        
        unidadePenalRepository.delete(unidadePenal);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadePenalResponseDTO> buscarUnidadePenalPorNome(String nome) {
        return unidadePenalRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
}