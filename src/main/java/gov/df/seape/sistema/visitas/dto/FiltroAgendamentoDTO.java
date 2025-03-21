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
    
    // Validação básica do filtro
    public boolean isValid() {
        // Se dataFim for informada, dataInicio também deve ser
        if (dataFim != null && dataInicio == null) {
            return false;
        }
        
        // Se ambas as datas forem informadas, dataFim deve ser >= dataInicio
        if (dataInicio != null && dataFim != null && dataFim.isBefore(dataInicio)) {
            return false;
        }
        
        return true;
    }
}