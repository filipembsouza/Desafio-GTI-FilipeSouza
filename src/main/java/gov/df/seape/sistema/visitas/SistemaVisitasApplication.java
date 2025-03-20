package gov.df.seape.sistema.visitas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "gov.df.seape.sistema.visitas.repository")
@EnableTransactionManagement
@Slf4j  // Anotação do Lombok para geração de logger
public class SistemaVisitasApplication {

    public static void main(String[] args) {
        log.info("Iniciando Sistema de Visitas Prisionais");
        SpringApplication.run(SistemaVisitasApplication.class, args);
        log.info("Sistema de Visitas inicializado com sucesso!");
    }
}