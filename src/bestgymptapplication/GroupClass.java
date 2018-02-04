package bestgymptapplication;

import java.sql.Date;

public class GroupClass extends Class{
    private int spacesAvailable;

    public GroupClass(Instructor tempInstructor, ExerciseType exerciseType, String name, Date date, String comment) {
        super(tempInstructor, exerciseType, name, date, comment);
    }

    public int getSpacesAvailable() {
        return spacesAvailable;
    }

    public void setSpacesAvailable(int spacesAvailable) {
        this.spacesAvailable = spacesAvailable;
    }
}
