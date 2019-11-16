package Entities;

import enums.Level;

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
}
