package com.salvatorio.daotalk.dao;

import com.salvatorio.daotalk.domain.Group;
import com.salvatorio.daotalk.domain.Student;
import org.junit.After;
import org.junit.Before;
import org.junit.runners.Parameterized;
import com.salvatorio.daotalk.mysql.MySqlDaoFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;


public class MySqlDaoTest extends GenericDaoTest<Connection> {

    private Connection connection;

    private GenericDao dao;

    private static final DaoFactory<Connection> factory = new MySqlDaoFactory();

    @Parameterized.Parameters
    public static Collection getParameters() {
        return Arrays.asList(new Object[][]{
                {Group.class, new Group()},
                {Student.class, new Student()}
        });
    }

    @Before
    public void setUp() throws PersistException, SQLException {
        connection = factory.getContext();
        connection.setAutoCommit(false);
        dao = factory.getDao(connection, daoClass);
    }

    @After
    public void tearDown() throws SQLException {
        context().rollback();
        context().close();
    }

    @Override
    public GenericDao dao() {
        return dao;
    }

    @Override
    public Connection context() {
        return connection;
    }

    public MySqlDaoTest(Class clazz, Identified<Integer> notPersistedDto) {
        super(clazz, notPersistedDto);
    }
}
