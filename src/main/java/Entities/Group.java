package Entities;

import Connectivity.DatabaseConnection;
import enums.Level;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Group {
    private int id;
    private String level;

    public Group(int id, String level) {
        this.id = id;
        this.level = level;
    }

    public Group(String group) {
        String[] vals = group.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.level = vals[1];
    }

    public Group(){}

    @Override
    public String toString() {
        return id + "|" + level + "|";
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
            case "delete":
                deleteDB(conn);
                break;
        }
    }
    
}
