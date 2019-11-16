package Entities;

import java.util.Objects;

public class Student {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private int groupId;
    private String email;
    private String phone;

    public Student(int id, String name, String surname, String patronymic, int groupId, String email, String phone) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.groupId = groupId;
        this.email = email;
        this.phone = phone;
    }

    public Student(String student) {
        String[] vals = student.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.name = vals[1];
        if (!vals[2].equals("null")) this.surname = vals[2];
        if (!vals[3].equals("null")) this.patronymic = vals[3];
        if (!vals[4].equals("null")) this.groupId = Integer.parseInt(vals[4]);
        if (!vals[5].equals("null")) this.email = vals[5];
        if (!vals[6].equals("null")) this.phone = vals[6];
    }

    @Override
    public String toString() {
        return id +
                "|" + name +
                "|" + surname +
                "|" + patronymic +
                "|" + groupId +
                "|" + email +
                "|" + phone + "|";
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

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getEmail() {
        return Objects.requireNonNullElse(email, "");
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return Objects.requireNonNullElse(phone, "");
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
