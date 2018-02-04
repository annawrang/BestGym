package bestgymptapplication;

class Instructor extends Employee{
    private boolean ptQualification;
    
    public Instructor(int id, String name, String surname, String password){
        super(id, name, surname, password);
    }

    Instructor(String name, String surname) {
        super(name, surname);
    }

    public boolean isPtQualification() {
        return ptQualification;
    }

    public void setPtQualification(boolean ptQualification) {
        this.ptQualification = ptQualification;
    }
    
   
    
}
