package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.Custodiado;
import gov.df.seape.sistema.visitas.model.Pessoa;
import gov.df.seape.sistema.visitas.model.UnidadePenal;
import gov.df.seape.sistema.visitas.repository.CustodiadoRepository;
import gov.df.seape.sistema.visitas.repository.PessoaRepository;
import gov.df.seape.sistema.visitas.repository.UnidadePenalRepository;
import gov.df.seape.sistema.visitas.service.CustodiadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação da interface CustodiadoService.
 * Contém a lógica de negócio para gerenciamento de custodiados.
 */
@Service
@RequiredArgsConstructor

public class CustodiadoServiceImpl implements CustodiadoService {

    private final CustodiadoRepository custodiadoRepository;
    private final PessoaRepository pessoaRepository;
    private final UnidadePenalRepository unidadePenalRepository;

    @Override
    public List<Custodiado> listarTodos() {
        return custodiadoRepository.findAll();
    }

    @Override
    public Custodiado buscarPorId(Long id) {
        return custodiadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Custodiado não encontrado com ID: " + id));
    }

    @Override
    public Custodiado buscarPorNumeroProntuario(String numeroProntuario) {
        return custodiadoRepository.findByNumeroProntuario(numeroProntuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Custodiado não encontrado com número de prontuário: " + numeroProntuario));
    }

    @Override
    public List<Custodiado> buscarPorNome(String nome) {
        // Como o nome está na entidade Pessoa, precisamos fazer uma busca indireta
        List<Custodiado> todos = custodiadoRepository.findAll();
        
        return todos.stream()
                .filter(c -> c.getPessoa().getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Custodiado> buscarPorUnidadePenal(Long unidadePenalId) {
        UnidadePenal unidadePenal = unidadePenalRepository.findById(unidadePenalId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade Penal não encontrada com ID: " + unidadePenalId));
        
        return custodiadoRepository.findByUnidadePenal(unidadePenal);
    }
}