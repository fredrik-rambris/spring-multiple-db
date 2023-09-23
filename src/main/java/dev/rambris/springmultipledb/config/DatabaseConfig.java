package dev.rambris.springmultipledb.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class DatabaseConfig {

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    @ConfigurationProperties(prefix="datasource.customer")
    public DataSource customerDataSource() {
        log.info("Creating customerDataSource");
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix="datasource.product")
    public DataSource productDataSource() {
        log.info("Creating productDataSource");
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix="datasource.order")
    public DataSource orderDataSource() {
        log.info("Creating orderDataSource");
        return DataSourceBuilder.create().build();
    }

    @Bean
    public NamedParameterJdbcTemplate customerTemplate(DataSource customerDataSource) throws SQLException {
        var ds = customerDataSource.unwrap(HikariDataSource.class);
        log.info("Creating customerTemplate. poolName={} maximumPoolSize={}", ds.getPoolName(), ds.getMaximumPoolSize());
        return new NamedParameterJdbcTemplate(customerDataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate productTemplate(DataSource productDataSource) throws SQLException {
        var ds = productDataSource.unwrap(HikariDataSource.class);
        log.info("Creating productTemplate poolName={} maximumPoolSize={}", ds.getPoolName(), ds.getMaximumPoolSize());
        return new NamedParameterJdbcTemplate(productDataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate orderTemplate(DataSource orderDataSource) throws SQLException {
        var ds = orderDataSource.unwrap(HikariDataSource.class);
        log.info("Creating orderTemplate poolName={} maximumPoolSize={}", ds.getPoolName(), ds.getMaximumPoolSize());
        return new NamedParameterJdbcTemplate(orderDataSource);
    }

    @Bean
    public JdbcClient customerClient(DataSource customerDataSource) throws SQLException {
        var ds = customerDataSource.unwrap(HikariDataSource.class);
        log.info("Creating customerClient. poolName={} maximumPoolSize={}", ds.getPoolName(), ds.getMaximumPoolSize());
        return JdbcClient.create(customerDataSource);
    }

    @Bean
    public JdbcClient productClient(DataSource productDataSource) throws SQLException {
        var ds = productDataSource.unwrap(HikariDataSource.class);
        log.info("Creating productClient. poolName={} maximumPoolSize={}", ds.getPoolName(), ds.getMaximumPoolSize());
        return JdbcClient.create(productDataSource);
    }

    @Bean
    public JdbcClient orderClient(DataSource orderDataSource) throws SQLException {
        var ds = orderDataSource.unwrap(HikariDataSource.class);
        log.info("Creating orderClient. poolName={} maximumPoolSize={}", ds.getPoolName(), ds.getMaximumPoolSize());
        return JdbcClient.create(orderDataSource);
    }
}
