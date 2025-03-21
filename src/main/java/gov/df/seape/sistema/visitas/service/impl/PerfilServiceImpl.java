package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.PerfilRequestDTO;
import gov.df.seape.sistema.visitas.dto.PerfilResponseDTO;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Funcionalidade;
import gov.df.seape.sistema.visitas.model.Perfil;
import gov.df.seape.sistema.visitas.repository.FuncionalidadeRepository;
import gov.df.seape.sistema.visitas.repository.PerfilRepository;
import gov.df.seape.sistema.visitas.service.PerfilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação do serviço de Perfis de usuário.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PerfilServiceImpl implements PerfilService {

    private static final String PERFIL_NAO_ENCONTRADO = "Perfil não encontrado com ID: ";

    private final PerfilRepository perfilRepository;
    private final FuncionalidadeRepository funcionalidadeRepository;

    /**
     * Criar um novo perfil.
     *
     * @param requestDTO Dados do perfil a ser criado
     * @return O perfil criado, com ID gerado
     */
    @Override
    @Transactional
    public PerfilResponseDTO criarPerfil(PerfilRequestDTO requestDTO) {
        log.info("Criando novo perfil com nome: {}", requestDTO.getNome());

        // Mapeia o campo "nome" do DTO para a propriedade "descricao" do Perfil
        Perfil perfil = new Perfil();
        perfil.setDescricao(requestDTO.getNome());
        perfil = perfilRepository.save(perfil);

        log.info("Perfil criado com sucesso. ID: {}", perfil.getId());
        return convertToDTO(perfil);
    }

    /**
     * Atualizar um perfil existente.
     *
     * @param id         ID do perfil a ser atualizado
     * @param requestDTO Novos dados do perfil
     * @return O perfil atualizado
     */
    @Override
    @Transactional
    public PerfilResponseDTO atualizarPerfil(Long id, PerfilRequestDTO requestDTO) {
        log.info("Atualizando perfil com ID: {}", id);

        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(PERFIL_NAO_ENCONTRADO + id));

        perfil.setDescricao(requestDTO.getNome());
        perfil = perfilRepository.save(perfil);

        log.info("Perfil atualizado com sucesso. ID: {}", perfil.getId());
        return convertToDTO(perfil);
    }

    /**
     * Buscar todos os perfis com paginação.
     *
     * @param pageable Configurações de paginação
     * @return Página de perfis
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PerfilResponseDTO> listarPerfisPaginados(Pageable pageable) {
        log.info("Listando perfis com paginação");
        Page<Perfil> pagina = perfilRepository.findAll(pageable);
        Page<PerfilResponseDTO> paginaDTO = pagina.map(this::convertToDTO);
        return new PageResponseDTO<>(paginaDTO);
    }

    /**
     * Buscar todos os perfis.
     *
     * @return Lista de todos os perfis
     */
    @Override
    @Transactional(readOnly = true)
    public List<PerfilResponseDTO> listarPerfis() {
        log.info("Listando todos os perfis");
        List<Perfil> perfis = perfilRepository.findAllOrderByDescricao();
        return perfis.stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * Buscar um perfil específico pelo ID.
     *
     * @param id ID do perfil a ser buscado
     * @return O perfil encontrado
     */
    @Override
    @Transactional(readOnly = true)
    public PerfilResponseDTO buscarPerfilPorId(Long id) {
        log.info("Buscando perfil por ID: {}", id);

        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(PERFIL_NAO_ENCONTRADO + id));

        return convertToDTO(perfil);
    }

    /**
     * Buscar perfis por descrição.
     *
     * @param descricao Descrição ou parte da descrição
     * @param pageable  Configurações de paginação
     * @return Página de perfis que contêm a descrição especificada
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PerfilResponseDTO> buscarPorDescricao(String descricao, Pageable pageable) {
        log.info("Buscando perfis por descrição contendo: {}", descricao);

        Page<Perfil> pagina = perfilRepository.findByDescricaoContainingIgnoreCase(descricao, pageable);
        Page<PerfilResponseDTO> paginaDTO = pagina.map(this::convertToDTO);
        return new PageResponseDTO<>(paginaDTO);
    }

    /**
     * Adicionar funcionalidades a um perfil.
     *
     * @param perfilId           ID do perfil
     * @param funcionalidadeIds  Lista de IDs de funcionalidades a serem adicionadas
     * @return O perfil atualizado com as novas funcionalidades
     */
    @Override
    @Transactional
    public PerfilResponseDTO adicionarFuncionalidades(Long perfilId, List<Long> funcionalidadeIds) {
        log.info("Adicionando funcionalidades ao perfil com ID: {}", perfilId);

        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(PERFIL_NAO_ENCONTRADO + perfilId));

        for (Long funcId : funcionalidadeIds) {
            Funcionalidade funcionalidade = funcionalidadeRepository.findById(funcId)
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Funcionalidade não encontrada com ID: " + funcId));
            perfil.adicionarFuncionalidade(funcionalidade);
        }
        perfil = perfilRepository.save(perfil);

        log.info("Funcionalidades adicionadas com sucesso ao perfil com ID: {}", perfil.getId());
        return convertToDTO(perfil);
    }

    /**
     * Remover funcionalidades de um perfil.
     *
     * @param perfilId           ID do perfil
     * @param funcionalidadeIds  Lista de IDs de funcionalidades a serem removidas
     * @return O perfil atualizado
     */
    @Override
    @Transactional
    public PerfilResponseDTO removerFuncionalidades(Long perfilId, List<Long> funcionalidadeIds) {
        log.info("Removendo funcionalidades do perfil com ID: {}", perfilId);

        Perfil perfil = perfilRepository.findById(perfilId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(PERFIL_NAO_ENCONTRADO + perfilId));

        // Remove os vínculos que possuam os IDs de funcionalidades informados
        perfil.getVinculos().removeIf(vinculo -> funcionalidadeIds.contains(vinculo.getFuncionalidade().getId()));
        perfil = perfilRepository.save(perfil);

        log.info("Funcionalidades removidas com sucesso do perfil com ID: {}", perfil.getId());
        return convertToDTO(perfil);
    }

    /**
     * Método auxiliar para converter a entidade Perfil em PerfilResponseDTO.
     * Como Perfil possui apenas a propriedade "descricao", mapeamos "nome" do DTO para "descricao".
     * Os demais campos (CPF, dataNascimento, idade) são deixados em branco (null).
     *
     * @param perfil A entidade Perfil a ser convertida
     * @return Objeto PerfilResponseDTO preenchido
     */
    private PerfilResponseDTO convertToDTO(Perfil perfil) {
        PerfilResponseDTO dto = new PerfilResponseDTO();
        dto.setId(perfil.getId());
        dto.setNome(perfil.getDescricao());
        dto.setCpf(null);
        dto.setDataNascimento(null);
        dto.setIdade(null);
        return dto;
    }
}
