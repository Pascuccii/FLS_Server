package Entities;

import Connectivity.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TeacherSubject {
    private int id;
    private int teacherId;
    private int subjectId;

    public TeacherSubject(int id, int teacherId, int subjectId) {
        this.id = id;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }

    public TeacherSubject(String teacherSubject) {
        String[] vals = teacherSubject.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.teacherId = Integer.parseInt(vals[1]);
        if (!vals[2].equals("null")) this.subjectId = Integer.parseInt(vals[2]);
    }

    public TeacherSubject(){}

    @Override
    public String toString() {
        return id +
                "|" + teacherId +
                "|" + subjectId + "|";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public void deleteDB(DatabaseConnection conn) {
        try {
            String prepStat = "DELETE FROM teacher_subject WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(1, this.id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTeacherIdDB(DatabaseConnection conn, String teacherId) {
        this.teacherId = Integer.parseInt(teacherId);
        try {
            String prepStat = "UPDATE teacher_subject SET teacherId = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setInt(1, Integer.parseInt(teacherId));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setSubjectIdDB(DatabaseConnection conn, String subjectId) {
        this.subjectId = Integer.parseInt(subjectId);
        try {
            String prepStat = "UPDATE teacher_subject SET subjectId = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setInt(1, Integer.parseInt(subjectId));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(DatabaseConnection conn, String field, String value) {
        switch (field) {
            case "setTeacherId":
                setTeacherIdDB(conn, value);
                break;
            case "setSubjectId":
                setSubjectIdDB(conn, value);
                break;
            case "delete":
                deleteDB(conn);
                break;
        }
    }
}
