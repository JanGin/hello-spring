package me.chan.spring.demo;

import lombok.extern.slf4j.Slf4j;
import me.chan.spring.demo.service.BarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement(mode= AdviceMode.PROXY)
@Slf4j
public class TransactionDemoApplication implements CommandLineRunner  {


    @Autowired
    private BarService barService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(TransactionDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        barService.insertRecord();
        String result = jdbcTemplate.queryForObject("SELECT foo FROM bar", (rs, i) -> rs.getString(1));
        log.info("result is equivalent to 11:{}", "11".equals(result));
        log.info("count of the table bar is :{}", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bar", Long.class));
        try {
            barService.insertThenRollback();
        } catch (Exception e) {
            log.info("count of the table bar is :{}", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bar", Long.class));
        }
        try {
            barService.doInsertThenRollback();
        } catch (Exception e) {
            log.info("count of the table bar is :{}", jdbcTemplate.queryForObject("SELECT COUNT(*) FROM bar", Long.class));
        }
    }
}
