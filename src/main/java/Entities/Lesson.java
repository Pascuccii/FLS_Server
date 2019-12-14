package Entities;

import Connectivity.DatabaseConnection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;

public class Lesson {
    private int id;
    private int groupId;
    private int teacherId;
    private int cabinet;
    private String date;
    private String time;

    public Lesson(int id, int groupId, int teacherId, int cabinet, String date, String time) {
        this.id = id;
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.cabinet = cabinet;
        this.date = date;
        this.time = time;
    }

    public Lesson(String lesson) {
        String[] vals = lesson.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.groupId = Integer.parseInt(vals[1]);
        if (!vals[2].equals("null")) this.teacherId = Integer.parseInt(vals[2]);
        if (!vals[3].equals("null")) this.cabinet = Integer.parseInt(vals[3]);
        if (!vals[4].equals("null")) this.date = vals[4];
        if (!vals[5].equals("null")) this.time = vals[5];
    }

    public Lesson(){}

    @Override
    public String toString() {
        return id +
                "|" + groupId +
                "|" + teacherId +
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

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
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
    public void setTeacherIdDB(DatabaseConnection conn, String teacherId) {
            this.teacherId = Integer.parseInt(teacherId);
            try {
                String prepStat = "UPDATE lesson SET teacherId = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setInt(1, Integer.parseInt(teacherId));
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
            case "setTeacherId":
                setTeacherIdDB(conn, value);
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
