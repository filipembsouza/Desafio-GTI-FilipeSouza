package gov.df.seape.sistema.visitas.service.impl;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.UnidadePenalRequestDTO;
import gov.df.seape.sistema.visitas.dto.UnidadePenalResponseDTO;
import gov.df.seape.sistema.visitas.exception.OperacaoInvalidaException;
import gov.df.seape.sistema.visitas.exception.RecursoNaoEncontradoException;
import gov.df.seape.sistema.visitas.model.UnidadePenal;
import gov.df.seape.sistema.visitas.repository.CustodiadoRepository;
import gov.df.seape.sistema.visitas.repository.UnidadePenalRepository;
import gov.df.seape.sistema.visitas.service.UnidadePenalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de Unidades Penais
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UnidadePenalServiceImpl implements UnidadePenalService {

    private final UnidadePenalRepository unidadePenalRepository;
    private final CustodiadoRepository custodiadoRepository;

    @Override
    @Transactional
    public UnidadePenalResponseDTO criarUnidadePenal(UnidadePenalRequestDTO requestDTO) {
        log.info("Criando nova unidade penal: {}", requestDTO.getNome());
        
        // Verificar se já existe unidade penal com este nome
        if (unidadePenalRepository.findByNome(requestDTO.getNome()).isPresent()) {
            log.warn("Tentativa de criar unidade penal com nome duplicado: {}", requestDTO.getNome());
            throw new OperacaoInvalidaException("Já existe uma unidade penal com o nome: " + requestDTO.getNome());
        }
        
        UnidadePenal unidadePenal = new UnidadePenal();
        unidadePenal.setNome(requestDTO.getNome());
        unidadePenal.setDescricao(requestDTO.getDescricao());
        
        unidadePenal = unidadePenalRepository.save(unidadePenal);
        log.info("Unidade penal criada com sucesso. ID: {}", unidadePenal.getId());
        
        return new UnidadePenalResponseDTO(unidadePenal);
    }

    @Override
    @Transactional
    public UnidadePenalResponseDTO atualizarUnidadePenal(Long id, UnidadePenalRequestDTO requestDTO) {
        log.info("Atualizando unidade penal com ID: {}", id);
        
        UnidadePenal unidadePenal = unidadePenalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade penal não encontrada com ID: " + id));
        
        // Verificar se já existe outra unidade penal com este nome
        unidadePenalRepository.findByNome(requestDTO.getNome())
                .ifPresent(u -> {
                    if (!u.getId().equals(id)) {
                        log.warn("Tentativa de atualizar unidade penal com nome duplicado: {}", requestDTO.getNome());
                        throw new OperacaoInvalidaException("Já existe outra unidade penal com o nome: " + requestDTO.getNome());
                    }
                });
        
        unidadePenal.setNome(requestDTO.getNome());
        unidadePenal.setDescricao(requestDTO.getDescricao());
        
        unidadePenal = unidadePenalRepository.save(unidadePenal);
        log.info("Unidade penal atualizada com sucesso. ID: {}", unidadePenal.getId());
        
        return new UnidadePenalResponseDTO(unidadePenal);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<UnidadePenalResponseDTO> listarUnidadesPenaisPaginadas(Pageable pageable) {
        log.info("Listando unidades penais com paginação");
        Page<UnidadePenal> pagina = unidadePenalRepository.findAll(pageable);
        
        Page<UnidadePenalResponseDTO> paginaDTO = pagina.map(UnidadePenalResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnidadePenalResponseDTO> listarUnidadesPenais() {
        log.info("Listando todas as unidades penais");
        List<UnidadePenal> unidadesPenais = unidadePenalRepository.findAllOrderByNome();
        
        return unidadesPenais.stream()
                .map(UnidadePenalResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UnidadePenalResponseDTO buscarUnidadePenalPorId(Long id) {
        log.info("Buscando unidade penal por ID: {}", id);
        UnidadePenal unidadePenal = unidadePenalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade penal não encontrada com ID: " + id));
        
        return new UnidadePenalResponseDTO(unidadePenal);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<UnidadePenalResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        log.info("Buscando unidades penais por nome contendo: {}", nome);
        Page<UnidadePenal> pagina = unidadePenalRepository.findByNomeContainingIgnoreCase(nome, pageable);
        
        Page<UnidadePenalResponseDTO> paginaDTO = pagina.map(UnidadePenalResponseDTO::new);
        return new PageResponseDTO<>(paginaDTO);
    }
    
    /**
     * Exclui uma unidade penal.
     * Não incluído na interface original, mas adicionado para completude CRUD.
     * 
     * @param id ID da unidade penal a ser excluída
     */
    @Transactional
    public void excluirUnidadePenal(Long id) {
        log.info("Excluindo unidade penal com ID: {}", id);
        
        UnidadePenal unidadePenal = unidadePenalRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade penal não encontrada com ID: " + id));
        
        // Verificar se existem custodiados associados a esta unidade penal
        long custodiadoCount = custodiadoRepository.countByUnidadePenalId(id);
        if (custodiadoCount > 0) {
            log.warn("Tentativa de excluir unidade penal com custodiados associados. ID: {}, Custodiados: {}", 
                    id, custodiadoCount);
            throw new OperacaoInvalidaException(
                    "Não é possível excluir esta unidade penal pois existem " + custodiadoCount + 
                    " custodiados associados a ela. Transfira os custodiados para outra unidade antes de excluí-la.");
        }
        
        unidadePenalRepository.delete(unidadePenal);
        log.info("Unidade penal excluída com sucesso. ID: {}", id);
    }
    
    /**
     * Conta o número de custodiados por unidade penal.
     * Método adicional útil para dashboard ou relatórios.
     * 
     * @return Lista de contagens de custodiados por unidade penal
     */
    @Transactional(readOnly = true)
    public List<Object[]> contarCustodiadosPorUnidadePenal() {
        log.info("Contando custodiados por unidade penal");
        
        // Esta consulta agregaria os dados. Na implementação real, teria que ser definida no repositório
        // Aqui é apenas um exemplo conceitual do que o método faria
        List<Object[]> contagens = custodiadoRepository.countByUnidadePenal();
        
        return contagens;
    }
}