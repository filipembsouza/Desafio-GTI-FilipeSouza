package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.VisitanteRequestDTO;
import gov.df.seape.sistema.visitas.dto.VisitanteResponseDTO;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.model.Visitante;
import gov.df.seape.sistema.visitas.repository.AgendamentoVisitaRepository;
import gov.df.seape.sistema.visitas.repository.PessoaRepository;
import gov.df.seape.sistema.visitas.repository.VisitanteRepository;
import gov.df.seape.sistema.visitas.service.VisitanteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Visitantes
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VisitanteServiceImpl implements VisitanteService {

    private final VisitanteRepository visitanteRepository;
    private final PessoaRepository pessoaRepository;
    private final AgendamentoVisitaRepository agendamentoVisitaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public VisitanteResponseDTO criarVisitante(VisitanteRequestDTO requestDTO) {
        log.info("Criando novo visitante com CPF: {}", requestDTO.getCpf());
        
        // Verificar se já existe pessoa com este CPF
        if (pessoaRepository.existsByCpf(requestDTO.getCpf())) {
            log.warn("Tentativa de criar visitante com CPF já cadastrado: {}", requestDTO.getCpf());
            throw new OperacaoInvalidaException("Já existe uma pessoa cadastrada com o CPF: " + requestDTO.getCpf());
        }
        
        // Criar pessoa
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(requestDTO.getNome());
        pessoa.setCpf(requestDTO.getCpf());
        pessoa.setDataNascimento(requestDTO.getDataNascimento());
        
        pessoa = pessoaRepository.save(pessoa);
        
        // Criar visitante
        Visitante visitante = new Visitante();
        visitante.setPessoa(pessoa);
        
        // Se senha online for fornecida, criptografá-la
        if (requestDTO.getSenhaOnline() != null && !requestDTO.getSenhaOnline().isEmpty()) {
            visitante.setSenhaOnline(passwordEncoder.encode(requestDTO.getSenhaOnline()));
        }
        
        visitante = visitanteRepository.save(visitante);
        log.info("Visitante criado com sucesso. ID: {}", visitante.getId());
        
        VisitanteResponseDTO responseDTO = new VisitanteResponseDTO(visitante);
        responseDTO.setGrauParentesco(requestDTO.getGrauParentesco());
        return responseDTO;
    }

    @Override
    @Transactional
    public VisitanteResponseDTO atualizarVisitante(Long id, VisitanteRequestDTO requestDTO) {
        log.info("Atualizando visitante com ID: {}", id);
        
        Visitante visitante = visitanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Visitante não encontrado com ID: " + id));
        
        // Verificar se já existe outra pessoa com este CPF
        pessoaRepository.findByCpf(requestDTO.getCpf())
                .ifPresent(p -> {
                    if (!p.getId().equals(visitante.getPessoa().getId())) {
                        log.warn("Tentativa de atualizar visitante com CPF já cadastrado: {}", requestDTO.getCpf());
                        throw new OperacaoInvalidaException("Já existe outra pessoa cadastrada com o CPF: " + requestDTO.getCpf());
                    }
                });
        
        // Atualizar pessoa
        Pessoa pessoa = visitante.getPessoa();
        pessoa.setNome(requestDTO.getNome());
        pessoa.setCpf(requestDTO.getCpf());
        pessoa.setDataNascimento(requestDTO.getDataNascimento());
        
        pessoaRepository.save(pessoa);
        
        // Atualizar visitante - senha online só é atualizada se fornecida
        if (requestDTO.getSenhaOnline() != null && !requestDTO.getSenhaOnline().isEmpty()) {
            visitante.setSenhaOnline(passwordEncoder.encode(requestDTO.getSenhaOnline()));
        }
        
        visitante = visitanteRepository.save(visitante);
        log.info("Visitante atualizado com sucesso. ID: {}", visitante.getId());
        
        VisitanteResponseDTO responseDTO = new VisitanteResponseDTO(visitante);
        responseDTO.setGrauParentesco(requestDTO.getGrauParentesco());
        return responseDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<VisitanteResponseDTO> listarVisitantesPaginados(Pageable pageable) {
        log.info("Listando visitantes com paginação");
        Page<Visitante> pagina = visitanteRepository.findAll(pageable);
        
        Page<VisitanteResponseDTO> paginaDTO = pagina.map(visitante -> {
            VisitanteResponseDTO dto = new VisitanteResponseDTO(visitante);
            return dto;
        });
        
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VisitanteResponseDTO> listarVisitantes() {
        log.info("Listando todos os visitantes");
        List<Visitante> visitantes = visitanteRepository.findAll();
        
        return visitantes.stream()
                .map(VisitanteResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public VisitanteResponseDTO buscarVisitantePorId(Long id) {
        log.info("Buscando visitante por ID: {}", id);
        Visitante visitante = visitanteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Visitante não encontrado com ID: " + id));
        
        return new VisitanteResponseDTO(visitante);
    }

    @Override
    @Transactional(readOnly = true)
    public VisitanteResponseDTO buscarPorCpf(String cpf) {
        log.info("Buscando visitante por CPF: {}", cpf);
        Visitante visitante = visitanteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Visitante não encontrado com CPF: " + cpf));
        
        return new VisitanteResponseDTO(visitante);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<VisitanteResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        log.info("Buscando visitantes por nome contendo: {}", nome);
        Page<Visitante> pagina = visitanteRepository.findByNomeContainingIgnoreCase(nome, pageable);
        
        Page<VisitanteResponseDTO> paginaDTO = pagina.map(VisitanteResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<VisitanteResponseDTO> buscarVisitantesPorCustodiado(Long custodiadoId, Pageable pageable) {
        log.info("Buscando visitantes por custodiado ID: {}", custodiadoId);
        
        // Buscar IDs de visitantes distintos que visitaram este custodiado
        List<Long> visitanteIds = agendamentoVisitaRepository.findVisitanteIdsByCustodiadoId(custodiadoId);
        
        if (visitanteIds.isEmpty()) {
            // Retornar página vazia se não houver visitantes
            return new PageResponseDTO<>(Page.empty(pageable));
        }
        
        // Buscar visitantes pelos IDs encontrados
        Page<Visitante> pagina = visitanteRepository.findByIdIn(visitanteIds, pageable);
        
        Page<VisitanteResponseDTO> paginaDTO = pagina.map(VisitanteResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }
}