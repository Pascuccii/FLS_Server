package Entities;

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
}
