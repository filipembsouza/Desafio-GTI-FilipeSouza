package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.PessoaRequestDTO;
import gov.df.seape.sistema.visitas.dto.PessoaResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para gerenciamento de Pessoas.
 * Define operações para manipulação de dados pessoais no sistema.
 */
public interface PessoaService {
    
    /**
     * Cria uma nova pessoa.
     * 
     * @param pessoaRequestDTO Dados para criação da pessoa
     * @return Pessoa criada
     */
    PessoaResponseDTO criarPessoa(PessoaRequestDTO pessoaRequestDTO);
    
    /**
     * Atualiza uma pessoa existente.
     * 
     * @param id Identificador da pessoa
     * @param pessoaRequestDTO Novos dados da pessoa
     * @return Pessoa atualizada
     */
    PessoaResponseDTO atualizarPessoa(Long id, PessoaRequestDTO pessoaRequestDTO);
    
    /**
     * Busca uma pessoa pelo seu identificador.
     * 
     * @param id Identificador da pessoa
     * @return Optional com a pessoa encontrada
     */
    Optional<PessoaResponseDTO> buscarPessoaPorId(Long id);
    
    /**
     * Busca uma pessoa pelo CPF.
     * 
     * @param cpf CPF da pessoa
     * @return Optional com a pessoa encontrada
     */
    Optional<PessoaResponseDTO> buscarPessoaPorCpf(String cpf);
    
    /**
     * Lista todas as pessoas cadastradas.
     * 
     * @return Lista de todas as pessoas
     */
    List<PessoaResponseDTO> listarTodasPessoas();
    
    /**
     * Lista pessoas com suporte a paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de pessoas
     */
    Page<PessoaResponseDTO> listarPessoasPaginado(Pageable pageable);
    
    /**
     * Remove uma pessoa pelo seu identificador.
     * 
     * @param id Identificador da pessoa a ser removida
     */
    void excluirPessoa(Long id);
    
    /**
     * Busca pessoas por nome.
     * 
     * @param nome Termo de busca no nome
     * @return Lista de pessoas correspondentes
     */
    List<PessoaResponseDTO> buscarPessoaPorNome(String nome);
    
    /**
     * Busca pessoas por intervalo de data de nascimento.
     * 
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de pessoas nascidas no intervalo
     */
    List<PessoaResponseDTO> buscarPessoaPorIntervaloNascimento(LocalDate dataInicio, LocalDate dataFim);
}