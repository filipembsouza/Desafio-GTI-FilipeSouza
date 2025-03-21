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
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Pessoas
 */
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
    public PessoaResponseDTO criarPessoa(PessoaRequestDTO requestDTO) {
        log.info("Criando nova pessoa com CPF: {}", requestDTO.getCpf());
        
        // Verificar se já existe pessoa com este CPF
        if (pessoaRepository.existsByCpf(requestDTO.getCpf())) {
            log.warn("Tentativa de criar pessoa com CPF já cadastrado: {}", requestDTO.getCpf());
            throw new OperacaoInvalidaException("Já existe uma pessoa cadastrada com o CPF: " + requestDTO.getCpf());
        }
        
        // Criar pessoa
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(requestDTO.getNome());
        pessoa.setCpf(requestDTO.getCpf());
        pessoa.setDataNascimento(requestDTO.getDataNascimento());
        
        pessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa criada com sucesso. ID: {}", pessoa.getId());
        
        return convertToPessoaResponseDTO(pessoa);
    }

    @Override
    @Transactional
    public PessoaResponseDTO atualizarPessoa(Long id, PessoaRequestDTO requestDTO) {
        log.info("Atualizando pessoa com ID: {}", id);
        
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com ID: " + id));
        
        // Verificar se já existe outra pessoa com este CPF
        pessoaRepository.findByCpf(requestDTO.getCpf())
                .ifPresent(p -> {
                    if (!p.getId().equals(id)) {
                        log.warn("Tentativa de atualizar pessoa com CPF já cadastrado: {}", requestDTO.getCpf());
                        throw new OperacaoInvalidaException("Já existe outra pessoa cadastrada com o CPF: " + requestDTO.getCpf());
                    }
                });
        
        // Atualizar pessoa
        pessoa.setNome(requestDTO.getNome());
        pessoa.setCpf(requestDTO.getCpf());
        pessoa.setDataNascimento(requestDTO.getDataNascimento());
        
        pessoa = pessoaRepository.save(pessoa);
        log.info("Pessoa atualizada com sucesso. ID: {}", pessoa.getId());
        
        return convertToPessoaResponseDTO(pessoa);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PessoaResponseDTO> listarPessoasPaginadas(Pageable pageable) {
        log.info("Listando pessoas com paginação");
        Page<Pessoa> pagina = pessoaRepository.findAll(pageable);
        
        Page<PessoaResponseDTO> paginaDTO = pagina.map(this::convertToPessoaResponseDTO);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarPessoas() {
        log.info("Listando todas as pessoas");
        List<Pessoa> pessoas = pessoaRepository.findAll();
        
        return pessoas.stream()
                .map(this::convertToPessoaResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PessoaResponseDTO buscarPessoaPorId(Long id) {
        log.info("Buscando pessoa por ID: {}", id);
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com ID: " + id));
        
        return convertToPessoaResponseDTO(pessoa);
    }

    @Override
    @Transactional(readOnly = true)
    public PessoaResponseDTO buscarPessoaPorCpf(String cpf) {
        log.info("Buscando pessoa por CPF: {}", cpf);
        Pessoa pessoa = pessoaRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com CPF: " + cpf));
        
        return convertToPessoaResponseDTO(pessoa);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PessoaResponseDTO> buscarPessoasPorNome(String nome, Pageable pageable) {
        log.info("Buscando pessoas por nome contendo: {}", nome);
        Page<Pessoa> pagina = pessoaRepository.findByNomeContainingIgnoreCase(nome, pageable);
        
        Page<PessoaResponseDTO> paginaDTO = pagina.map(this::convertToPessoaResponseDTO);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional
    public void excluirPessoa(Long id) {
        log.info("Excluindo pessoa com ID: {}", id);
        
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pessoa não encontrada com ID: " + id));
        
        // Verificar se a pessoa está vinculada a um custodiado
        if (custodiadoRepository.findByPessoaId(id).isPresent()) {
            log.warn("Tentativa de excluir pessoa vinculada a um custodiado. ID: {}", id);
            throw new OperacaoInvalidaException("Não é possível excluir esta pessoa pois está vinculada a um custodiado.");
        }
        
        // Verificar se a pessoa está vinculada a um visitante
        if (visitanteRepository.findByPessoaId(id).isPresent()) {
            log.warn("Tentativa de excluir pessoa vinculada a um visitante. ID: {}", id);
            throw new OperacaoInvalidaException("Não é possível excluir esta pessoa pois está vinculada a um visitante.");
        }
        
        // Verificar se a pessoa está vinculada a um usuário
        if (usuarioRepository.findByPessoaId(id).isPresent()) {
            log.warn("Tentativa de excluir pessoa vinculada a um usuário. ID: {}", id);
            throw new OperacaoInvalidaException("Não é possível excluir esta pessoa pois está vinculada a um usuário.");
        }
        
        pessoaRepository.delete(pessoa);
        log.info("Pessoa excluída com sucesso. ID: {}", id);
    }
    
    /**
     * Converte entidade Pessoa para PessoaResponseDTO
     */
    private PessoaResponseDTO convertToPessoaResponseDTO(Pessoa pessoa) {
        PessoaResponseDTO dto = new PessoaResponseDTO();
        dto.setId(pessoa.getId());
        dto.setNome(pessoa.getNome());
        dto.setCpf(pessoa.getCpf());
        dto.setDataNascimento(pessoa.getDataNascimento());
        
        // Calcular idade
        if (pessoa.getDataNascimento() != null) {
            int idade = Period.between(pessoa.getDataNascimento(), LocalDate.now()).getYears();
            dto.setIdade(idade);
        }
        
        return dto;
    }
}