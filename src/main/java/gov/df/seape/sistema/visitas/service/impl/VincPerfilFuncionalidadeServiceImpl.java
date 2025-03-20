package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeResponseDTO;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Perfil;
import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.model.VincPerfilFuncionalidade;
import gov.df.seape.sistema.visitas.repository.PerfilRepository;
import gov.df.seape.sistema.visitas.repository.FuncionalidadeRepository;
import gov.df.seape.sistema.visitas.repository.VincPerfilFuncionalidadeRepository;
import gov.df.seape.sistema.visitas.service.VincPerfilFuncionalidadeService;
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
 * Implementação do serviço de VincPerfilFuncionalidade.
 * Gerencia operações relacionadas aos vínculos entre perfis e funcionalidades.
 */
@Service
@Transactional
public class VincPerfilFuncionalidadeServiceImpl implements VincPerfilFuncionalidadeService {

    private final VincPerfilFuncionalidadeRepository vincRepository;
    private final PerfilRepository perfilRepository;
    private final FuncionalidadeRepository funcionalidadeRepository;

    @Autowired
    public VincPerfilFuncionalidadeServiceImpl(
        VincPerfilFuncionalidadeRepository vincRepository,
        PerfilRepository perfilRepository,
        FuncionalidadeRepository funcionalidadeRepository
    ) {
        this.vincRepository = vincRepository;
        this.perfilRepository = perfilRepository;
        this.funcionalidadeRepository = funcionalidadeRepository;
    }

    /**
     * Converte VincPerfilFuncionalidadeRequestDTO para entidade VincPerfilFuncionalidade
     */
    private VincPerfilFuncionalidade convertToEntity(@Valid VincPerfilFuncionalidadeRequestDTO dto) {
        Perfil perfil = perfilRepository.findById(dto.getPerfilId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado com ID: " + dto.getPerfilId()));
        
        Funcionalidade funcionalidade = funcionalidadeRepository.findById(dto.getFuncionalidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionalidade não encontrada com ID: " + dto.getFuncionalidadeId()));
        
        return new VincPerfilFuncionalidade(perfil, funcionalidade);
    }

    /**
     * Converte entidade VincPerfilFuncionalidade para VincPerfilFuncionalidadeResponseDTO
     */
    private VincPerfilFuncionalidadeResponseDTO convertToResponseDTO(VincPerfilFuncionalidade entity) {
        return new VincPerfilFuncionalidadeResponseDTO(
            entity.getId(),
            new PerfilResponseDTO(
                entity.getPerfil().getId(), 
                entity.getPerfil().getDescricao(), 
                null
            ),
            new FuncionalidadeResponseDTO(
                entity.getFuncionalidade().getId(), 
                entity.getFuncionalidade().getDescricao(), 
                entity.getFuncionalidade().getAuthority()
            )
        );
    }

    @Override
    @Transactional
    public VincPerfilFuncionalidadeResponseDTO criarVinculo(@Valid VincPerfilFuncionalidadeRequestDTO vinculoRequestDTO) {
        // Verificar se o vínculo já existe
        if (vincRepository.existsByPerfilIdAndFuncionalidadeId(
            vinculoRequestDTO.getPerfilId(), 
            vinculoRequestDTO.getFuncionalidadeId()
        )) {
            throw new IllegalArgumentException("Vínculo entre perfil e funcionalidade já existe");
        }

        VincPerfilFuncionalidade vinculo = convertToEntity(vinculoRequestDTO);
        vinculo = vincRepository.save(vinculo);
        return convertToResponseDTO(vinculo);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VincPerfilFuncionalidadeResponseDTO> buscarVinculoPorId(Long id) {
        return vincRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VincPerfilFuncionalidadeResponseDTO> listarTodosVinculos() {
        return vincRepository.findAllOrderByPerfilAndFuncionalidade().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VincPerfilFuncionalidadeResponseDTO> listarVinculosPaginado(Pageable pageable) {
        return vincRepository.findAllOrderByPerfilAndFuncionalidade(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public void excluirVinculo(Long id) {
        VincPerfilFuncionalidade vinculo = vincRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vínculo não encontrado com ID: " + id));
        
        vincRepository.delete(vinculo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VincPerfilFuncionalidadeResponseDTO> listarVinculosPorPerfil(Long perfilId) {
        return vincRepository.findByPerfilId(perfilId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<VincPerfilFuncionalidadeResponseDTO> listarVinculosPorFuncionalidade(Long funcionalidadeId) {
        return vincRepository.findByFuncionalidadeId(funcionalidadeId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeVinculo(Long perfilId, Long funcionalidadeId) {
        return vincRepository.existsByPerfilIdAndFuncionalidadeId(perfilId, funcionalidadeId);
    }
}