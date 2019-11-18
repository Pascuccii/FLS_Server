package Entities;

import Connectivity.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Teacher {
    private int id;
    private String name;
    private String surname;
    private String patronymic;

    public Teacher(int id, String name, String surname, String patronymic) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
    }

    public Teacher(String teacher) {
        String[] vals = teacher.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.name = vals[1];
        if (!vals[2].equals("null")) this.surname = vals[2];
        if (!vals[3].equals("null")) this.patronymic = vals[3];
    }

    public Teacher(){}

    @Override
    public String toString() {
        return id +
                "|" + name +
                "|" + surname +
                "|" + patronymic + "|";
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }


    public void setNameDB(DatabaseConnection conn, String name) {
        if (name.matches("[а-яА-Я]{2,20}")) {
            this.name = name;
            try {
                String prepStat = "UPDATE teacher SET name = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, name);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSurnameDB(DatabaseConnection conn, String surname) {
        if (surname.matches("[а-яА-Я]{2,20}")) {
            this.surname = surname;
            try {
                String prepStat = "UPDATE teacher SET surname = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, surname);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPatronymicDB(DatabaseConnection conn, String patronymic) {
        if (patronymic.matches("[а-яА-Я]{2,30}")) {
            this.patronymic = patronymic;
            try {
                String prepStat = "UPDATE teacher SET patronymic = ? WHERE id = ?";
                PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
                preparedStatement.setInt(2, this.id);
                preparedStatement.setString(1, patronymic);
                preparedStatement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public void deleteDB(DatabaseConnection conn) {
        try {
            String prepStat = "DELETE FROM teacher WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(prepStat);
            preparedStatement.setInt(1, this.id);
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
            case "setSurname":
                setSurnameDB(conn, value);
                break;
            case "setPatronymic":
                setPatronymicDB(conn, value);
                break;
            case "delete":
                deleteDB(conn);
                break;
        }
    }
}
