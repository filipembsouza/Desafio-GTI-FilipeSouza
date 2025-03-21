package gov.df.seape.sistema.visitas.util;

import org.springframework.stereotype.Component;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Classe utilitária para validação de horários de visita.
 * Agora apenas quartas e quintas, das 09h às 15h.
 */
@Component  // permite que seja injetada em outros lugares do Spring
public class HorarioVisitaUtil {

    private static final LocalTime HORARIO_INICIO = LocalTime.of(9, 0);  // 09:00
    private static final LocalTime HORARIO_FIM    = LocalTime.of(15, 0); // 15:00

    /**
     * Remove o 'static' para poder usar como bean injetado.
     *
     * Verifica se o horário de visita está dentro dos períodos permitidos:
     * - Apenas quartas e quintas-feiras
     * - Das 9h às 15h
     */
    public boolean isHorarioPermitido(LocalDateTime dataHora) {
        DayOfWeek diaSemana = dataHora.getDayOfWeek();

        // Só permite quarta ou quinta
        if (diaSemana != DayOfWeek.WEDNESDAY && diaSemana != DayOfWeek.THURSDAY) {
            return false;
        }

        // Verifica se o horário está entre 09:00 e 15:00
        LocalTime horario = dataHora.toLocalTime();
        return !horario.isBefore(HORARIO_INICIO) && !horario.isAfter(HORARIO_FIM);
    }
}
