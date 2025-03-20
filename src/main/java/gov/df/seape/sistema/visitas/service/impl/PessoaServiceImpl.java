package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.PessoaRequestDTO;
import gov.df.seape.sistema.visitas.dto.PessoaResponseDTO;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.repository.PessoaRepository;
import gov.df.seape.sistema.visitas.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Pessoa.
 * Gerencia operações relacionadas a dados pessoais.
 */
@Service
@Transactional
public class PessoaServiceImpl implements PessoaService {

    private final PessoaRepository pessoaRepository;

    @Autowired
    public PessoaServiceImpl(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    /**
     * Converte PessoaRequestDTO para entidade Pessoa
     */
    private Pessoa convertToEntity(@Valid PessoaRequestDTO dto) {
        return new Pessoa(
            dto.getNome(), 
            dto.getCpf(), 
            dto.getDataNascimento()
        );
    }

    /**
     * Converte entidade Pessoa para PessoaResponseDTO
     */
    private PessoaResponseDTO convertToResponseDTO(Pessoa entity) {
        return new PessoaResponseDTO(
            entity.getId(), 
            entity.getNome(), 
            entity.getCpf(), 
            entity.getDataNascimento()
        );
    }

    @Override
    @Transactional
    public PessoaResponseDTO criarPessoa(@Valid PessoaRequestDTO pessoaRequestDTO) {
        // Verificar se já existe pessoa com este CPF
        if (pessoaRepository.existsByCpf(pessoaRequestDTO.getCpf())) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com este CPF");
        }

        Pessoa pessoa = convertToEntity(pessoaRequestDTO);
        pessoa = pessoaRepository.save(pessoa);
        return convertToResponseDTO(pessoa);
    }

    @Override
    @Transactional
    public PessoaResponseDTO atualizarPessoa(Long id, @Valid PessoaRequestDTO pessoaRequestDTO) {
        Pessoa pessoaExistente = pessoaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com ID: " + id));
        
        // Verificar se o novo CPF já existe em outra pessoa
        if (!pessoaExistente.getCpf().equals(pessoaRequestDTO.getCpf()) && 
            pessoaRepository.existsByCpf(pessoaRequestDTO.getCpf())) {
            throw new IllegalArgumentException("Já existe uma pessoa cadastrada com este CPF");
        }

        pessoaExistente.setNome(pessoaRequestDTO.getNome());
        pessoaExistente.setCpf(pessoaRequestDTO.getCpf());
        pessoaExistente.setDataNascimento(pessoaRequestDTO.getDataNascimento());
        
        pessoaExistente = pessoaRepository.save(pessoaExistente);
        
        return convertToResponseDTO(pessoaExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaResponseDTO> buscarPessoaPorId(Long id) {
        return pessoaRepository.findById(id)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PessoaResponseDTO> buscarPessoaPorCpf(String cpf) {
        return pessoaRepository.findByCpf(cpf)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarTodasPessoas() {
        return pessoaRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PessoaResponseDTO> listarPessoasPaginado(Pageable pageable) {
        return pessoaRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    @Override
    @Transactional
    public void excluirPessoa(Long id) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com ID: " + id));
        
        pessoaRepository.delete(pessoa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> buscarPessoaPorNome(String nome) {
        return pessoaRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> buscarPessoaPorIntervaloNascimento(LocalDate dataInicio, LocalDate dataFim) {
        return pessoaRepository.findByDataNascimentoBetween(dataInicio, dataFim).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
}