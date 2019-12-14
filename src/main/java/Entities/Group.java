package Entities;

import Connectivity.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Group {
    private int id;
    private String level;
    private String subject;

    public Group(int id, String level, String subject) {
        this.id = id;
        this.level = level;
        this.subject = subject;
    }

    public Group(String group) {
        String[] vals = group.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.level = vals[1];
        if (!vals[2].equals("null")) this.subject = vals[2];
    }

    public Group() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return id + "|" + level + "|" + subject + "|";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setLevelDB(DatabaseConnection conn, String level) {
        if (level.equals("A") || level.equals("B") || level.equals("C")) {
            this.level = level;
            try {
                String prepStat = "UPDATE `group` SET Level = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, level);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSubjectDB(DatabaseConnection conn, String subject) {
        this.subject = subject;
        try {
            String prepStat = "UPDATE `group` SET subjectId = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, subject);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteDB(DatabaseConnection conn) {
        try {
            String prepStat = "DELETE FROM `group` WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(1, this.id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void set(DatabaseConnection conn, String field, String value) {
        switch (field) {
            case "setLevel":
                setLevelDB(conn, value);
                break;
            case "setSubject":
                setSubjectDB(conn, value);
                break;
            case "delete":
                deleteDB(conn);
                break;
        }
    }

}
