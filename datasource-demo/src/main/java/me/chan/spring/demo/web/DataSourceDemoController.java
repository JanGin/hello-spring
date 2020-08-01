package me.chan.spring.demo.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;

@RestController
@RequestMapping("ds")
@Slf4j
public class DataSourceDemoController {

    @Resource(name="fooDatasource")
    DataSource fooDS;

    @Resource(name="barDatasource")
    DataSource barDS;

    @Resource(name="fooJdbcTemplate")
    JdbcTemplate fooJdbc;

    @Resource(name="barJdbcTemplate")
    JdbcTemplate barJdbc;

    @PostMapping("fooInsert")
    public String insertDataWithFoo() {
        try {
            /*
            PreparedStatement ps = conn.prepareStatement("INSERT INO USER (id, name) VALUES (?, ?)");
            ps.setString(1, "1");
            ps.setString(2, "foo");
            ResultSet rs = ps.executeQuery();
            */
            String sql = "INSERT INTO USER (name) VALUES ('foo')";
            fooJdbc.execute(sql);
        } catch (Exception e) {
            log.error("error:{}", e.getMessage());
        }

        return "success";
    }

    @PostMapping("createTable")
    public String createTable() {
        Connection conn = null;
        try {
            conn = fooDS.getConnection();
            log.info("conn:{}", conn);
            Statement stmt = conn.createStatement();
            stmt.execute("DROP TABLE IF EXISTS `USER`");
            stmt.execute("CREATE TABLE `USER` (id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100));");

        } catch (SQLException e) {
            log.error("errors occur when creating table:{} ", e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("exception occurs: ", e);
                }
            }
        }
        return "success";
    }


    @GetMapping("barSelect")
    public String selectDataWithBar() {

        try {
            barJdbc.queryForList("SELECT * FROM `USER`").forEach(row -> System.out.println(row));
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }

    @GetMapping("fooSelect")
    public String selectDataWithFoo() {
        try {
            fooJdbc.queryForList("SELECT * FROM `USER`").forEach(row -> System.out.println(row));
        } catch (Exception e) {
            return e.getMessage();
        }
        return "success";
    }


}
