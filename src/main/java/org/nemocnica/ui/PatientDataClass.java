package org.nemocnica.ui;

import org.apache.commons.lang3.StringUtils;
import org.nemocnica.utils.UserMessageException;

public class PatientDataClass {
    private int id;
    private Integer doctorId;
    private Integer departmentId;
    private String firstName;
    private String lastName;
    private boolean oKButtonClicked;
    private static final String onlyLettersRegex = "^[a-zA-Z]+$";
    private static final String capitalFirstLetterRegex = "[A-Z]\\S+";

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDoctorId(Integer doctorId) throws UserMessageException {
        if (doctorId == null) {
            throw new UserMessageException("Doctor Id can't be a negative number");
        }
        this.doctorId = doctorId;
    }
    public void setFirstName(String name) throws UserMessageException  {
        if (!name.matches(onlyLettersRegex)) {
            throw new UserMessageException("First name can only contain letters");
        } else if (!name.matches(capitalFirstLetterRegex)) {
            throw new UserMessageException("First letter of first name should be uppercase");
        } else {
            this.firstName = name;
        }
    }

    public void setLastName(String surname) throws UserMessageException  {
        if (!surname.matches(onlyLettersRegex)) {
            throw new UserMessageException("Last name can only contain letters");
        } else if (!surname.matches(capitalFirstLetterRegex)) {
            throw new UserMessageException("First letter of last name should be uppercase");
        } else {
            this.lastName = surname;
        }
    }


    public int getDoctorId() {
        return doctorId;
    }

    public void setDepartmentId(int departmentId) throws UserMessageException {
        if (departmentId < 0) {
            throw new UserMessageException("Department Id can't be a negative number");
        }
        this.departmentId = departmentId;
    }

    public String getLastName() { return lastName; }
    public String getFirstName() {
        return firstName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public boolean isoKButtonClicked() {
        return oKButtonClicked;
    }

    public void setoKButtonClicked(boolean oKButtonClicked) {
        this.oKButtonClicked = oKButtonClicked;
    }

    public String getInsertString() {
        String insert = "INSERT INTO PATIENTS (name, surname, main_doctor_id,department_id) VALUES (";
        insert += "'" + getFirstName() + "',"; //name
        insert += "'" + getLastName() + "',"; //surname
        insert += getDoctorId() + ",";
        insert += getDepartmentId();


        insert += ")";
        return insert;
    }

    public String getUpdateString() {
        String update = "UPDATE PATIENTS SET ";
        update += "name='" + getFirstName() + "',";
        update += "surname='" + getLastName() + "',";
        update += "main_doctor_id=" + getDoctorId() + ",";
        update += "department_id=" + getDepartmentId();
        update += " WHERE department_id=" + getId();
        return update;
    }
}
