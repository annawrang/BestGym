package bestgymptapplication;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Class {

    private int id;
    private Date dateAndTime;
    private Instructor inctructor;
    private List<Member> memberList = new ArrayList<>();
    private Room room;
    private ExerciseType exerciseType;
    private String name;
    private String comment;

    Class(int id, Instructor tempInstructor, ExerciseType exerciseType, String name, Date date) {
        this.id=id;
        this.inctructor = tempInstructor;
        this.exerciseType = exerciseType;
        this.name=name;
        this.dateAndTime=date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Date dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Instructor getInctructor() {
        return inctructor;
    }

    public void setInctructor(Instructor inctructor) {
        this.inctructor = inctructor;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ExerciseType getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(ExerciseType exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
