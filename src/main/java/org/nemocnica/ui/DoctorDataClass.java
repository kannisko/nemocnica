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

    public String getInsertString(){
        String insert = "INSERT INTO DOCTORS (name,surname,med_specialisation,position,chief_doctor_id,department_id,salary) VALUES (";
        insert += "'" + getFirstName() + "',"; //name
        insert += "'" + getLastName() +"',"; //surname
        insert += "'" + "fake scpecialization" +"',";  //med_specialisation not null - daje na rympał - potem z danych kopiować
        insert += "'" + "fake position" + "',";  // position not null - daje na rympał, potem z danych brać
        insert += "NULL,"; //chief_doctor_id może byc null - potem cos wymyslę
        insert += "4,"; //department_id potem jakies powiązanie
        insert += "3200"; //salary NOT NULL na razie na rympał
        insert += ")";
        return insert;
    }
}
