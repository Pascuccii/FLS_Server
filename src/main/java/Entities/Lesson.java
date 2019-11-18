package Entities;

import Connectivity.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

public class Lesson {
    private int id;
    private int groupId;
    private int teacher_subjectId;
    private int cabinet;
    private String date;
    private String time;

    public Lesson(int id, int groupId, int teacher_subjectId, int cabinet, String date, String time) {
        this.id = id;
        this.groupId = groupId;
        this.teacher_subjectId = teacher_subjectId;
        this.cabinet = cabinet;
        this.date = date;
        this.time = time;
    }

    public Lesson(String lesson) {
        String[] vals = lesson.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.groupId = Integer.parseInt(vals[1]);
        if (!vals[2].equals("null")) this.teacher_subjectId = Integer.parseInt(vals[2]);
        if (!vals[3].equals("null")) this.cabinet = Integer.parseInt(vals[3]);
        if (!vals[4].equals("null")) this.date = vals[4];
        if (!vals[5].equals("null")) this.time = vals[5];
    }

    public Lesson(){}

    @Override
    public String toString() {
        return id +
                "|" + groupId +
                "|" + teacher_subjectId +
                "|" + cabinet +
                "|" + date +
                "|" + time + "|";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getTeacher_subjectId() {
        return teacher_subjectId;
    }

    public void setTeacher_subjectId(int teacher_subjectId) {
        this.teacher_subjectId = teacher_subjectId;
    }

    public int getCabinet() {
        return cabinet;
    }

    public void setCabinet(int cabinet) {
        this.cabinet = cabinet;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setGroupIdDB(DatabaseConnection conn, String groupId) {
            this.groupId = Integer.parseInt(groupId);
            try {
                String prepStat = "UPDATE lesson SET groupId = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setInt(1, Integer.parseInt(groupId));
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public void setTeacherSubjectIdDB(DatabaseConnection conn, String teacherSubjectId) {
            this.teacher_subjectId = Integer.parseInt(teacherSubjectId);
            try {
                String prepStat = "UPDATE lesson SET teacher_subjectId = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setInt(1, Integer.parseInt(teacherSubjectId));
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public void setCabinetDB(DatabaseConnection conn, String cabinet) {
        this.cabinet = Integer.parseInt(cabinet);
        try {
            String prepStat = "UPDATE lesson SET cabinet = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setInt(1, Integer.parseInt(cabinet));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setDateDB(DatabaseConnection conn, String date) {
        this.date = date;
        try {
            String prepStat = "UPDATE lesson SET date = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setDate(1, Date.valueOf(date));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void setTimeDB(DatabaseConnection conn, String time) {
        this.time = time;
        try {
            String prepStat = "UPDATE lesson SET time = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setTime(1, Time.valueOf(time));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDB(DatabaseConnection conn) {
        try {
            String prepStat = "DELETE FROM lesson WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(1, this.id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(DatabaseConnection conn, String field, String value) {
        switch (field) {
            case "setGroupId":
                setGroupIdDB(conn, value);
                break;
            case "setTeacherSubjectId":
                setTeacherSubjectIdDB(conn, value);
                break;
            case "setCabinet":
                setCabinetDB(conn, value);
                break;
            case "setDate":
                setDateDB(conn, value);
                break;
            case "setTime":
                setTimeDB(conn, value);
                break;
            case "delete":
                deleteDB(conn);
                break;
        }
    }
}
