package org.hoss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;


@Configuration
public class SpringJdbcConfig {

    @Bean
    public DataSource pgsqlDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://ec2-3-214-136-47.compute-1.amazonaws.com/dfvge3198d0ojl");
        dataSource.setUsername("nxieynoygcixtk");
        dataSource.setPassword("fc8125b38a49dd3bc13db5f6c519ed141703f7163003a926235b9576545b4d06");
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
