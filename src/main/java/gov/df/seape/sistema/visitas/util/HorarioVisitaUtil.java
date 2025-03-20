package gov.df.seape.sistema.visitas.util;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Classe utilitária para validação de horários de visita.
 * Implementa a regra opcional de validação de horários permitidos.
 */
public class HorarioVisitaUtil {
    
    // Horário de início e fim das visitas
    private static final LocalTime HORARIO_INICIO = LocalTime.of(9, 0); // 9:00
    private static final LocalTime HORARIO_FIM = LocalTime.of(17, 0);  // 17:00
    
    /**
     * Verifica se o horário de visita está dentro dos períodos permitidos.
     * As visitas são permitidas apenas de terça a domingo, das 9h às 17h.
     * 
     * @param dataHora Data e hora da visita
     * @return true se o horário for permitido, false caso contrário
     */
    public static boolean isHorarioPermitido(LocalDateTime dataHora) {
        // Verificar o dia da semana (não permite visitas às segundas-feiras)
        DayOfWeek diaSemana = dataHora.getDayOfWeek();
        if (diaSemana == DayOfWeek.MONDAY) {
            return false;
        }
        
        // Verificar horário (permitido entre 9h e 17h)
        LocalTime horario = dataHora.toLocalTime();
        return !horario.isBefore(HORARIO_INICIO) && !horario.isAfter(HORARIO_FIM);
    }
}