package org.nemocnica.ui;

import org.apache.commons.lang3.StringUtils;
import org.nemocnica.utils.UserMessageException;

//w sumie to co w bazie, dodać resztę kolumn w ramach potrzeb
public class DoctorDataClass {
    private int id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String position;
    private Integer chiefDoctorId;
    private Integer departmentId;
    private Double salary;

    private String onlyLettersRegex = "^[a-zA-Z]+$";
    private String capitalFirstLetterRegex = "[A-Z]\\S+";
    private String onlyNumbersRegex = "^[0-9]+$";
    private String onlyDoubleRegex = "^(-?)(0|([1-9][0-9]*))(\\.[0-9]+)?$";

    private boolean oKButtonClicked;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFirstName(String firstName) throws UserMessageException {
        if(StringUtils.isEmpty(firstName)) {
            throw new UserMessageException("First name data incomplete");
        }
        else if(!firstName.matches(onlyLettersRegex)) {
            throw new UserMessageException("First name can only contain letters");
        }
        else if(!firstName.matches(capitalFirstLetterRegex)) {
            throw new UserMessageException("First letter of first name should be uppercase");
        }
        else {
            this.firstName = firstName;
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setLastName(String lastName) throws UserMessageException {
        if(StringUtils.isEmpty(lastName)) {
            throw new UserMessageException("Last name data incomplete");
        }
        else if(!lastName.matches(onlyLettersRegex)) {
            throw new UserMessageException("Last name can only contain letters");
        }
        else if(!lastName.matches(capitalFirstLetterRegex)) {
            throw new UserMessageException("First letter of last name should be uppercase");
        }
        else {
            this.lastName = lastName;
        }
    }

    public String getLastName() {
        return lastName;
    }

    public void setSpecialization(String specialization) throws UserMessageException {
        if(StringUtils.isEmpty(specialization)) {
            throw new UserMessageException("Specialization data incomplete");
        }
        else {
            this.specialization = specialization;
        }
    }

    public String getSpecialization() { return specialization;}

    public void setPosition(String position) throws UserMessageException {
        if(StringUtils.isEmpty(position)) {
            throw new UserMessageException("Position data incomplete");
        }
        else {
            this.position = position;
        }
    }

    public String getPosition() {return position;}

    public void setChiefDoctorId(String chiefDoctorId) throws UserMessageException {
        if(StringUtils.isEmpty(chiefDoctorId)) {
            this.chiefDoctorId = null;
        }
        else if(!chiefDoctorId.matches(onlyNumbersRegex)) {
            throw new UserMessageException("Chief doctor Id must be a number");
        }
        else {
            int chiefDoctorIdInt = Integer.parseInt(chiefDoctorId);
            if(chiefDoctorIdInt < 0) {
                throw new UserMessageException("Chief doctor Id can't be a negative number");
            }
            else {
                this.chiefDoctorId = chiefDoctorIdInt;
            }
        }
    }

    public Integer getChiefDoctorId() {return chiefDoctorId;}

    public void setDepartmentId(String departmentId) throws UserMessageException {
        if(StringUtils.isEmpty(departmentId)) {
            throw new UserMessageException("Department Id data incomplete");
        }
        else if(!departmentId.matches(onlyNumbersRegex)) {
            throw new UserMessageException("Department Id must be a number");
        }
        else {
            int departmentIdInt = Integer.parseInt(departmentId);
            if(departmentIdInt < 0) {
                throw new UserMessageException("Department Id can't be a negative number");
            }
            else {
                this.departmentId = departmentIdInt;
            }
        }
    }

    public Integer getDepartmentId() {return departmentId;}

    public void setSalary(String salary) throws UserMessageException {
        if(StringUtils.isEmpty(salary)) {
            this.salary = 0.0;
        }
        else if(!salary.matches(onlyDoubleRegex)) {
            throw new UserMessageException("Salary must be a number");
        }
        else {
            Double salaryDouble = Double.parseDouble(salary);
            if(salaryDouble < 0.0) {
                throw new UserMessageException("Salary can't be a negative number");
            }
            else {
                this.salary = salaryDouble;
            }
        }
    }

    public Double getSalary() {return salary;}

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
        insert += "'" + getSpecialization() +"',";  //med_specialisation not null - daje na rympał - potem z danych kopiować
        insert += "'" + getPosition() + "',";  // position not null - daje na rympał, potem z danych brać
        insert += getChiefDoctorId() + ","; //chief_doctor_id może byc null - potem cos wymyslę
        insert += getDepartmentId() + ","; //department_id potem jakies powiązanie
        insert += getSalary(); //salary NOT NULL na razie na rympał
        insert += ")";
        return insert;
    }

    public String getUpdateString(){
        String update = "UPDATE DOCTORS SET ";
        update += "name='" + getFirstName() + "',";
        update += "surname='" + getLastName() + "'";
        //reszta wartości jak bedzie mozna edytować

        update += " WHERE doctor_id=" + getId();
        return update;
    }
}
