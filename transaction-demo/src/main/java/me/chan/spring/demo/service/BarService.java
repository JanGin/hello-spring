package me.chan.spring.demo.service;

import java.sql.SQLTransactionRollbackException;

/**
 * Created by JanGin
 */
public interface BarService {

    void insertRecord();


    void insertThenRollback() throws SQLTransactionRollbackException;

    void doInsertThenRollback() throws  SQLTransactionRollbackException;
}
