package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.FuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.FuncionalidadeResponseDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.repository.FuncionalidadeRepository;
import gov.df.seape.sistema.visitas.service.FuncionalidadeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Implementação do serviço de Funcionalidades
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FuncionalidadeServiceImpl implements FuncionalidadeService {

    // Constante para mensagem de erro padronizada
    private static final String FUNC_NAO_ENCONTRADA = "Funcionalidade não encontrada com ID: ";

    private final FuncionalidadeRepository funcionalidadeRepository;

    // Lista de funcionalidades essenciais do sistema
    private static final List<String> FUNCIONALIDADES_SISTEMA = Arrays.asList(
        "api.agendamentos.create",
        "api.agendamentos.read",
        "api.agendamentos.update", 
        "api.agendamentos.cancel",
        "api.usuarios.*"
    );

    
    /**
     * Verifica se a funcionalidade é essencial para o sistema.
     *
     * @param authority Authority da funcionalidade
     * @return true se for uma funcionalidade essencial, false caso contrário
     */
    private boolean isFuncionalidadeSistema(String authority) {
        return FUNCIONALIDADES_SISTEMA.contains(authority);
    }

    @Override
    @Transactional
    public FuncionalidadeResponseDTO criarFuncionalidade(FuncionalidadeRequestDTO requestDTO) {
        log.info("Criando nova funcionalidade: {}", requestDTO.getDescricao());

        // Verificar se já existe funcionalidade com esta descrição
        if (funcionalidadeRepository.findByDescricaoIgnoreCase(requestDTO.getDescricao()).isPresent()) {
            log.warn("Tentativa de criar funcionalidade com descrição duplicada: {}", requestDTO.getDescricao());
            throw new OperacaoInvalidaException("Já existe uma funcionalidade com a descrição: " + requestDTO.getDescricao());
        }

        // Verificar se já existe funcionalidade com esta authority
        if (funcionalidadeRepository.findByAuthorityIgnoreCase(requestDTO.getAuthority()).isPresent()) {
            log.warn("Tentativa de criar funcionalidade com authority duplicada: {}", requestDTO.getAuthority());
            throw new OperacaoInvalidaException("Já existe uma funcionalidade com a authority: " + requestDTO.getAuthority());
        }

        Funcionalidade funcionalidade = new Funcionalidade();
        funcionalidade.setDescricao(requestDTO.getDescricao());
        funcionalidade.setAuthority(requestDTO.getAuthority());

        funcionalidade = funcionalidadeRepository.save(funcionalidade);
        log.info("Funcionalidade criada com sucesso. ID: {}", funcionalidade.getId());

        return new FuncionalidadeResponseDTO(funcionalidade);
    }

    @Override
    @Transactional
    public FuncionalidadeResponseDTO atualizarFuncionalidade(Long id, FuncionalidadeRequestDTO requestDTO) {
        log.info("Atualizando funcionalidade com ID: {}", id);

        Funcionalidade funcionalidade = funcionalidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(FUNC_NAO_ENCONTRADA + id));

        // Verificar se é uma funcionalidade do sistema
        if (isFuncionalidadeSistema(funcionalidade.getAuthority())) {
            log.warn("Tentativa de modificar funcionalidade do sistema: {}", funcionalidade.getAuthority());
            throw new OperacaoInvalidaException("Não é permitido modificar funcionalidades essenciais do sistema.");
        }

        // Verificar se já existe outra funcionalidade com esta descrição
        funcionalidadeRepository.findByDescricaoIgnoreCase(requestDTO.getDescricao())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        log.warn("Tentativa de atualizar funcionalidade com descrição duplicada: {}", requestDTO.getDescricao());
                        throw new OperacaoInvalidaException("Já existe outra funcionalidade com a descrição: " + requestDTO.getDescricao());
                    }
                });

        // Verificar se já existe outra funcionalidade com esta authority
        funcionalidadeRepository.findByAuthorityIgnoreCase(requestDTO.getAuthority())
                .ifPresent(f -> {
                    if (!f.getId().equals(id)) {
                        log.warn("Tentativa de atualizar funcionalidade com authority duplicada: {}", requestDTO.getAuthority());
                        throw new OperacaoInvalidaException("Já existe outra funcionalidade com a authority: " + requestDTO.getAuthority());
                    }
                });

        funcionalidade.setDescricao(requestDTO.getDescricao());
        funcionalidade.setAuthority(requestDTO.getAuthority());

        funcionalidade = funcionalidadeRepository.save(funcionalidade);
        log.info("Funcionalidade atualizada com sucesso. ID: {}", funcionalidade.getId());

        return new FuncionalidadeResponseDTO(funcionalidade);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<FuncionalidadeResponseDTO> listarFuncionalidadesPaginadas(Pageable pageable) {
        log.info("Listando funcionalidades com paginação");
        Page<Funcionalidade> pagina = funcionalidadeRepository.findAll(pageable);

        Page<FuncionalidadeResponseDTO> paginaDTO = pagina.map(FuncionalidadeResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FuncionalidadeResponseDTO> listarFuncionalidades() {
        log.info("Listando todas as funcionalidades");
        List<Funcionalidade> funcionalidades = funcionalidadeRepository.findAllOrderByDescricao();

        return funcionalidades.stream()
                .map(FuncionalidadeResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FuncionalidadeResponseDTO buscarFuncionalidadePorId(Long id) {
        log.info("Buscando funcionalidade por ID: {}", id);
        Funcionalidade funcionalidade = funcionalidadeRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(FUNC_NAO_ENCONTRADA + id));

        return new FuncionalidadeResponseDTO(funcionalidade);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<FuncionalidadeResponseDTO> buscarPorDescricao(String descricao, Pageable pageable) {
        log.info("Buscando funcionalidades por descrição contendo: {}", descricao);
        Page<Funcionalidade> pagina = funcionalidadeRepository.findByDescricaoContainingIgnoreCase(descricao, pageable);

        Page<FuncionalidadeResponseDTO> paginaDTO = pagina.map(FuncionalidadeResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<FuncionalidadeResponseDTO> buscarPorAuthority(String authority, Pageable pageable) {
        log.info("Buscando funcionalidades por authority contendo: {}", authority);

        // Em vez de usar Specification, utilize o método customizado no repositório:
        Page<Funcionalidade> pagina = funcionalidadeRepository.findByAuthorityContainingIgnoreCase(authority, pageable);

        Page<FuncionalidadeResponseDTO> paginaDTO = pagina.map(FuncionalidadeResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FuncionalidadeResponseDTO> buscarPorPerfil(Long perfilId) {
        log.info("Buscando funcionalidades por perfil ID: {}", perfilId);
        List<Funcionalidade> funcionalidades = funcionalidadeRepository.findByPerfilId(perfilId);

        return funcionalidades.stream()
                .map(FuncionalidadeResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FuncionalidadeResponseDTO> buscarNaoAssociadasAoPerfil(Long perfilId) {
        log.info("Buscando funcionalidades não associadas ao perfil ID: {}", perfilId);
        List<Funcionalidade> funcionalidades = funcionalidadeRepository.findNotInPerfil(perfilId);

        return funcionalidades.stream()
                .map(FuncionalidadeResponseDTO::new)
                .toList();
    }
}
