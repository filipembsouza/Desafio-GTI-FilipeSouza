package gov.df.seape.sistema.visitas.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração centralizada de logging para o Sistema de Visitas Prisionais.
 * Responsável por gerenciar e padronizar o registro de eventos e logs da aplicação.
 */
@Configuration
public class LoggingConfig {
    // Logger estático para registrar eventos relacionados à configuração
    private static final Logger logger = LoggerFactory.getLogger(LoggingConfig.class);

    /**
     * Método de inicialização para configurar e registrar o início do sistema.
     * Registra uma mensagem informativa indicando que o logging foi inicializado.
     */
    public void init() {
        logger.info("Sistema de Visitas Prisionais - Logging inicializado com sucesso");
        logger.info("Configurações de log carregadas para monitoramento do sistema");
    }
}