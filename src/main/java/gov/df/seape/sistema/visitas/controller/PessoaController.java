package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.dto.PageResponseDTO;
import gov.df.seape.sistema.visitas.dto.PessoaRequestDTO;
import gov.df.seape.sistema.visitas.dto.PessoaResponseDTO;
import gov.df.seape.sistema.visitas.service.PessoaService;
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
 * Controlador REST para operações relacionadas a Pessoas.
 */
@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
@Tag(name = "Pessoas", description = "Endpoints para gerenciamento de pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    /**
     * Cria uma nova pessoa.
     * 
     * @param requestDTO Dados da pessoa a ser criada
     * @return A pessoa criada com status 201 (Created)
     */
    @PostMapping
    @Operation(summary = "Criar pessoa", description = "Cria uma nova pessoa")
    public ResponseEntity<PessoaResponseDTO> criarPessoa(@Valid @RequestBody PessoaRequestDTO requestDTO) {
        PessoaResponseDTO responseDTO = pessoaService.criarPessoa(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Atualiza uma pessoa existente.
     * 
     * @param id ID da pessoa a ser atualizada
     * @param requestDTO Novos dados da pessoa
     * @return A pessoa atualizada
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pessoa", description = "Atualiza uma pessoa existente")
    public ResponseEntity<PessoaResponseDTO> atualizarPessoa(
            @PathVariable Long id, 
            @Valid @RequestBody PessoaRequestDTO requestDTO) {
        PessoaResponseDTO responseDTO = pessoaService.atualizarPessoa(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todas as pessoas com paginação.
     * 
     * @param pageable Configurações de paginação
     * @return Página de pessoas
     */
    @GetMapping("/paginado")
    @Operation(summary = "Listar pessoas paginadas", description = "Lista todas as pessoas com paginação")
    public ResponseEntity<PageResponseDTO<PessoaResponseDTO>> listarPessoasPaginadas(Pageable pageable) {
        PageResponseDTO<PessoaResponseDTO> pageResponseDTO = pessoaService.listarPessoasPaginadas(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Lista todas as pessoas.
     * 
     * @return Lista de todas as pessoas
     */
    @GetMapping
    @Operation(summary = "Listar pessoas", description = "Lista todas as pessoas")
    public ResponseEntity<List<PessoaResponseDTO>> listarPessoas() {
        List<PessoaResponseDTO> pessoas = pessoaService.listarPessoas();
        return ResponseEntity.ok(pessoas);
    }

    /**
     * Busca uma pessoa pelo seu ID.
     * 
     * @param id ID da pessoa a ser buscada
     * @return A pessoa encontrada
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID", description = "Retorna uma pessoa específica pelo ID")
    public ResponseEntity<PessoaResponseDTO> buscarPessoaPorId(@PathVariable Long id) {
        PessoaResponseDTO responseDTO = pessoaService.buscarPessoaPorId(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca uma pessoa pelo CPF.
     * 
     * @param cpf CPF da pessoa
     * @return A pessoa encontrada
     */
    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar por CPF", description = "Busca uma pessoa pelo CPF")
    public ResponseEntity<PessoaResponseDTO> buscarPessoaPorCpf(@PathVariable String cpf) {
        PessoaResponseDTO responseDTO = pessoaService.buscarPessoaPorCpf(cpf);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Busca pessoas pelo nome.
     * 
     * @param nome Nome ou parte do nome
     * @param pageable Configurações de paginação
     * @return Página de pessoas que contêm o nome especificado
     */
    @GetMapping("/nome")
    @Operation(summary = "Buscar por nome", description = "Busca pessoas pelo nome")
    public ResponseEntity<PageResponseDTO<PessoaResponseDTO>> buscarPessoasPorNome(
            @RequestParam String nome, 
            Pageable pageable) {
        PageResponseDTO<PessoaResponseDTO> pageResponseDTO = pessoaService.buscarPessoasPorNome(nome, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    /**
     * Exclui uma pessoa pelo seu ID.
     * 
     * @param id ID da pessoa a ser excluída
     * @return Status 204 (No Content)
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pessoa", description = "Exclui uma pessoa existente")
    public ResponseEntity<Void> excluirPessoa(@PathVariable Long id) {
        pessoaService.excluirPessoa(id);
        return ResponseEntity.noContent().build();
    }
}