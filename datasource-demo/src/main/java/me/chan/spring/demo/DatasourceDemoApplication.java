package me.chan.spring.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Slf4j
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
                                    JdbcTemplateAutoConfiguration.class,
                        DataSourceTransactionManagerAutoConfiguration.class})
// 排除掉Spring boot自动装配的类
public class DatasourceDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatasourceDemoApplication.class, args);
    }



    @Bean
    @ConfigurationProperties("bar.datasource")
    public DataSourceProperties barDatasourceProp() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("foo.datasource")
    public DataSourceProperties fooDatasourceProp() {
        return new DataSourceProperties();
    }


    @Bean
    public DataSource barDatasource() {
        DataSourceProperties dsProp = barDatasourceProp();
        log.info("bar ds url:{}", dsProp.getUrl());
        return dsProp.initializeDataSourceBuilder().build();
    }

    @Bean
    public DataSource fooDatasource() {
        DataSourceProperties dsProp = fooDatasourceProp();
        log.info("foo ds url:{}", dsProp.getUrl());
        return dsProp.initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate fooJdbcTemplate() {
        DataSource foo = fooDatasource();
        return new JdbcTemplate(foo);
    }

    @Bean
    public JdbcTemplate barJdbcTemplate() {
        DataSource bar = barDatasource();
        return new JdbcTemplate(bar);
    }

    @Bean
    public DataSourceTransactionManager barTxManager() {
        return new DataSourceTransactionManager(barDatasource());
    }

    @Bean
    public DataSourceTransactionManager fooTxManager() {
        return new DataSourceTransactionManager(fooDatasource());
    }
}




