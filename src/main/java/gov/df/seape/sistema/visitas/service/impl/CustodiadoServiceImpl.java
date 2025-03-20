package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.CustodiadoRequestDTO;
import gov.df.seape.sistema.visitas.dto.CustodiadoResponseDTO;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Custodiado;
import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.model.UnidadePenal;
import gov.df.seape.sistema.visitas.repository.CustodiadoRepository;
import gov.df.seape.sistema.visitas.repository.PessoaRepository;
import gov.df.seape.sistema.visitas.repository.UnidadePenalRepository;
import gov.df.seape.sistema.visitas.service.CustodiadoService;
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
 * Implementação do serviço de Custodiado.
 * Gerencia operações relacionadas aos detentos no sistema prisional.
 */
@Service
@Transactional
public class CustodiadoServiceImpl implements CustodiadoService {

    private final CustodiadoRepository custodiadoRepository;
    private final PessoaRepository pessoaRepository;
    private final UnidadePenalRepository unidadePenalRepository;

    @Autowired
    public CustodiadoServiceImpl(
        CustodiadoRepository custodiadoRepository,
        PessoaRepository pessoaRepository,
        UnidadePenalRepository unidadePenalRepository
    ) {
        this.custodiadoRepository = custodiadoRepository;
        this.pessoaRepository = pessoaRepository;
        this.unidadePenalRepository = unidadePenalRepository;
    }

    /**
     * Converte CustodiadoRequestDTO para entidade Custodiado
     */
    private Custodiado convertToEntity(@Valid CustodiadoRequestDTO dto) {
        // Criar ou buscar pessoa
        Pessoa pessoa = pessoaRepository.findByCpf(dto.getCpf())
                .orElseGet(() -> {
                    Pessoa novaPessoa = new Pessoa(
                        dto.getNome(), 
                        dto.getCpf(), 
                        dto.getDataNascimento()
                    );
                    return pessoaRepository.save(novaPessoa);
                });

        // Buscar unidade penal
        UnidadePenal unidadePenal = unidadePenalRepository.findById(dto.getUnidadePenalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade Penal não encontrada com ID: " + dto.getUnidadePenalId()));

        return new Custodiado(
            pessoa, 
            dto.getNumeroProntuario(), 
            dto.getVulgo(), 
            unidadePenal
        );
    }

    /**
     * Converte entidade Custodiado para CustodiadoResponseDTO
     */
    private CustodiadoResponseDTO convertToResponseDTO(Custodiado entity) {
        return new CustodiadoResponseDTO(
            entity.getId(),
            entity.getPessoa().getNome(),
            entity.getPessoa().getCpf(),
            entity.getNumeroProntuario(),
            entity.getVulgo(),
            entity.getUnidadePenal().getId(),
            entity.getUnidadePenal().getNome()
        );
    }

    @Override
    @Transactional
    public CustodiadoResponseDTO criarCustodiado(@Valid CustodiadoRequestDTO custodiadoRequestDTO) {
        // Verificar se já existe custodiado com este número de prontuário
        if (custodiadoRepository.findByNumeroProntuario(custodiadoRequestDTO.getNumeroProntuario()).isPresent()) {
            throw new IllegalArgumentException("Já existe um custodiado com este número de prontuário");
        }

        Custodiado custodiado = convertToEntity(custodiadoRequestDTO);
        custodiado = custodiadoRepository.save(custodiado);
        return convertToResponseDTO(custodiado);
    }

    @Override
    @Transactional
    public CustodiadoResponseDTO atualizarCustodiado(Long id, @Valid CustodiadoRequestDTO custodiadoRequestDTO) {
        Custodiado custodiadoExistente = custodiadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + id));
        
        // Verificar se o novo número de prontuário já existe em outro custodiado
        custodiadoRepository.findByNumeroProntuario(custodiadoRequestDTO.getNumeroProntuario())
                .ifPresent(c -> {
                    if (!c.getId().equals(id)) {
                        throw new IllegalArgumentException("Já existe um custodiado com este número de prontuário");
                    }
                });

        // Atualizar pessoa
        Pessoa pessoa = custodiadoExistente.getPessoa();
        pessoa.setNome(custodiadoRequestDTO.getNome());
        pessoa.setCpf(custodiadoRequestDTO.getCpf());
        pessoa.setDataNascimento(custodiadoRequestDTO.getDataNascimento());
        pessoaRepository.save(pessoa);

        // Atualizar unidade penal
        UnidadePenal unidadePenal = unidadePenalRepository.findById(custodiadoRequestDTO.getUnidadePenalId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade Penal não encontrada com ID: " + custodiadoRequestDTO.getUnidadePenalId()));

        custodiadoExistente.setNumeroProntuario(custodiadoRequestDTO.getNumeroProntuario());
        custodiadoExistente.setVulgo(custodiadoRequestDTO.getVulgo());
        custodiadoExistente.setUnidadePenal(unidadePenal);
        
        custodiadoExistente = custodiadoRepository.save(custodiadoExistente);
        
        return convertToResponseDTO(custodiadoExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustodiadoResponseDTO> buscarCustodiadoPorId(Long id) {
        return custodiadoRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustodiadoResponseDTO> buscarCustodiadoPorNumeroProntuario(String numeroProntuario) {
        return custodiadoRepository.findByNumeroProntuario(numeroProntuario)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustodiadoResponseDTO> listarTodosCustodiados() {
        return custodiadoRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustodiadoResponseDTO> listarCustodiadosPaginado(Pageable pageable) {
        return custodiadoRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public void excluirCustodiado(Long id) {
        Custodiado custodiado = custodiadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + id));
        
        custodiadoRepository.delete(custodiado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustodiadoResponseDTO> buscarCustodiadoPorNome(String nome) {
        return custodiadoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustodiadoResponseDTO> buscarCustodiadoPorVulgo(String vulgo) {
        return custodiadoRepository.findByVulgoContainingIgnoreCase(vulgo).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustodiadoResponseDTO> buscarCustodiadoPorUnidadePenal(Long unidadePenalId) {
        UnidadePenal unidadePenal = unidadePenalRepository.findById(unidadePenalId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade Penal não encontrada com ID: " + unidadePenalId));
        
        return custodiadoRepository.findByUnidadePenal(unidadePenal).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
}