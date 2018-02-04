package bestgymptapplication;

public abstract class Employee extends Person {

    public Employee(int id, String name, String surname, String password) {
        super(id, name, surname, password);

    }
    
    public Employee(String name, String surname){
        super(name, surname);
    }

}
