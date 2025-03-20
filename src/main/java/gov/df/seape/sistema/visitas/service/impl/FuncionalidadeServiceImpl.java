package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.FuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.FuncionalidadeResponseDTO;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.repository.FuncionalidadeRepository;
import gov.df.seape.sistema.visitas.service.FuncionalidadeService;
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
 * Implementação do serviço de Funcionalidade.
 * Gerencia operações relacionadas às funcionalidades do sistema.
 */
@Service
@Transactional
public class FuncionalidadeServiceImpl implements FuncionalidadeService {

    private final FuncionalidadeRepository funcionalidadeRepository;

    @Autowired
    public FuncionalidadeServiceImpl(FuncionalidadeRepository funcionalidadeRepository) {
        this.funcionalidadeRepository = funcionalidadeRepository;
    }

    /**
     * Converte FuncionalidadeRequestDTO para entidade Funcionalidade
     */
    private Funcionalidade convertToEntity(@Valid FuncionalidadeRequestDTO dto) {
        return new Funcionalidade(dto.getDescricao(), dto.getAuthority());
    }

    /**
     * Converte entidade Funcionalidade para FuncionalidadeResponseDTO
     */
    private FuncionalidadeResponseDTO convertToResponseDTO(Funcionalidade entity) {
        return new FuncionalidadeResponseDTO(
            entity.getId(), 
            entity.getDescricao(), 
            entity.getAuthority()
        );
    }

    @Override
    @Transactional
    public FuncionalidadeResponseDTO criarFuncionalidade(@Valid FuncionalidadeRequestDTO funcionalidadeRequestDTO) {
        // Verificar se já existe funcionalidade com esta descrição ou authority
        if (funcionalidadeRepository.findByDescricaoIgnoreCase(funcionalidadeRequestDTO.getDescricao()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma funcionalidade com esta descrição");
        }
        
        if (funcionalidadeRepository.findByAuthorityIgnoreCase(funcionalidadeRequestDTO.getAuthority()).isPresent()) {
            throw new IllegalArgumentException("Já existe uma funcionalidade com esta authority");
        }

        Funcionalidade funcionalidade = convertToEntity(funcionalidadeRequestDTO);
        funcionalidade = funcionalidadeRepository.save(funcionalidade);
        return convertToResponseDTO(funcionalidade);
    }

    @Override
    @Transactional
    public FuncionalidadeResponseDTO atualizarFuncionalidade(Long id, @Valid FuncionalidadeRequestDTO funcionalidadeRequestDTO) {
        Funcionalidade funcionalidadeExistente = funcionalidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionalidade não encontrada com ID: " + id));
        
        // Verificar se a nova descrição ou authority já existem
        funcionalidadeRepository.findByDescricaoIgnoreCase(funcionalidadeRequestDTO.getDescricao())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        throw new IllegalArgumentException("Já existe uma funcionalidade com esta descrição");
                    }
                });
        
        funcionalidadeRepository.findByAuthorityIgnoreCase(funcionalidadeRequestDTO.getAuthority())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        throw new IllegalArgumentException("Já existe uma funcionalidade com esta authority");
                    }
                });

        funcionalidadeExistente.setDescricao(funcionalidadeRequestDTO.getDescricao());
        funcionalidadeExistente.setAuthority(funcionalidadeRequestDTO.getAuthority());
        
        funcionalidadeExistente = funcionalidadeRepository.save(funcionalidadeExistente);
        
        return convertToResponseDTO(funcionalidadeExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FuncionalidadeResponseDTO> buscarFuncionalidadePorId(Long id) {
        return funcionalidadeRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FuncionalidadeResponseDTO> buscarFuncionalidadePorDescricao(String descricao) {
        return funcionalidadeRepository.findByDescricaoIgnoreCase(descricao)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FuncionalidadeResponseDTO> buscarFuncionalidadePorAuthority(String authority) {
        return funcionalidadeRepository.findByAuthorityIgnoreCase(authority)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FuncionalidadeResponseDTO> listarTodasFuncionalidades() {
        return funcionalidadeRepository.findAllOrderByDescricao().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FuncionalidadeResponseDTO> listarFuncionalidadesPaginado(Pageable pageable) {
        return funcionalidadeRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public void excluirFuncionalidade(Long id) {
        Funcionalidade funcionalidade = funcionalidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionalidade não encontrada com ID: " + id));
        
        funcionalidadeRepository.delete(funcionalidade);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FuncionalidadeResponseDTO> buscarFuncionalidadePorDescricaoContendo(String descricao) {
        return funcionalidadeRepository.findByDescricaoContainingIgnoreCase(descricao).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
}