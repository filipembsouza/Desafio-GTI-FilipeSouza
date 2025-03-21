package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.CustodiadoRequestDTO;
import gov.df.seape.sistema.visitas.dto.CustodiadoResponseDTO;
import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.service.CustodiadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operações relacionadas a Custodiados.
 */
@RestController
@RequestMapping("/api/custodiados")
@RequiredArgsConstructor
@Tag(name = "Custodiados", description = "Endpoints para gerenciamento de custodiados")
public class CustodiadoController {

    private final CustodiadoService custodiadoService;

    /**
     * Cria um novo custodiado.
     * 
     * @param requestDTO Dados do custodiado a ser criado
     * @return O custodiado criado com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar custodiado", description = "Cria um novo custodiado")
    public ResponseEntity<CustodiadoResponseDTO> criarCustodiado(@Valid @RequestBody CustodiadoRequestDTO requestDTO) {
        CustodiadoResponseDTO responseDTO = custodiadoService.criarCustodiado(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Atualiza um custodiado existente.
     * 
     * @param id ID do custodiado a ser atualizado
     * @param requestDTO Novos dados do custodiado
     * @return O custodiado atualizado
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar custodiado", description = "Atualiza um custodiado existente")
    public ResponseEntity<CustodiadoResponseDTO> atualizarCustodiado(
            @PathVariable Long id, 
            @Valid @RequestBody CustodiadoRequestDTO requestDTO) {
        CustodiadoResponseDTO responseDTO = custodiadoService.atualizarCustodiado(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os custodiados com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de custodiados
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar custodiados paginados", description = "Lista todos os custodiados com paginação")
    public ResponseEntity<PageResponseDTO<CustodiadoResponseDTO>> listarCustodiadosPaginados(Pageable pageable) {
        PageResponseDTO<CustodiadoResponseDTO> pageResponseDTO = custodiadoService.listarCustodiadosPaginados(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Lista todos os custodiados.
     * 
     * @return Lista de todos os custodiados
     */
    @GetMapping
    @Operation(summary = "Listar custodiados", description = "Lista todos os custodiados")
    public ResponseEntity<List<CustodiadoResponseDTO>> listarCustodiados() {
        List<CustodiadoResponseDTO> custodiados = custodiadoService.listarCustodiados();
        return ResponseEntity.ok(custodiados);
    }

    /**
     * Busca um custodiado pelo seu ID.
     * 
     * @param id ID do custodiado a ser buscado
     * @return O custodiado encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar custodiado por ID", description = "Retorna um custodiado específico pelo ID")
    public ResponseEntity<CustodiadoResponseDTO> buscarCustodiadoPorId(@PathVariable Long id) {
        CustodiadoResponseDTO responseDTO = custodiadoService.buscarCustodiadoPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca um custodiado pelo número de prontuário.
     * 
     * @param numeroProntuario Número do prontuário
     * @return O custodiado encontrado
     */
    @GetMapping("/prontuario/{numeroProntuario}")
    @Operation(summary = "Buscar por prontuário", description = "Busca um custodiado pelo número de prontuário")
    public ResponseEntity<CustodiadoResponseDTO> buscarPorNumeroProntuario(@PathVariable String numeroProntuario) {
        CustodiadoResponseDTO responseDTO = custodiadoService.buscarPorNumeroProntuario(numeroProntuario);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca custodiados pelo nome.
     * 
     * @param nome Nome ou parte do nome
     * @param pageable Configurações de paginação
     * @return Página de custodiados que contêm o nome especificado
     */
    @GetMapping("/nome")
    @Operation(summary = "Buscar por nome", description = "Busca custodiados pelo nome")
    public ResponseEntity<PageResponseDTO<CustodiadoResponseDTO>> buscarPorNome(
            @RequestParam String nome, 
            Pageable pageable) {
        PageResponseDTO<CustodiadoResponseDTO> pageResponseDTO = custodiadoService.buscarPorNome(nome, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca custodiados por unidade penal.
     * 
     * @param unidadePenalId ID da unidade penal
     * @param pageable Configurações de paginação
     * @return Página de custodiados da unidade penal
     */
    @GetMapping("/unidade/{unidadePenalId}")
    @Operation(summary = "Buscar por unidade penal", description = "Busca custodiados por unidade penal")
    public ResponseEntity<PageResponseDTO<CustodiadoResponseDTO>> buscarPorUnidadePenal(
            @PathVariable Long unidadePenalId, 
            Pageable pageable) {
        PageResponseDTO<CustodiadoResponseDTO> pageResponseDTO = custodiadoService.buscarPorUnidadePenal(unidadePenalId, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Busca custodiados pelo vulgo (apelido).
     * 
     * @param vulgo Vulgo ou parte do vulgo
     * @param pageable Configurações de paginação
     * @return Página de custodiados que contêm o vulgo especificado
     */
    @GetMapping("/vulgo")
    @Operation(summary = "Buscar por vulgo", description = "Busca custodiados pelo vulgo (apelido)")
    public ResponseEntity<PageResponseDTO<CustodiadoResponseDTO>> buscarPorVulgo(
            @RequestParam String vulgo, 
            Pageable pageable) {
        PageResponseDTO<CustodiadoResponseDTO> pageResponseDTO = custodiadoService.buscarPorVulgo(vulgo, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }
}