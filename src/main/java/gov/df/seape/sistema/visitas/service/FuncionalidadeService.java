package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.dto.FuncionalidadeRequestDTO;
import gov.df.seape.sistema.visitas.dto.FuncionalidadeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interface de serviço para gerenciamento de Funcionalidades.
 * Define operações para manipulação de funcionalidades no sistema.
 */
public interface FuncionalidadeService {
    
    /**
     * Cria uma nova funcionalidade.
     * 
     * @param funcionalidadeRequestDTO Dados para criação da funcionalidade
     * @return Funcionalidade criada
     */
    FuncionalidadeResponseDTO criarFuncionalidade(FuncionalidadeRequestDTO funcionalidadeRequestDTO);
    
    /**
     * Atualiza uma funcionalidade existente.
     * 
     * @param id Identificador da funcionalidade
     * @param funcionalidadeRequestDTO Novos dados da funcionalidade
     * @return Funcionalidade atualizada
     */
    FuncionalidadeResponseDTO atualizarFuncionalidade(Long id, FuncionalidadeRequestDTO funcionalidadeRequestDTO);
    
    /**
     * Busca uma funcionalidade pelo seu identificador.
     * 
     * @param id Identificador da funcionalidade
     * @return Optional com a funcionalidade encontrada
     */
    Optional<FuncionalidadeResponseDTO> buscarFuncionalidadePorId(Long id);
    
    /**
     * Busca uma funcionalidade pela sua descrição.
     * 
     * @param descricao Descrição da funcionalidade
     * @return Optional com a funcionalidade encontrada
     */
    Optional<FuncionalidadeResponseDTO> buscarFuncionalidadePorDescricao(String descricao);
    
    /**
     * Busca uma funcionalidade por sua authority.
     * 
     * @param authority Identificador técnico da funcionalidade
     * @return Optional com a funcionalidade encontrada
     */
    Optional<FuncionalidadeResponseDTO> buscarFuncionalidadePorAuthority(String authority);
    
    /**
     * Lista todas as funcionalidades cadastradas.
     * 
     * @return Lista de todas as funcionalidades
     */
    List<FuncionalidadeResponseDTO> listarTodasFuncionalidades();
    
    /**
     * Lista funcionalidades com suporte a paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de funcionalidades
     */
    Page<FuncionalidadeResponseDTO> listarFuncionalidadesPaginado(Pageable pageable);
    
    /**
     * Remove uma funcionalidade pelo seu identificador.
     * 
     * @param id Identificador da funcionalidade a ser removida
     */
    void excluirFuncionalidade(Long id);
    
    /**
     * Busca funcionalidades por descrição.
     * 
     * @param descricao Termo de busca na descrição
     * @return Lista de funcionalidades correspondentes
     */
    List<FuncionalidadeResponseDTO> buscarFuncionalidadePorDescricaoContendo(String descricao);
}