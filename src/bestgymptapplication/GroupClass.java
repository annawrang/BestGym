package bestgymptapplication;

import java.sql.Date;

public class GroupClass extends Class{
    private int spacesAvailable;

    public GroupClass(int id, Instructor tempInstructor, ExerciseType exerciseType, String name, Date date) {
        super(id, tempInstructor, exerciseType, name, date);
    }

    public int getSpacesAvailable() {
        return spacesAvailable;
    }

    public void setSpacesAvailable(int spacesAvailable) {
        this.spacesAvailable = spacesAvailable;
    }
}
