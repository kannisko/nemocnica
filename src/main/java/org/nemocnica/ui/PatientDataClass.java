package org.nemocnica.ui;

import org.apache.commons.lang3.StringUtils;
import org.nemocnica.utils.UserMessageException;

public class PatientDataClass {
    private int id;
    private Integer doctorId;
    private Integer departmentId;

    private boolean oKButtonClicked;

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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDepartmentId(int departmentId) throws UserMessageException {
        if (departmentId < 0) {
            throw new UserMessageException("Department Id can't be a negative number");
        }
        this.departmentId = departmentId;
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
        String insert = "INSERT INTO PATIENTS (main_doctor_id,department_id) VALUES (";
        insert += getDoctorId() + ","; //Doctor id
        insert += getDepartmentId();
        insert += ")";
        return insert;
    }

    public String getUpdateString() {
        String update = "UPDATE PATIENTS SET ";
        update += "main_doctor_id=" + getDoctorId() + ",";
        update += "department_id=" + getDepartmentId();

        update += " WHERE department_id=" + getId();
        return update;
    }
}
