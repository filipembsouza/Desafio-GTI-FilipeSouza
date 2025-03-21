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

    @PostMapping
    @Operation(summary = "Criar pessoa", description = "Cria uma nova pessoa")
    public ResponseEntity<PessoaResponseDTO> criarPessoa(@Valid @RequestBody PessoaRequestDTO requestDTO) {
        PessoaResponseDTO responseDTO = pessoaService.criarPessoa(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pessoa", description = "Atualiza uma pessoa existente")
    public ResponseEntity<PessoaResponseDTO> atualizarPessoa(
            @PathVariable Long id, 
            @Valid @RequestBody PessoaRequestDTO requestDTO) {
        PessoaResponseDTO responseDTO = pessoaService.atualizarPessoa(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/paginado")
    @Operation(summary = "Listar pessoas paginadas", description = "Lista todas as pessoas com paginação")
    public ResponseEntity<PageResponseDTO<PessoaResponseDTO>> listarPessoasPaginadas(Pageable pageable) {
        PageResponseDTO<PessoaResponseDTO> pageResponseDTO = (PageResponseDTO<PessoaResponseDTO>) pessoaService.listarPessoasPaginado(pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    @GetMapping
    @Operation(summary = "Listar pessoas", description = "Lista todas as pessoas")
    public ResponseEntity<List<PessoaResponseDTO>> listarPessoas() {
        List<PessoaResponseDTO> pessoas = pessoaService.listarTodasPessoas();
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pessoa por ID", description = "Retorna uma pessoa específica pelo ID")
    public ResponseEntity<PessoaResponseDTO> buscarPessoaPorId(@PathVariable Long id) {
        PessoaResponseDTO responseDTO = pessoaService.buscarPessoaPorId(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada")); // Recomenda-se utilizar uma exceção customizada
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar por CPF", description = "Busca uma pessoa pelo CPF")
    public ResponseEntity<PessoaResponseDTO> buscarPessoaPorCpf(@PathVariable String cpf) {
        PessoaResponseDTO responseDTO = pessoaService.buscarPessoaPorCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada")); // Recomenda-se utilizar uma exceção customizada
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/nome")
    @Operation(summary = "Buscar por nome", description = "Busca pessoas pelo nome")
    public ResponseEntity<PageResponseDTO<PessoaResponseDTO>> buscarPessoasPorNome(
            @RequestParam String nome, 
            Pageable pageable) {
        PageResponseDTO<PessoaResponseDTO> pageResponseDTO = pessoaService.buscarPessoasPorNome(nome, pageable);
        return ResponseEntity.ok(pageResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pessoa", description = "Exclui uma pessoa existente")
    public ResponseEntity<Void> excluirPessoa(@PathVariable Long id) {
        pessoaService.excluirPessoa(id);
        return ResponseEntity.noContent().build();
    }
}
