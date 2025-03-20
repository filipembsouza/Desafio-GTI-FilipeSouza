package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.PerfilRequestDTO;
import gov.df.seape.sistema.visitas.dto.PerfilResponseDTO;
import gov.df.seape.sistema.visitas.dto.FuncionalidadeResponseDTO;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Perfil;
import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.model.VincPerfilFuncionalidade;
import gov.df.seape.sistema.visitas.repository.PerfilRepository;
import gov.df.seape.sistema.visitas.repository.FuncionalidadeRepository;
import gov.df.seape.sistema.visitas.repository.VincPerfilFuncionalidadeRepository;
import gov.df.seape.sistema.visitas.service.PerfilService;
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
 * Implementação do serviço de Perfil.
 * Gerencia operações relacionadas aos perfis de usuário.
 */
@Service
@Transactional
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfilRepository;
    private final FuncionalidadeRepository funcionalidadeRepository;
    private final VincPerfilFuncionalidadeRepository vincRepository;

    @Autowired
    public PerfilServiceImpl(
        PerfilRepository perfilRepository, 
        FuncionalidadeRepository funcionalidadeRepository,
        VincPerfilFuncionalidadeRepository vincRepository
    ) {
        this.perfilRepository = perfilRepository;
        this.funcionalidadeRepository = funcionalidadeRepository;
        this.vincRepository = vincRepository;
    }

    /**
     * Converte PerfilRequestDTO para entidade Perfil
     */
    private Perfil convertToEntity(@Valid PerfilRequestDTO dto) {
        return new Perfil(dto.getDescricao());
    }

    /**
     * Converte entidade Perfil para PerfilResponseDTO
     */
    private PerfilResponseDTO convertToResponseDTO(Perfil entity) {
        List<FuncionalidadeResponseDTO> funcionalidades = entity.getFuncionalidades().stream()
            .map(f -> new FuncionalidadeResponseDTO(
                f.getId(), 
                f.getDescricao(), 
                f.getAuthority()
            ))
            .collect(Collectors.toList());

        return new PerfilResponseDTO(
            entity.getId(), 
            entity.getDescricao(), 
            funcionalidades
        );
    }

    @Override
    @Transactional
    public PerfilResponseDTO criarPerfil(@Valid PerfilRequestDTO perfilRequestDTO) {
        // Verificar se já existe perfil com esta descrição
        if (perfilRepository.findByDescricaoIgnoreCase(perfilRequestDTO.getDescricao()).isPresent()) {
            throw new IllegalArgumentException("Já existe um perfil com esta descrição");
        }

        Perfil perfil = convertToEntity(perfilRequestDTO);
        perfil = perfilRepository.save(perfil);
        return convertToResponseDTO(perfil);
    }

    @Override
    @Transactional
    public PerfilResponseDTO atualizarPerfil(Long id, @Valid PerfilRequestDTO perfilRequestDTO) {
        Perfil perfilExistente = perfilRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado com ID: " + id));
        
        // Verificar se a nova descrição já existe
        perfilRepository.findByDescricaoIgnoreCase(perfilRequestDTO.getDescricao())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        throw new IllegalArgumentException("Já existe um perfil com esta descrição");
                    }
                });

        perfilExistente.setDescricao(perfilRequestDTO.getDescricao());
        perfilExistente = perfilRepository.save(perfilExistente);
        
        return convertToResponseDTO(perfilExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PerfilResponseDTO> buscarPerfilPorId(Long id) {
        return perfilRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PerfilResponseDTO> buscarPerfilPorDescricao(String descricao) {
        return perfilRepository.findByDescricaoIgnoreCase(descricao)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilResponseDTO> listarTodosPerfis() {
        return perfilRepository.findAllOrderByDescricao().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PerfilResponseDTO> listarPerfisPaginado(Pageable pageable) {
        return perfilRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public void excluirPerfil(Long id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado com ID: " + id));
        
        perfilRepository.delete(perfil);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilResponseDTO> buscarPerfilPorDescricaoContendo(String descricao) {
        return perfilRepository.findByDescricaoContainingIgnoreCase(descricao).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PerfilResponseDTO adicionarFuncionalidade(Long perfilId, Long funcionalidadeId) {
        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado com ID: " + perfilId));
        
        Funcionalidade funcionalidade = funcionalidadeRepository.findById(funcionalidadeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionalidade não encontrada com ID: " + funcionalidadeId));
        
        // Verificar se o vínculo já existe
        if (!vincRepository.existsByPerfilAndFuncionalidade(perfil, funcionalidade)) {
            VincPerfilFuncionalidade vinculo = new VincPerfilFuncionalidade(perfil, funcionalidade);
            vincRepository.save(vinculo);
            perfil.getFuncionalidades().add(funcionalidade);
        }
        
        return convertToResponseDTO(perfil);
    }

    @Override
    @Transactional
    public PerfilResponseDTO removerFuncionalidade(Long perfilId, Long funcionalidadeId) {
        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado com ID: " + perfilId));
        
        Funcionalidade funcionalidade = funcionalidadeRepository.findById(funcionalidadeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionalidade não encontrada com ID: " + funcionalidadeId));
        
        // Remover o vínculo se existir
        vincRepository.findByPerfilAndFuncionalidade(perfil, funcionalidade)
                .ifPresent(vincRepository::delete);
        
        perfil.getFuncionalidades().removeIf(f -> f.getId().equals(funcionalidadeId));
        
        return convertToResponseDTO(perfil);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FuncionalidadeResponseDTO> listarFuncionalidadesDoPerfil(Long perfilId) {
        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado com ID: " + perfilId));
        
        return perfil.getFuncionalidades().stream()
                .map(f -> new FuncionalidadeResponseDTO(
                    f.getId(), 
                    f.getDescricao(), 
                    f.getAuthority()
                ))
                .collect(Collectors.toList());
    }
}