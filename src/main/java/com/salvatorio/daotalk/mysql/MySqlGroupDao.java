package com.salvatorio.daotalk.mysql;

import com.salvatorio.daotalk.domain.Group;
import com.salvatorio.daotalk.dao.AbstractJDBCDao;
import com.salvatorio.daotalk.dao.PersistException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class MySqlGroupDao extends AbstractJDBCDao<Group, Integer> {

    private class PersistGroup extends Group {
        public void setId(int id) {
            super.setId(id);
        }
    }


    @Override
    public String getSelectQuery() {
        return "SELECT id, number, department FROM daotalk.Group";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO daotalk.Group (number, department) \n" +
                "VALUES (?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE daotalk.Group SET number= ? department = ? WHERE id= ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM daotalk.Group WHERE id= ?;";
    }

    @Override
    public Group create() throws PersistException {
        Group g = new Group();
        return persist(g);
    }

    public MySqlGroupDao(Connection connection) {
        super(connection);
    }

    @Override
    protected List<Group> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Group> result = new LinkedList<Group>();
        try {
            while (rs.next()) {
                PersistGroup group = new PersistGroup();
                group.setId(rs.getInt("id"));
                group.setNumber(rs.getInt("number"));
                group.setDepartment(rs.getString("department"));
                result.add(group);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Group object) throws PersistException {
        try {
            statement.setInt(1, object.getNumber());
            statement.setString(2, object.getDepartment());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Group object) throws PersistException {
        try {
            statement.setInt(1, object.getNumber());
            statement.setString(2, object.getDepartment());
            statement.setInt(3, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }
}
