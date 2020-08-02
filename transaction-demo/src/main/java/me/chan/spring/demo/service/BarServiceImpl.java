package me.chan.spring.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLTransactionRollbackException;

/**
 * Created by JanGin
 */
@Service
public class BarServiceImpl implements BarService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public void insertRecord() {
        jdbcTemplate.execute("INSERT INTO BAR (foo) VALUES ('11')");
    }

    @Transactional(rollbackFor = SQLTransactionRollbackException.class)
    @Override
    public void insertThenRollback() throws SQLTransactionRollbackException {
        jdbcTemplate.execute("INSERT INTO BAR (foo) VALUES ('22')");
        throw new SQLTransactionRollbackException();
    }

    @Override
    public void doInsertThenRollback() throws SQLTransactionRollbackException {
        insertThenRollback();
    }
}
