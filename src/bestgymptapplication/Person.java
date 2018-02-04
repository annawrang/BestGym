package bestgymptapplication;

public abstract class Person {

    private int id;
    private String firstName;
    private String surName;
    private String address;
    private String phoneNr;
    private String password;

    public Person(int id, String namn, String surname) {
        this.id = id;
        this.firstName = namn;
        this.surName = surname;
    }
    
    public Person(String name, String surname){
        this.firstName=name;
        this.surName=surname;
    }

    public Person(int id, String name, String surname, String password) {
        this.id = id;
        this.firstName = name;
        this.surName = surname;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
