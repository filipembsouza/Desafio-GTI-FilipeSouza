package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.CustodiadoRequestDTO;
import gov.df.seape.sistema.visitas.dto.CustodiadoResponseDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Custodiado;
import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.model.UnidadePenal;
import gov.df.seape.sistema.visitas.repository.CustodiadoRepository;
import gov.df.seape.sistema.visitas.repository.PessoaRepository;
import gov.df.seape.sistema.visitas.repository.UnidadePenalRepository;
import gov.df.seape.sistema.visitas.service.CustodiadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustodiadoServiceImpl implements CustodiadoService {

    private final CustodiadoRepository custodiadoRepository;
    private final PessoaRepository pessoaRepository;
    private final UnidadePenalRepository unidadePenalRepository;

    private static final String UNIDADE_PENAL_NOT_FOUND = "Unidade Penal não encontrada com ID: ";

    @Override
    @Transactional
    public CustodiadoResponseDTO criarCustodiado(CustodiadoRequestDTO requestDTO) {
        log.info("Criando novo custodiado com prontuário: {}", requestDTO.getNumeroProntuario());

        if (custodiadoRepository.findByNumeroProntuario(requestDTO.getNumeroProntuario()).isPresent()) {
            log.warn("Tentativa de criar custodiado com prontuário duplicado: {}", requestDTO.getNumeroProntuario());
            throw new OperacaoInvalidaException("Já existe um custodiado com o prontuário: " + requestDTO.getNumeroProntuario());
        }

        if (pessoaRepository.existsByCpf(requestDTO.getCpf())) {
            log.warn("Tentativa de criar custodiado com CPF já cadastrado: {}", requestDTO.getCpf());
            throw new OperacaoInvalidaException("Já existe uma pessoa cadastrada com o CPF: " + requestDTO.getCpf());
        }

        UnidadePenal unidadePenal = unidadePenalRepository.findById(requestDTO.getUnidadePenalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(UNIDADE_PENAL_NOT_FOUND + requestDTO.getUnidadePenalId()));

        Pessoa pessoa = new Pessoa();
        pessoa.setNome(requestDTO.getNome());
        pessoa.setCpf(requestDTO.getCpf());
        pessoa.setDataNascimento(requestDTO.getDataNascimento());
        pessoa = pessoaRepository.save(pessoa);

        Custodiado custodiado = new Custodiado();
        custodiado.setPessoa(pessoa);
        custodiado.setNumeroProntuario(requestDTO.getNumeroProntuario());
        custodiado.setVulgo(requestDTO.getVulgo());
        custodiado.setUnidadePenal(unidadePenal);
        custodiado = custodiadoRepository.save(custodiado);

        log.info("Custodiado criado com sucesso. ID: {}", custodiado.getId());
        return new CustodiadoResponseDTO(custodiado);
    }

    @Override
    @Transactional
    public CustodiadoResponseDTO atualizarCustodiado(Long id, CustodiadoRequestDTO requestDTO) {
        log.info("Atualizando custodiado com ID: {}", id);

        final Custodiado custodiadoExistente = custodiadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + id));

        custodiadoRepository.findByNumeroProntuario(requestDTO.getNumeroProntuario())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        log.warn("Tentativa de atualizar custodiado com prontuário duplicado: {}", requestDTO.getNumeroProntuario());
                        throw new OperacaoInvalidaException("Já existe outro custodiado com o prontuário: " + requestDTO.getNumeroProntuario());
                    }
                });

        pessoaRepository.findByCpf(requestDTO.getCpf())
                .ifPresent(p -> {
                    if (!p.getId().equals(custodiadoExistente.getPessoa().getId())) {
                        log.warn("Tentativa de atualizar custodiado com CPF já cadastrado: {}", requestDTO.getCpf());
                        throw new OperacaoInvalidaException("Já existe outra pessoa cadastrada com o CPF: " + requestDTO.getCpf());
                    }
                });

        UnidadePenal unidadePenal = unidadePenalRepository.findById(requestDTO.getUnidadePenalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(UNIDADE_PENAL_NOT_FOUND + requestDTO.getUnidadePenalId()));

        Pessoa pessoa = custodiadoExistente.getPessoa();
        pessoa.setNome(requestDTO.getNome());
        pessoa.setCpf(requestDTO.getCpf());
        pessoa.setDataNascimento(requestDTO.getDataNascimento());
        pessoaRepository.save(pessoa);

        custodiadoExistente.setNumeroProntuario(requestDTO.getNumeroProntuario());
        custodiadoExistente.setVulgo(requestDTO.getVulgo());
        custodiadoExistente.setUnidadePenal(unidadePenal);

        Custodiado custodiadoAtualizado = custodiadoRepository.save(custodiadoExistente);

        log.info("Custodiado atualizado com sucesso. ID: {}", custodiadoAtualizado.getId());
        return new CustodiadoResponseDTO(custodiadoAtualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CustodiadoResponseDTO> listarCustodiadosPaginados(Pageable pageable) {
        log.info("Listando custodiados com paginação");
        return new PageResponseDTO<>(custodiadoRepository.findAll(pageable)
                .map(CustodiadoResponseDTO::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustodiadoResponseDTO> listarCustodiados() {
        log.info("Listando todos os custodiados");
        return custodiadoRepository.findAll().stream()
                .map(CustodiadoResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustodiadoResponseDTO buscarCustodiadoPorId(Long id) {
        log.info("Buscando custodiado por ID: {}", id);
        Custodiado custodiado = custodiadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + id));
        return new CustodiadoResponseDTO(custodiado);
    }

    @Override
    @Transactional(readOnly = true)
    public CustodiadoResponseDTO buscarPorNumeroProntuario(String numeroProntuario) {
        log.info("Buscando custodiado por número de prontuário: {}", numeroProntuario);
        Custodiado custodiado = custodiadoRepository.findByNumeroProntuario(numeroProntuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com prontuário: " + numeroProntuario));
        return new CustodiadoResponseDTO(custodiado);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CustodiadoResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        log.info("Buscando custodiados por nome contendo: {}", nome);
        return new PageResponseDTO<>(custodiadoRepository.findByNomeContainingIgnoreCase(nome, pageable)
                .map(CustodiadoResponseDTO::new));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CustodiadoResponseDTO> buscarPorUnidadePenal(Long unidadePenalId, Pageable pageable) {
        log.info("Buscando custodiados por unidade penal ID: {}", unidadePenalId);

        if (!unidadePenalRepository.existsById(unidadePenalId)) {
            throw new RecursoNaoEncontradoException(UNIDADE_PENAL_NOT_FOUND + unidadePenalId);
        }

        return new PageResponseDTO<>(custodiadoRepository.findByUnidadePenalId(unidadePenalId, pageable)
                .map(CustodiadoResponseDTO::new));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CustodiadoResponseDTO> buscarPorVulgo(String vulgo, Pageable pageable) {
        log.info("Buscando custodiados por vulgo contendo: {}", vulgo);
        return new PageResponseDTO<>(custodiadoRepository.findByVulgoContainingIgnoreCase(vulgo, pageable)
                .map(CustodiadoResponseDTO::new));
    }
}
