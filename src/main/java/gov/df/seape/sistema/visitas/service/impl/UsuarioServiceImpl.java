package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.UsuarioRequestDTO;
import gov.df.seape.sistema.visitas.dto.UsuarioResponseDTO;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Perfil;
import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.model.Usuario;
import gov.df.seape.sistema.visitas.repository.PerfilRepository;
import gov.df.seape.sistema.visitas.repository.PessoaRepository;
import gov.df.seape.sistema.visitas.repository.UsuarioRepository;
import gov.df.seape.sistema.visitas.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementação do serviço de Usuários
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private static final String PERFIL_NAO_ENCONTRADO = "Perfil não encontrado com ID: ";
    private static final String USUARIO_NAO_ENCONTRADO = "Usuário não encontrado com ID: ";

    private final UsuarioRepository usuarioRepository;
    private final PessoaRepository pessoaRepository;
    private final PerfilRepository perfilRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO requestDTO) {
        log.info("Criando novo usuário com email: {}", requestDTO.getEmail());

        // Verificar se já existe usuário com este email
        if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            log.warn("Tentativa de criar usuário com email já cadastrado: {}", requestDTO.getEmail());
            throw new OperacaoInvalidaException("Já existe um usuário cadastrado com o email: " + requestDTO.getEmail());
        }

        // Verificar se já existe pessoa com o CPF informado
        if (pessoaRepository.existsByCpf(requestDTO.getPessoa().getCpf())) {
            log.warn("Tentativa de criar usuário com CPF já cadastrado: {}", requestDTO.getPessoa().getCpf());
            throw new OperacaoInvalidaException(
                    "Já existe uma pessoa cadastrada com o CPF: " + requestDTO.getPessoa().getCpf()
            );
        }

        // Buscar perfil
        Perfil perfil = perfilRepository.findById(requestDTO.getPerfilId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        PERFIL_NAO_ENCONTRADO + requestDTO.getPerfilId()
                ));

        // Criar pessoa
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(requestDTO.getPessoa().getNome());
        pessoa.setCpf(requestDTO.getPessoa().getCpf());
        pessoa.setDataNascimento(requestDTO.getPessoa().getDataNascimento());
        pessoa = pessoaRepository.save(pessoa);

        // Criar usuário
        Usuario usuario = new Usuario();
        usuario.setEmail(requestDTO.getEmail());
        // Criptografar senha
        usuario.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
        usuario.setPerfil(perfil);
        usuario.setPessoa(pessoa);

        usuario = usuarioRepository.save(usuario);
        log.info("Usuário criado com sucesso. ID: {}", usuario.getId());

        return new UsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO requestDTO) {
        log.info("Atualizando usuário com ID: {}", id);

        // Buscar usuário apenas uma vez
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(USUARIO_NAO_ENCONTRADO + id));

        // Verificar se já existe outro usuário com este email
        usuarioRepository.findByEmail(requestDTO.getEmail())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        log.warn("Tentativa de atualizar usuário com email já cadastrado: {}", requestDTO.getEmail());
                        throw new OperacaoInvalidaException(
                                "Já existe outro usuário cadastrado com o email: " + requestDTO.getEmail()
                        );
                    }
                });

        // *** Captura o ID da pessoa associada ao usuário em uma variável final ***
        final Long usuarioPessoaId = usuario.getPessoa().getId();

        // Verificar se já existe outra pessoa com este CPF
        pessoaRepository.findByCpf(requestDTO.getPessoa().getCpf())
                .ifPresent(p -> {
                    if (!p.getId().equals(usuarioPessoaId)) {
                        log.warn("Tentativa de atualizar usuário com CPF já cadastrado: {}",
                                requestDTO.getPessoa().getCpf());
                        throw new OperacaoInvalidaException(
                                "Já existe outra pessoa cadastrada com o CPF: " + requestDTO.getPessoa().getCpf()
                        );
                    }
                });

        // Buscar perfil
        Perfil perfil = perfilRepository.findById(requestDTO.getPerfilId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        PERFIL_NAO_ENCONTRADO + requestDTO.getPerfilId()
                ));

        // Atualizar pessoa
        Pessoa pessoa = usuario.getPessoa();
        pessoa.setNome(requestDTO.getPessoa().getNome());
        pessoa.setCpf(requestDTO.getPessoa().getCpf());
        pessoa.setDataNascimento(requestDTO.getPessoa().getDataNascimento());
        pessoaRepository.save(pessoa);

        // Atualizar usuário
        usuario.setEmail(requestDTO.getEmail());
        usuario.setPerfil(perfil);

        // Senha só é atualizada se for fornecida
        if (requestDTO.getSenha() != null && !requestDTO.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
        }

        // Aqui há reatribuição, portanto 'usuario' não é efetivamente final
        usuario = usuarioRepository.save(usuario);
        log.info("Usuário atualizado com sucesso. ID: {}", usuario.getId());

        return new UsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<UsuarioResponseDTO> listarUsuariosPaginados(Pageable pageable) {
        log.info("Listando usuários com paginação");
        Page<Usuario> pagina = usuarioRepository.findAll(pageable);

        Page<UsuarioResponseDTO> paginaDTO = pagina.map(UsuarioResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuarios() {
        log.info("Listando todos os usuários");
        List<Usuario> usuarios = usuarioRepository.findAll();

        return usuarios.stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarUsuarioPorId(Long id) {
        log.info("Buscando usuário por ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(USUARIO_NAO_ENCONTRADO + id));

        return new UsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO buscarPorEmail(String email) {
        log.info("Buscando usuário por email: {}", email);
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Usuário não encontrado com email: " + email
                ));

        return new UsuarioResponseDTO(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<UsuarioResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        log.info("Buscando usuários por nome contendo: {}", nome);
        Page<Usuario> pagina = usuarioRepository.findByNomeContainingIgnoreCase(nome, pageable);

        Page<UsuarioResponseDTO> paginaDTO = pagina.map(UsuarioResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<UsuarioResponseDTO> buscarPorPerfil(Long perfilId, Pageable pageable) {
        log.info("Buscando usuários por perfil ID: {}", perfilId);

        if (!perfilRepository.existsById(perfilId)) {
            throw new RecursoNaoEncontradoException(PERFIL_NAO_ENCONTRADO + perfilId);
        }

        Page<Usuario> pagina = usuarioRepository.findByPerfilId(perfilId, pageable);
        Page<UsuarioResponseDTO> paginaDTO = pagina.map(UsuarioResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO alterarSenha(Long id, String senhaAtual, String novaSenha) {
        log.info("Alterando senha do usuário com ID: {}", id);

        if (senhaAtual == null || novaSenha == null
                || senhaAtual.isEmpty() || novaSenha.isEmpty()) {
            throw new OperacaoInvalidaException("Senhas atual e nova devem ser informadas");
        }

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(USUARIO_NAO_ENCONTRADO + id));

        // Verificar se a senha atual está correta
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            log.warn("Tentativa de alteração de senha com senha atual incorreta. Usuário ID: {}", id);
            throw new OperacaoInvalidaException("Senha atual incorreta");
        }

        // Verifica se a nova senha é igual à atual
        if (passwordEncoder.matches(novaSenha, usuario.getSenha())) {
            throw new OperacaoInvalidaException("A nova senha não pode ser igual à senha atual");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario = usuarioRepository.save(usuario);
        log.info("Senha alterada com sucesso. Usuário ID: {}", usuario.getId());

        return new UsuarioResponseDTO(usuario);
    }
}
