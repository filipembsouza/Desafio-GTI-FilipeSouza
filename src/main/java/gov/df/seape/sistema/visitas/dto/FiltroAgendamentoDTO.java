package gov.df.seape.sistema.visitas.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltroAgendamentoDTO {

    private Long custodiadoId;
    private Long visitanteId;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Long statusId;
    private Long unidadePenalId;

    /**
     * Validação básica do filtro:
     * - Se dataFim for informada, dataInicio também deve ser informada.
     * - Se ambas as datas forem informadas, dataFim deve ser >= dataInicio.
     */
    public boolean isValid() {
        return !(
            // dataFim informada sem dataInicio
            (dataFim != null && dataInicio == null)
            ||
            // dataFim antes de dataInicio
            (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio))
        );
    }
}
