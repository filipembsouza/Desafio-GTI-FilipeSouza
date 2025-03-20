package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.CustodiadoRequestDTO;
import gov.df.seape.sistema.visitas.dto.CustodiadoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para gerenciamento de Custodiados.
 * Define operações para manipulação de detentos no sistema prisional.
 */
public interface CustodiadoService {
    
    /**
     * Cria um novo custodiado.
     * 
     * @param custodiadoRequestDTO Dados para criação do custodiado
     * @return Custodiado criado
     */
    CustodiadoResponseDTO criarCustodiado(CustodiadoRequestDTO custodiadoRequestDTO);
    
    /**
     * Atualiza um custodiado existente.
     * 
     * @param id Identificador do custodiado
     * @param custodiadoRequestDTO Novos dados do custodiado
     * @return Custodiado atualizado
     */
    CustodiadoResponseDTO atualizarCustodiado(Long id, CustodiadoRequestDTO custodiadoRequestDTO);
    
    /**
     * Busca um custodiado pelo seu identificador.
     * 
     * @param id Identificador do custodiado
     * @return Optional com o custodiado encontrado
     */
    Optional<CustodiadoResponseDTO> buscarCustodiadoPorId(Long id);
    
    /**
     * Busca um custodiado pelo número do prontuário.
     * 
     * @param numeroProntuario Número do prontuário
     * @return Optional com o custodiado encontrado
     */
    Optional<CustodiadoResponseDTO> buscarCustodiadoPorNumeroProntuario(String numeroProntuario);
    
    /**
     * Lista todos os custodiados cadastrados.
     * 
     * @return Lista de todos os custodiados
     */
    List<CustodiadoResponseDTO> listarTodosCustodiados();
    
    /**
     * Lista custodiados com suporte a paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de custodiados
     */
    Page<CustodiadoResponseDTO> listarCustodiadosPaginado(Pageable pageable);
    
    /**
     * Remove um custodiado pelo seu identificador.
     * 
     * @param id Identificador do custodiado a ser removido
     */
    void excluirCustodiado(Long id);
    
    /**
     * Busca custodiados por nome.
     * 
     * @param nome Termo de busca no nome
     * @return Lista de custodiados correspondentes
     */
    List<CustodiadoResponseDTO> buscarCustodiadoPorNome(String nome);
    
    /**
     * Busca custodiados por vulgo.
     * 
     * @param vulgo Termo de busca no vulgo
     * @return Lista de custodiados correspondentes
     */
    List<CustodiadoResponseDTO> buscarCustodiadoPorVulgo(String vulgo);
    
    /**
     * Busca custodiados por unidade penal.
     * 
     * @param unidadePenalId ID da unidade penal
     * @return Lista de custodiados da unidade penal
     */
    List<CustodiadoResponseDTO> buscarCustodiadoPorUnidadePenal(Long unidadePenalId);
}