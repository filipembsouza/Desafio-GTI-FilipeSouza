package gov.df.seape.sistema.visitas.config;

import gov.df.seape.sistema.visitas.model.*;
import gov.df.seape.sistema.visitas.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final PessoaRepository pessoaRepository;
    private final VisitanteRepository visitanteRepository;
    private final CustodiadoRepository custodiadoRepository;
    private final UnidadePenalRepository unidadePenalRepository;
    private final StatusRepository statusRepository;
    private final AgendamentoVisitaRepository agendamentoVisitaRepository;
    private final PerfilRepository perfilRepository;
    private final FuncionalidadeRepository funcionalidadeRepository;
    private final VincPerfilFuncionalidadeRepository vincPerfilFuncionalidadeRepository;
    private final UsuarioRepository usuarioRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {

        List<Status> statusList = Arrays.asList(
                createStatus("AGENDADO"),
                createStatus("CONFIRMADO"),
                createStatus("REALIZADO"),
                createStatus("CANCELADO")
        );
        statusRepository.saveAll(statusList);

        List<UnidadePenal> unidadesList = Arrays.asList(
                createUnidadePenal("Penitenciária do Distrito Federal I", "PDF I"),
                createUnidadePenal("Penitenciária do Distrito Federal II", "PDF II"),
                createUnidadePenal("Centro de Detenção Provisória", "CDP")
        );
        unidadePenalRepository.saveAll(unidadesList);

        Pessoa pessoaCustodiado1 = createPessoa("João da Silva", "111.222.333-44", LocalDate.of(1985, 5, 15));
        pessoaRepository.save(pessoaCustodiado1);
        Custodiado custodiado1 = createCustodiado(pessoaCustodiado1, "C12345", "Careca", unidadesList.get(0));
        custodiadoRepository.save(custodiado1);

        Pessoa pessoaCustodiado2 = createPessoa("Pedro Oliveira", "222.333.444-55", LocalDate.of(1990, 8, 22));
        pessoaRepository.save(pessoaCustodiado2);
        Custodiado custodiado2 = createCustodiado(pessoaCustodiado2, "C67890", "Magrão", unidadesList.get(1));
        custodiadoRepository.save(custodiado2);

        Pessoa pessoaVisitante1 = createPessoa("Maria Santos", "333.444.555-66", LocalDate.of(1988, 3, 10));
        pessoaRepository.save(pessoaVisitante1);
        Visitante visitante1 = createVisitante(pessoaVisitante1, "senha123");
        visitanteRepository.save(visitante1);

        Pessoa pessoaVisitante2 = createPessoa("Ana Souza", "444.555.666-77", LocalDate.of(1992, 7, 25));
        pessoaRepository.save(pessoaVisitante2);
        Visitante visitante2 = createVisitante(pessoaVisitante2, "senha456");
        visitanteRepository.save(visitante2);

        AgendamentoVisita agendamento1 = createAgendamento(
                custodiado1,
                visitante1,
                LocalDateTime.now().plusDays(2).withHour(14).withMinute(0).withSecond(0),
                statusList.get(0)
        );
        agendamentoVisitaRepository.save(agendamento1);

        AgendamentoVisita agendamento2 = createAgendamento(
                custodiado2,
                visitante2,
                LocalDateTime.now().plusDays(3).withHour(10).withMinute(0).withSecond(0),
                statusList.get(0)
        );
        agendamentoVisitaRepository.save(agendamento2);

        List<Perfil> perfis = Arrays.asList(
                createPerfil("ADMIN", "Administrador do Sistema"),
                createPerfil("USER", "Usuário Padrão")
        );
        perfilRepository.saveAll(perfis);

        List<Funcionalidade> funcionalidades = Arrays.asList(
                createFuncionalidade("AGENDAMENTO_VISITA_CRIAR", "api.agendamentos.create"),
                createFuncionalidade("AGENDAMENTO_VISITA_ATUALIZAR", "api.agendamentos.update"),
                createFuncionalidade("AGENDAMENTO_VISITA_CONSULTAR", "api.agendamentos.read"),
                createFuncionalidade("AGENDAMENTO_VISITA_CANCELAR", "api.agendamentos.cancel"),
                createFuncionalidade("CUSTODIADO_GERENCIAR", "api.custodiados.*"),
                createFuncionalidade("VISITANTE_GERENCIAR", "api.visitantes.*"),
                createFuncionalidade("USUARIO_GERENCIAR", "api.usuarios.*")
        );
        funcionalidadeRepository.saveAll(funcionalidades);

        for (Funcionalidade func : funcionalidades) {
            VincPerfilFuncionalidade vinculo = createVinculo(perfis.get(0), func);
            vincPerfilFuncionalidadeRepository.save(vinculo);
        }

        List<Funcionalidade> userFuncionalidades = Arrays.asList(
                funcionalidades.get(0),
                funcionalidades.get(2)
        );
        for (Funcionalidade func : userFuncionalidades) {
            VincPerfilFuncionalidade vinculo = createVinculo(perfis.get(1), func);
            vincPerfilFuncionalidadeRepository.save(vinculo);
        }

        Pessoa pessoaAdmin = createPessoa("Administrador Sistema", "555.666.777-88", LocalDate.of(1980, 1, 1));
        pessoaRepository.save(pessoaAdmin);
        Usuario usuarioAdmin = createUsuario(
                pessoaAdmin,
                "admin",
                "{bcrypt}$2a$12$8iJ2o6B.y4K4SsyJPSXo/.Nz/uGkXmQrZJHYJBBKh9jn3tXxaaPt.",
                perfis.get(0));
        usuarioRepository.save(usuarioAdmin);

        Pessoa pessoaUser = createPessoa("Usuário Padrão", "666.777.888-99", LocalDate.of(1990, 5, 5));
        pessoaRepository.save(pessoaUser);
        Usuario usuarioNormal = createUsuario(
                pessoaUser,
                "user",
                "{bcrypt}$2a$12$rWWQ/f.QLNR7OXkDKqcS4.z9BI1W1LD1JqS5vA8UpRlN2sVv3OOY.",
                perfis.get(1));
        usuarioRepository.save(usuarioNormal);

        log.info(">>> Dados de exemplo carregados com sucesso!");
    }

    // Métodos auxiliares (iguais aos anteriores)

    private Status createStatus(String descricao) {
        Status status = new Status();
        status.setDescricao(descricao);
        return status;
    }

    private UnidadePenal createUnidadePenal(String nome, String descricao) {
        UnidadePenal unidadePenal = new UnidadePenal();
        unidadePenal.setNome(nome);
        unidadePenal.setDescricao(descricao);
        return unidadePenal;
    }

    private Pessoa createPessoa(String nome, String cpf, LocalDate dataNascimento) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setCpf(cpf);
        pessoa.setDataNascimento(dataNascimento);
        return pessoa;
    }

    private Custodiado createCustodiado(Pessoa pessoa, String numeroProntuario, String vulgo, UnidadePenal unidadePenal) {
        Custodiado custodiado = new Custodiado();
        custodiado.setPessoa(pessoa);
        custodiado.setNumeroProntuario(numeroProntuario);
        custodiado.setVulgo(vulgo);
        custodiado.setUnidadePenal(unidadePenal);
        return custodiado;
    }

    private Visitante createVisitante(Pessoa pessoa, String senhaOnline) {
        Visitante visitante = new Visitante();
        visitante.setPessoa(pessoa);
        visitante.setSenhaOnline(senhaOnline);
        return visitante;
    }

    private AgendamentoVisita createAgendamento(Custodiado custodiado, Visitante visitante,
                                                LocalDateTime dataHora, Status status) {
        AgendamentoVisita agendamento = new AgendamentoVisita();
        agendamento.setCustodiado(custodiado);
        agendamento.setVisitante(visitante);
        agendamento.setDataHoraAgendamento(dataHora);
        agendamento.setStatus(status);
        return agendamento;
    }

    private Perfil createPerfil(String descricao, String nome) {
        Perfil perfil = new Perfil();
        perfil.setDescricao(descricao + " (" + nome + ")");
        return perfil;
    }

    private Funcionalidade createFuncionalidade(String descricao, String authority) {
        Funcionalidade funcionalidade = new Funcionalidade();
        funcionalidade.setDescricao(descricao);
        funcionalidade.setAuthority(authority);
        return funcionalidade;
    }

    private VincPerfilFuncionalidade createVinculo(Perfil perfil, Funcionalidade funcionalidade) {
        VincPerfilFuncionalidade vinculo = new VincPerfilFuncionalidade();
        vinculo.setPerfil(perfil);
        vinculo.setFuncionalidade(funcionalidade);
        return vinculo;
    }

    private Usuario createUsuario(Pessoa pessoa, String email, String senha, Perfil perfil) {
        Usuario usuario = new Usuario();
        usuario.setPessoa(pessoa);
        usuario.setEmail(email);
        usuario.setSenha(senha);
        usuario.setPerfil(perfil);
        return usuario;
    }
}
