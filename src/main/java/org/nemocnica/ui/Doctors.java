package org.nemocnica.ui;

import org.apache.commons.lang3.StringUtils;
import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.AppProperties;
import org.nemocnica.utils.ComboDictionaryItem;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

public class Doctors {
    private JPanel panel;
    private JTable doctorsTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;
    private JTextField nameFilter;
    private JTextField surnameFilter;
    private JButton clearFilterButton;

    Connection connection;

    DocumentListener filterEventListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            doFilter();
        }
        @Override
        public void removeUpdate(DocumentEvent e) {
            doFilter();
        }
        @Override
        public void changedUpdate(DocumentEvent e) {
            doFilter();
        }
    };

    public Doctors(Connection connection) {
        this.connection = connection;
        addButton.addActionListener(e -> addNewDoctor());
        editButton.addActionListener(e -> editDoctor());
        deleteButton.addActionListener(e -> deleteDoctor());
        clearFilterButton.addActionListener(e->{
            nameFilter.setText("");
            surnameFilter.setText("");
        });

        nameFilter.getDocument().addDocumentListener(filterEventListener);
        surnameFilter.getDocument().addDocumentListener(filterEventListener);

        try {
            refreshDoctorsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    private void doFilter(){
        try {
            refreshDoctorsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void deleteDoctor() {
        int selectedRow = doctorsTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        Object objectId = doctorsTable.getModel().getValueAt(selectedRow, 0);

        try {
            DatabaseOperations.deleteFromTable(connection, "DOCTORS", "DOCTOR_ID", objectId.toString());
            refreshDoctorsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void addNewDoctor() {

        DoctorDataClass data = new DoctorDataClass();
        DoctorsAddEdit box = new DoctorsAddEdit("Wprowadź nowego lekarza", data, true, connection);
        box.setVisible(true);
        if (data.isoKButtonClicked()) {

            String insertString = data.getInsertString();
            try {
                DatabaseOperations.executeStatement(connection, insertString);
                refreshDoctorsTab();
            } catch (UserMessageException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    private void editDoctor() {
        int selectedRow = doctorsTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        TableModel model = doctorsTable.getModel();

        DoctorDataClass data = new DoctorDataClass();
        try {
            TableColumnModel tableColumnModel = doctorsTable.getColumnModel();
            data.setId((int) model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("id")));
            data.setFirstName(model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Imię")).toString());
            data.setLastName(model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Nazwisko")).toString());
            data.setSpecialization(model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Specjalizacja")).toString());
            data.setPosition(model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Stanowisko")).toString());
            data.setChiefDoctorId(((ComboDictionaryItem)model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Przełozony"))).getId());
            data.setDepartmentId(((ComboDictionaryItem)model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Departament"))).getId());
            data.setSalary(model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Płaca")).toString());
        } catch (UserMessageException e) {
            throw new IllegalStateException("This should never happen, illegal values in database");
        }
        DoctorsAddEdit box = new DoctorsAddEdit("Edytuj dane lekarza", data, false, connection);
        box.setVisible(true);
        if (data.isoKButtonClicked()) {
            String updateString = data.getUpdateString();
            try {
                DatabaseOperations.executeStatement(connection, updateString);
                refreshDoctorsTab();
            } catch (UserMessageException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    TableModel getTableModel() throws UserMessageException {

        try (Statement stmt = connection.createStatement()) {
            String filterNameString  = nameFilter.getText();
            String filterSurnameString = surnameFilter.getText();
            String sql =
                    "SELECT DOCTORS.doctor_id,DOCTORS.name,DOCTORS.surname,DOCTORS.med_specialisation,DOCTORS.position,DOCTORS.chief_doctor_id,DOCTORS.department_id,DEPARTMENTS.name,DOCTORS.salary,DOC2.name,DOC2.surname "+
                            "FROM DOCTORS " +
                            "LEFT JOIN DEPARTMENTS ON DOCTORS.department_id=DEPARTMENTS.department_id "+
                            "LEFT JOIN DOCTORS AS DOC2 ON DOCTORS.chief_doctor_id = DOC2.doctor_id";
            String filterString = " WHERE";
            boolean andRequired = false;

            if(!StringUtils.isEmpty(filterNameString)){
                if( andRequired){
                    filterString += " AND";
                }
                filterString += " DOCTORS.name LIKE '%" + filterNameString + "%'";
                andRequired = true;
            }

            if(!StringUtils.isEmpty(filterSurnameString)){
                if( andRequired){
                    filterString += " AND";
                }
                filterString += " DOCTORS.surname LIKE '%" + filterSurnameString + "%'";
                andRequired = true;
            }

            if( andRequired ){
                sql += filterString;
            }

            String columnNames[] = new String[]{
                    "id", "Imię", "Nazwisko", "Specjalizacja", "Stanowisko", "Przełozony",  "Departament", "Płaca"

            };
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            Vector<String> columnNamesVector = new Vector<>(Arrays.asList(columnNames));

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> currentRow = new Vector<>();

                currentRow.add(rs.getInt(1));
                //imię, nazwisko,specjalizacja, stanowisko
                currentRow.add(rs.getString(2));
                currentRow.add(rs.getString(3));
                currentRow.add(rs.getString(4));
                currentRow.add(rs.getString(5));

                //przełozony
                Integer chief_doctor_id = rs.getInt(6);

                String chief_name = rs.getString(10);
                String chief_surname = rs.getString(11);
                if( chief_surname != null){
                    //imie i nazwisko not null, chyba że nie ma przełozonego
                    chief_surname += " " + chief_name;
                }
                currentRow.add(new ComboDictionaryItem(chief_doctor_id,chief_surname));


                //departament
                int deptId = rs.getInt(7);
                String deptName = rs.getString(8);
                currentRow.add(new ComboDictionaryItem(deptId,deptName));
                //salary
                currentRow.add(rs.getObject(9));

                data.add(currentRow);
            }


            return new DefaultTableModel(data, columnNamesVector) {


                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

        } catch (SQLException exception) {
            throw new UserMessageException(exception.getMessage());
        }

    }

    private void refreshDoctorsTab() throws UserMessageException {
        TableModel tableModel = getTableModel();
        doctorsTable.setModel(tableModel);
        doctorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (tableModel.getRowCount() > 0) {
            doctorsTable.setRowSelectionInterval(0, 0);
        }
    }
}
