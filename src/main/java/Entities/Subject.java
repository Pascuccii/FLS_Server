package Entities;

import Connectivity.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class Subject {
    private int id;
    private String name;
    private Integer hours;

    public Subject(int id, String name, Integer hours) {
        this.id = id;
        this.name = name;
        this.hours = hours;
    }

    public Subject(String subject) {
        String[] vals = subject.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.name = vals[1];
        if (!vals[2].equals("null")) this.hours = Integer.parseInt(vals[2]);
    }

    public Subject(){}

    @Override
    public String toString() {
        return id +
                "|" + name +
                "|" + hours + "|";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHours() {
        return Objects.requireNonNullElse(hours, 0);
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public void deleteDB(DatabaseConnection conn) {
        try {
            String prepStat = "DELETE FROM subject WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(1, this.id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setNameDB(DatabaseConnection conn, String name) {
        this.name = name;
        try {
            String prepStat = "UPDATE subject SET name = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setHoursDB(DatabaseConnection conn, String hours) {
        this.hours = Integer.parseInt(hours);
        try {
            String prepStat = "UPDATE subject SET hours = ? WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(2, this.id);
            preparedStatement.setInt(1, Integer.parseInt(hours));
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void set(DatabaseConnection conn, String field, String value) {
        switch (field) {
            case "setName":
                setNameDB(conn, value);
                break;
            case "setHours":
                setHoursDB(conn, value);
                break;
            case "delete":
                deleteDB(conn);
                break;
        }
    }
}
