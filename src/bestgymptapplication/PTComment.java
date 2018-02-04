package bestgymptapplication;

public class PTComment {
    private int id;
    private Instructor instructor;
    private String comment;
    private Member member;
    private Class theClass;

    PTComment(int id, Instructor instruct, Member memb, String comment, Class clas) {
        this.id=id;
        this.instructor=instruct;
        this.member=memb;
        this.comment=comment;
        this.theClass=clas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Instructor getInsructor() {
        return instructor;
    }

    public void setInsructor(Instructor insructor) {
        this.instructor = insructor;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Class getTheClass() {
        return theClass;
    }

    public void setTheClass(Class theClass) {
        this.theClass = theClass;
    }
}
