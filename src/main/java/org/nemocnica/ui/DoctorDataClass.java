package org.nemocnica.ui;

//w sumie to co w bazie, dodać resztę kolumn w ramach potrzeb
public class DoctorDataClass {
    private int id;
    private String firstName;
    private String lastName;

    private boolean oKButtonClicked;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isoKButtonClicked() {
        return oKButtonClicked;
    }

    public void setoKButtonClicked(boolean oKButtonClicked) {
        this.oKButtonClicked = oKButtonClicked;
    }
}
