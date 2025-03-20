package gov.df.seape.sistema.visitas.controller;

import gov.df.seape.sistema.visitas.model.Custodiado;
import gov.df.seape.sistema.visitas.service.CustodiadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para operações relacionadas a Custodiados.
 * Expõe endpoints para consulta de custodiados.
 */
@RestController
@RequestMapping("/api/custodiados")
@RequiredArgsConstructor

public class CustodiadoController {

    private final CustodiadoService custodiadoService;

    /**
     * Lista todos os custodiados.
     * 
     * @return Lista de custodiados
     */
    @GetMapping
    public ResponseEntity<List<Custodiado>> listarTodos() {
        return ResponseEntity.ok(custodiadoService.listarTodos());
    }

    /**
     * Busca um custodiado pelo seu ID.
     * 
     * @param id ID do custodiado
     * @return O custodiado encontrado
     */
    @GetMapping("/{id}")
    public ResponseEntity<Custodiado> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(custodiadoService.buscarPorId(id));
    }

    /**
     * Busca um custodiado pelo número de prontuário.
     * 
     * @param numeroProntuario Número do prontuário
     * @return O custodiado encontrado
     */
    @GetMapping("/prontuario/{numeroProntuario}")
    public ResponseEntity<Custodiado> buscarPorNumeroProntuario(@PathVariable String numeroProntuario) {
        return ResponseEntity.ok(custodiadoService.buscarPorNumeroProntuario(numeroProntuario));
    }

    /**
     * Busca custodiados por parte do nome.
     * 
     * @param nome Parte do nome para busca
     * @return Lista de custodiados que atendem ao critério
     */
    @GetMapping("/nome")
    public ResponseEntity<List<Custodiado>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(custodiadoService.buscarPorNome(nome));
    }

    /**
     * Busca custodiados por unidade penal.
     * 
     * @param unidadePenalId ID da unidade penal
     * @return Lista de custodiados da unidade penal
     */
    @GetMapping("/unidade/{unidadePenalId}")
    public ResponseEntity<List<Custodiado>> buscarPorUnidadePenal(@PathVariable Long unidadePenalId) {
        return ResponseEntity.ok(custodiadoService.buscarPorUnidadePenal(unidadePenalId));
    }
}