package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.VincPerfilFuncionalidadeResponseDTO;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.model.Perfil;
import gov.df.seape.sistema.visitas.model.VincPerfilFuncionalidade;
import gov.df.seape.sistema.visitas.repository.FuncionalidadeRepository;
import gov.df.seape.sistema.visitas.repository.PerfilRepository;
import gov.df.seape.sistema.visitas.repository.VincPerfilFuncionalidadeRepository;
import gov.df.seape.sistema.visitas.service.VincPerfilFuncionalidadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * Implementação do serviço de Vínculos entre Perfis e Funcionalidades
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VincPerfilFuncionalidadeServiceImpl implements VincPerfilFuncionalidadeService {

    private final VincPerfilFuncionalidadeRepository vincPerfilFuncionalidadeRepository;
    private final PerfilRepository perfilRepository;
    private final FuncionalidadeRepository funcionalidadeRepository;
    
    // Lista de vínculos essenciais que não devem ser removidos
    private static final List<String> VINCULOS_ESSENCIAIS = Arrays.asList(
            "ADMIN:api.agendamentos.create",
            "ADMIN:api.agendamentos.read",
            "ADMIN:api.agendamentos.update",
            "ADMIN:api.agendamentos.cancel",
            "ADMIN:api.usuarios.*",
            "USER:api.agendamentos.read");

    /**
     * Verifica se o vínculo é essencial para o sistema.
     * 
     * @param perfilDescricao Descrição do perfil
     * @param funcionalidadeAuthority Authority da funcionalidade
     * @return true se for um vínculo essencial, false caso contrário
     */
    private boolean isVinculoEssencial(String perfilDescricao, String funcionalidadeAuthority) {
        String vinculo = perfilDescricao + ":" + funcionalidadeAuthority;
        return VINCULOS_ESSENCIAIS.contains(vinculo);
    }

    @Override
    @Transactional
    public VincPerfilFuncionalidadeResponseDTO criarVinculo(VincPerfilFuncionalidadeRequestDTO requestDTO) {
        log.info("Criando vínculo entre perfil ID: {} e funcionalidade ID: {}", 
                requestDTO.getPerfilId(), requestDTO.getFuncionalidadeId());
        
        // Buscar perfil e funcionalidade
        Perfil perfil = perfilRepository.findById(requestDTO.getPerfilId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Perfil não encontrado com ID: " + requestDTO.getPerfilId()));
        
        Funcionalidade funcionalidade = funcionalidadeRepository.findById(requestDTO.getFuncionalidadeId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionalidade não encontrada com ID: " + requestDTO.getFuncionalidadeId()));
        
        // Verificar se já existe este vínculo
        Optional<VincPerfilFuncionalidade> vinculoExistente = 
                vincPerfilFuncionalidadeRepository.findByPerfilAndFuncionalidade(perfil, funcionalidade);
        
        if (vinculoExistente.isPresent()) {
            log.warn("Tentativa de criar vínculo duplicado entre perfil: {} e funcionalidade: {}", 
                    perfil.getDescricao(), funcionalidade.getDescricao());
            throw new OperacaoInvalidaException("Este vínculo já existe.");
        }
        
        // Criar vínculo
        VincPerfilFuncionalidade vinculo = new VincPerfilFuncionalidade();
        vinculo.setPerfil(perfil);
        vinculo.setFuncionalidade(funcionalidade);
        
        vinculo = vincPerfilFuncionalidadeRepository.save(vinculo);
        log.info("Vínculo criado com sucesso. ID: {}", vinculo.getId());
        
        return new VincPerfilFuncionalidadeResponseDTO(vinculo);
    }

    @Override
    @Transactional
    public void excluirVinculo(Long id) {
        log.info("Excluindo vínculo com ID: {}", id);
        
        VincPerfilFuncionalidade vinculo = vincPerfilFuncionalidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vínculo não encontrado com ID: " + id));
        
        // Verificar se é um vínculo essencial
        if (isVinculoEssencial(vinculo.getPerfil().getDescricao(), vinculo.getFuncionalidade().getAuthority())) {
            log.warn("Tentativa de excluir vínculo essencial do sistema. Perfil: {}, Funcionalidade: {}", 
                    vinculo.getPerfil().getDescricao(), vinculo.getFuncionalidade().getDescricao());
            throw new OperacaoInvalidaException("Não é permitido excluir vínculos essenciais do sistema.");
        }
        
        vincPerfilFuncionalidadeRepository.delete(vinculo);
        log.info("Vínculo excluído com sucesso. ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<VincPerfilFuncionalidadeResponseDTO> listarVinculosPaginados(Pageable pageable) {
        log.info("Listando vínculos com paginação");
        Page<VincPerfilFuncionalidade> pagina = vincPerfilFuncionalidadeRepository.findAll(pageable);
        
        Page<VincPerfilFuncionalidadeResponseDTO> paginaDTO = pagina.map(VincPerfilFuncionalidadeResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VincPerfilFuncionalidadeResponseDTO> listarVinculos() {
        log.info("Listando todos os vínculos");
        List<VincPerfilFuncionalidade> vinculos = vincPerfilFuncionalidadeRepository.findAllOrderByPerfilAndFuncionalidade();
        
        return vinculos.stream()
                .map(VincPerfilFuncionalidadeResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VincPerfilFuncionalidadeResponseDTO buscarVinculoPorId(Long id) {
        log.info("Buscando vínculo por ID: {}", id);
        VincPerfilFuncionalidade vinculo = vincPerfilFuncionalidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vínculo não encontrado com ID: " + id));
        
        return new VincPerfilFuncionalidadeResponseDTO(vinculo);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<VincPerfilFuncionalidadeResponseDTO> buscarPorPerfil(Long perfilId, Pageable pageable) {
        log.info("Buscando vínculos por perfil ID: {}", perfilId);
        
        if (!perfilRepository.existsById(perfilId)) {
            throw new RecursoNaoEncontradoException("Perfil não encontrado com ID: " + perfilId);
        }
        
        Page<VincPerfilFuncionalidade> pagina = vincPerfilFuncionalidadeRepository.findByPerfilId(perfilId, pageable);
        
        Page<VincPerfilFuncionalidadeResponseDTO> paginaDTO = pagina.map(VincPerfilFuncionalidadeResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<VincPerfilFuncionalidadeResponseDTO> buscarPorFuncionalidade(Long funcionalidadeId, Pageable pageable) {
        log.info("Buscando vínculos por funcionalidade ID: {}", funcionalidadeId);
        
        if (!funcionalidadeRepository.existsById(funcionalidadeId)) {
            throw new RecursoNaoEncontradoException("Funcionalidade não encontrada com ID: " + funcionalidadeId);
        }
        
        Page<VincPerfilFuncionalidade> pagina = vincPerfilFuncionalidadeRepository.findByFuncionalidadeId(funcionalidadeId, pageable);
        
        Page<VincPerfilFuncionalidadeResponseDTO> paginaDTO = pagina.map(VincPerfilFuncionalidadeResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarVinculo(Long perfilId, Long funcionalidadeId) {
        log.info("Verificando existência de vínculo entre perfil ID: {} e funcionalidade ID: {}", 
                perfilId, funcionalidadeId);
        
        return vincPerfilFuncionalidadeRepository.existsByPerfilIdAndFuncionalidadeId(perfilId, funcionalidadeId);
    }
}