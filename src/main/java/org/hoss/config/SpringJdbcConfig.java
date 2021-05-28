package org.hoss.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
public class SpringJdbcConfig {

    @Value("${SPRING_DATASOURCE_USERNAME}")
    private String jdbcUsername;

    @Value("${SPRING_DATASOURCE_PASSWORD}")
    private String jdbcPassword;

    @Value("${SPRING_DATASOURCE_URL}")
    private String jdbcUrl;

    @Bean
    public DataSource pgsqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(this.jdbcUrl);
        dataSource.setUsername(this.jdbcUsername);
        dataSource.setPassword(this.jdbcPassword);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    public String getJdbcUsername() {return this.jdbcUsername;}
}
