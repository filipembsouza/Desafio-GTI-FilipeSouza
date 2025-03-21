package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.CustodiadoRequestDTO;
import gov.df.seape.sistema.visitas.dto.CustodiadoResponseDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Custodiados.
 */
public interface CustodiadoService {
    
    /**
     * Criar um novo custodiado.
     * 
     * @param requestDTO Dados do custodiado a ser criado
     * @return O custodiado criado, com ID gerado
     */
    CustodiadoResponseDTO criarCustodiado(CustodiadoRequestDTO requestDTO);
    
    /**
     * Atualizar um custodiado existente.
     * 
     * @param id ID do custodiado a ser atualizado
     * @param requestDTO Novos dados do custodiado
     * @return O custodiado atualizado
     */
    CustodiadoResponseDTO atualizarCustodiado(Long id, CustodiadoRequestDTO requestDTO);
    
    /**
     * Buscar todos os custodiados com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de custodiados
     */
    PageResponseDTO<CustodiadoResponseDTO> listarCustodiadosPaginados(Pageable pageable);
    
    /**
     * Buscar todos os custodiados.
     * 
     * @return Lista de todos os custodiados
     */
    List<CustodiadoResponseDTO> listarCustodiados();
    
    /**
     * Buscar um custodiado específico pelo ID.
     * 
     * @param id ID do custodiado a ser buscado
     * @return O custodiado encontrado
     */
    CustodiadoResponseDTO buscarCustodiadoPorId(Long id);
    
    /**
     * Buscar um custodiado pelo número de prontuário.
     * 
     * @param numeroProntuario Número do prontuário
     * @return O custodiado encontrado
     */
    CustodiadoResponseDTO buscarPorNumeroProntuario(String numeroProntuario);
    
    /**
     * Buscar custodiados por nome.
     * 
     * @param nome Nome ou parte do nome
     * @param pageable Configurações de paginação
     * @return Página de custodiados que contêm o nome especificado
     */
    PageResponseDTO<CustodiadoResponseDTO> buscarPorNome(String nome, Pageable pageable);
    
    /**
     * Buscar custodiados por unidade penal.
     * 
     * @param unidadePenalId ID da unidade penal
     * @param pageable Configurações de paginação
     * @return Página de custodiados da unidade penal
     */
    PageResponseDTO<CustodiadoResponseDTO> buscarPorUnidadePenal(Long unidadePenalId, Pageable pageable);
    
    /**
     * Buscar custodiados pelo vulgo (apelido).
     * 
     * @param vulgo Vulgo ou parte do vulgo
     * @param pageable Configurações de paginação
     * @return Página de custodiados que contêm o vulgo especificado
     */
    PageResponseDTO<CustodiadoResponseDTO> buscarPorVulgo(String vulgo, Pageable pageable);
}