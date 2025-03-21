package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.PessoaRequestDTO;
import gov.df.seape.sistema.visitas.dto.PessoaResponseDTO;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.repository.CustodiadoRepository;
import gov.df.seape.sistema.visitas.repository.PessoaRepository;
import gov.df.seape.sistema.visitas.repository.UsuarioRepository;
import gov.df.seape.sistema.visitas.repository.VisitanteRepository;
import gov.df.seape.sistema.visitas.service.PessoaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository pessoaRepository;
    private final CustodiadoRepository custodiadoRepository;
    private final VisitanteRepository visitanteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public PessoaResponseDTO criarPessoa(PessoaRequestDTO pessoaRequestDTO) {
        log.info("Criando nova pessoa com CPF: {}", pessoaRequestDTO.getCpf());

        if (pessoaRepository.existsByCpf(pessoaRequestDTO.getCpf())) {
            log.warn("Tentativa de criar pessoa com CPF já cadastrado: {}", pessoaRequestDTO.getCpf());
            throw new OperacaoInvalidaException("Já existe uma pessoa cadastrada com o CPF: " + pessoaRequestDTO.getCpf());
        }

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(pessoaRequestDTO.getNome());
        pessoa.setCpf(pessoaRequestDTO.getCpf());
        pessoa.setDataNascimento(pessoaRequestDTO.getDataNascimento());

        pessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa criada com sucesso. ID: {}", pessoa.getId());

        return convertToPessoaResponseDTO(pessoa);
    }

    @Override
    @Transactional
    public PessoaResponseDTO atualizarPessoa(Long id, PessoaRequestDTO pessoaRequestDTO) {
        log.info("Atualizando pessoa com ID: {}", id);

        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com ID: " + id));

        // Verifica se existe outra pessoa com o mesmo CPF
        pessoaRepository.findByCpf(pessoaRequestDTO.getCpf())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        log.warn("Tentativa de atualizar pessoa com CPF já cadastrado: {}", pessoaRequestDTO.getCpf());
                        throw new OperacaoInvalidaException("Já existe outra pessoa cadastrada com o CPF: " + pessoaRequestDTO.getCpf());
                    }
                });

        pessoa.setNome(pessoaRequestDTO.getNome());
        pessoa.setCpf(pessoaRequestDTO.getCpf());
        pessoa.setDataNascimento(pessoaRequestDTO.getDataNascimento());

        pessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa atualizada com sucesso. ID: {}", pessoa.getId());

        return convertToPessoaResponseDTO(pessoa);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaResponseDTO> buscarPessoaPorId(Long id) {
        log.info("Buscando pessoa por ID: {}", id);
        return pessoaRepository.findById(id)
                .map(this::convertToPessoaResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaResponseDTO> buscarPessoaPorCpf(String cpf) {
        log.info("Buscando pessoa por CPF: {}", cpf);
        return pessoaRepository.findByCpf(cpf)
                .map(this::convertToPessoaResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarTodasPessoas() {
        log.info("Listando todas as pessoas");
        return pessoaRepository.findAll().stream()
                .map(this::convertToPessoaResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PessoaResponseDTO> listarPessoasPaginado(Pageable pageable) {
        log.info("Listando pessoas com paginação");
        Page<Pessoa> pagina = pessoaRepository.findAll(pageable);
        return new PageResponseDTO<>(pagina.map(this::convertToPessoaResponseDTO));
    }

    @Override
    @Transactional
    public void excluirPessoa(Long id) {
        log.info("Excluindo pessoa com ID: {}", id);
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com ID: " + id));

        // Verifica se a pessoa está vinculada a um custodiado, visitante ou usuário
        if (custodiadoRepository.findByPessoaId(id).isPresent()) {
            log.warn("Tentativa de excluir pessoa vinculada a um custodiado. ID: {}", id);
            throw new OperacaoInvalidaException("Não é possível excluir esta pessoa pois está vinculada a um custodiado.");
        }
        if (visitanteRepository.findByPessoaId(id).isPresent()) {
            log.warn("Tentativa de excluir pessoa vinculada a um visitante. ID: {}", id);
            throw new OperacaoInvalidaException("Não é possível excluir esta pessoa pois está vinculada a um visitante.");
        }
        if (usuarioRepository.findByPessoaId(id).isPresent()) {
            log.warn("Tentativa de excluir pessoa vinculada a um usuário. ID: {}", id);
            throw new OperacaoInvalidaException("Não é possível excluir esta pessoa pois está vinculada a um usuário.");
        }

        pessoaRepository.delete(pessoa);
        log.info("Pessoa excluída com sucesso. ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> buscarPessoaPorNome(String nome) {
        log.info("Buscando pessoas por nome: {}", nome);
        // Supondo que o repositório possua um método que retorne List<Pessoa>
        return pessoaRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToPessoaResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> buscarPessoaPorIntervaloNascimento(LocalDate dataInicio, LocalDate dataFim) {
        log.info("Buscando pessoas por intervalo de data de nascimento entre {} e {}", dataInicio, dataFim);
        // Supondo que o repositório possua um método que retorne List<Pessoa>
        return pessoaRepository.findByDataNascimentoBetween(dataInicio, dataFim).stream()
                .map(this::convertToPessoaResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PessoaResponseDTO> buscarPessoasPorNome(String nome, Pageable pageable) {
        log.info("Buscando pessoas por nome com paginação: {}", nome);
        Page<Pessoa> pagina = pessoaRepository.findByNomeContainingIgnoreCase(nome, pageable);
        return new PageResponseDTO<>(pagina.map(this::convertToPessoaResponseDTO));
    }

    /**
     * Converte a entidade Pessoa para PessoaResponseDTO.
     */
    private PessoaResponseDTO convertToPessoaResponseDTO(Pessoa pessoa) {
        PessoaResponseDTO dto = new PessoaResponseDTO();
        dto.setId(pessoa.getId());
        dto.setNome(pessoa.getNome());
        dto.setCpf(pessoa.getCpf());
        dto.setDataNascimento(pessoa.getDataNascimento());
        if (pessoa.getDataNascimento() != null) {
            int idade = Period.between(pessoa.getDataNascimento(), LocalDate.now()).getYears();
            dto.setIdade(idade);
        }
        return dto;
    }
}
