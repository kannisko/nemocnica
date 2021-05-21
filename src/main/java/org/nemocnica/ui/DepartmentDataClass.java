package org.nemocnica.ui;

import org.apache.commons.lang3.StringUtils;
import org.nemocnica.utils.UserMessageException;

public class DepartmentDataClass {
    private int id;
    private String departmentName;

    private String onlyLettersRegex = "^[a-zA-Z]+$";
    private String capitalFirstLetterRegex = "[A-Z]\\S+";

    private boolean oKButtonClicked;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDepartmentName(String departmentName) throws UserMessageException {
        if (StringUtils.isEmpty(departmentName)) {
            throw new UserMessageException("First name data incomplete");
        } else if (!departmentName.matches(onlyLettersRegex)) {
            throw new UserMessageException("First name can only contain letters");
        } else if (!departmentName.matches(capitalFirstLetterRegex)) {
            throw new UserMessageException("First letter of first name should be uppercase");
        } else {
            this.departmentName = departmentName;
        }
    }

    public String getDepartmentName() {return departmentName;}

    public boolean isoKButtonClicked() {
        return oKButtonClicked;
    }

    public void setoKButtonClicked(boolean oKButtonClicked) {
        this.oKButtonClicked = oKButtonClicked;
    }

    public String getInsertString() {
        String insert = "INSERT INTO DEPARTMENTS (name) VALUES VALUES (";
        insert += "'" + getDepartmentName() + "',"; //name
        insert += ")";
        return insert;
    }

    public String getUpdateString() {
        String update = "UPDATE DEPARTMENTS SET ";
        update += "name='" + getDepartmentName() + "',";

        update += " WHERE doctor_id=" + getId();
        return update;
    }
}