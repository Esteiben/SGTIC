package com.uteq.sgtic.config.db;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
@Slf4j
public class DataSourceConfig {

    // 1. Configuramos la base de datos Principal
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    // 2. Configuramos la base de datos Secundaria (Respaldo)
    @Bean(name = "secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    // 3. Creamos el Enrutador y lo marcamos como el DataSource oficial de Spring
    @Bean
    @Primary
    public DataSource dataSource(
            @Qualifier("primaryDataSource") DataSource primary,
            @Qualifier("secondaryDataSource") DataSource secondary) {
            
        DataSourceRouter router = new DataSourceRouter();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("PRIMARY", primary);
        targetDataSources.put("SECONDARY", secondary);
        
        router.setTargetDataSources(targetDataSources);
        router.setDefaultTargetDataSource(primary); // Arranca siempre apuntando a la principal
        return router;
    }

    // 4. EL VIGILANTE: Revisa la salud de la principal cada 10 segundos
    @Scheduled(fixedDelay = 10000)
    public void checkPrimaryDatabaseHealth() {
        try (Connection conn = primaryDataSource().getConnection()) {
            if (conn.isValid(2)) {
                if ("SECONDARY".equals(DatabaseContextHolder.get())) {
                    log.info("La base de datos PRINCIPAL se ha recuperado. Restaurando tráfico a SGTIC...");
                    DatabaseContextHolder.set("PRIMARY");
                }
            }
        } catch (Exception e) {
            if ("PRIMARY".equals(DatabaseContextHolder.get())) {
                log.error("¡ALERTA! La base principal falló. Cambiando tráfico a SGTIC_SECUNDARIA...");
                DatabaseContextHolder.set("SECONDARY");
            }
        }
    }
}