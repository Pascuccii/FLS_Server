package Entities;

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
}
