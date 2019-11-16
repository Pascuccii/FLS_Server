package Entities;

public class TeacherSubject {
    private int id;
    private int teacherId;
    private int subjectId;

    public TeacherSubject(int id, int teacherId, int subjectId) {
        this.id = id;
        this.teacherId = teacherId;
        this.subjectId = subjectId;
    }

    public TeacherSubject(String teacherSubject) {
        String[] vals = teacherSubject.split("\\|");
        if (!vals[0].equals("null")) this.id = Integer.parseInt(vals[0]);
        if (!vals[1].equals("null")) this.teacherId = Integer.parseInt(vals[1]);
        if (!vals[2].equals("null")) this.subjectId = Integer.parseInt(vals[2]);
    }

    @Override
    public String toString() {
        return id +
                "|" + teacherId +
                "|" + subjectId + "|";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
