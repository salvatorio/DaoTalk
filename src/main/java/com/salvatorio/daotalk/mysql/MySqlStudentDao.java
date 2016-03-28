package com.salvatorio.daotalk.mysql;

import com.salvatorio.daotalk.domain.Student;
import com.salvatorio.daotalk.dao.AbstractJDBCDao;
import com.salvatorio.daotalk.dao.PersistException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class MySqlStudentDao extends AbstractJDBCDao<Student, Integer> {

    private class PersistStudent extends Student {
        public void setId(int id) {
            super.setId(id);
        }
    }

    @Override
    public String getSelectQuery() {
        return "SELECT id, name, surname, enrolment_date, group_id FROM daotalk.Student ";
    }

    @Override
    public String getCreateQuery() {
        return "INSERT INTO daotalk.Student (name, surname, enrolment_date, group_id) \n" +
                "VALUES (?, ?, ?, ?);";
    }

    @Override
    public String getUpdateQuery() {
        return "UPDATE daotalk.Student \n" +
                "SET name = ?, surname  = ?, enrolment_date = ?, group_id = ? \n" +
                "WHERE id = ?;";
    }

    @Override
    public String getDeleteQuery() {
        return "DELETE FROM daotalk.Student WHERE id= ?;";
    }

    @Override
    public Student create() throws PersistException {
        Student s = new Student();
        return persist(s);
    }

    public MySqlStudentDao(Connection connection) {
        super(connection);
    }

    @Override
    protected List<Student> parseResultSet(ResultSet rs) throws PersistException {
        LinkedList<Student> result = new LinkedList<Student>();
        try {
            while (rs.next()) {
                PersistStudent student = new PersistStudent();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setSurname(rs.getString("surname"));
                student.setEnrolmentDate(rs.getDate("enrolment_date"));
                student.setGroupId(rs.getInt("group_id"));
                result.add(student);
            }
        } catch (Exception e) {
            throw new PersistException(e);
        }
        return result;
    }

    @Override
    protected void prepareStatementForUpdate(PreparedStatement statement, Student object) throws PersistException {
        try {
            Date sqlDate = convert(object.getEnrolmentDate());
            statement.setString(1, object.getName());
            statement.setString(2, object.getSurname());
            statement.setDate(3, sqlDate);
            statement.setInt(4, object.getGroupId());
            statement.setInt(5, object.getId());
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    @Override
    protected void prepareStatementForInsert(PreparedStatement statement, Student object) throws PersistException {
        try {
            Date sqlDate = convert(object.getEnrolmentDate());
            int groupId = (object.getGroupId() == null) ? 0 : object.getGroupId();

            statement.setString(1, object.getName());
            statement.setString(2, object.getSurname());
            statement.setDate(3, sqlDate);
            statement.setInt(4, groupId);
        } catch (Exception e) {
            throw new PersistException(e);
        }
    }

    protected java.sql.Date convert(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }
}
