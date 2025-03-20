package gov.df.seape.sistema.visitas.service;

import gov.df.seape.sistema.visitas.model.Custodiado;

import java.util.List;

/**
 * Interface de serviço para operações relacionadas a Custodiados.
 * Define o contrato de operações disponíveis para manipulação de custodiados.
 */
public interface CustodiadoService {
    
    /**
     * Busca todos os custodiados cadastrados.
     * 
     * @return Lista de todos os custodiados
     */
    List<Custodiado> listarTodos();
    
    /**
     * Busca um custodiado pelo seu ID.
     * 
     * @param id ID do custodiado
     * @return O custodiado encontrado
     */
    Custodiado buscarPorId(Long id);
    
    /**
     * Busca um custodiado pelo número de prontuário.
     * 
     * @param numeroProntuario Número do prontuário
     * @return O custodiado encontrado
     */
    Custodiado buscarPorNumeroProntuario(String numeroProntuario);
    
    /**
     * Busca custodiados por parte do nome.
     * 
     * @param nome Parte do nome para busca
     * @return Lista de custodiados que atendem ao critério
     */
    List<Custodiado> buscarPorNome(String nome);
    
    /**
     * Busca custodiados por unidade penal.
     * 
     * @param unidadePenalId ID da unidade penal
     * @return Lista de custodiados da unidade penal
     */
    List<Custodiado> buscarPorUnidadePenal(Long unidadePenalId);
}