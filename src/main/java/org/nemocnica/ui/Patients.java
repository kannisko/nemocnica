package org.nemocnica.ui;

import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.ComboDictionaryItem;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

public class Patients {
    private JPanel panel;
    private JTable patientsTable;
    private JButton editButton;
    private JButton addButton;
    private JButton deleteButton;

    Connection connection;

    public Patients(Connection connection) {
        this.connection = connection;
        addButton.addActionListener(e -> addNewPatient());
        editButton.addActionListener(e -> editPatient());
        deleteButton.addActionListener(e -> deletePatient());


        try {
            refreshPatientsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public JPanel getPanel () {
        return panel;
    }

    private void deletePatient() {
        int selectedRow = patientsTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }

        Object objectId = patientsTable.getModel().getValueAt(selectedRow, 0);

        try {
            DatabaseOperations.deleteFromTable(connection, "PATIENTS", "PATIENT_ID", objectId.toString());
            refreshPatientsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void addNewPatient() {
        PatientDataClass data = new PatientDataClass();
        PatientsAddEdit box = new PatientsAddEdit("Wprowadź dane pacjenta", data, true, connection);
        box.setVisible(true);
        if (data.isoKButtonClicked()) {
            String insertString = data.getInsertString();
            try {
                DatabaseOperations.executeStatement(connection, insertString);
                refreshPatientsTab();
            } catch (UserMessageException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    private void editPatient() {
        int selectedRow = patientsTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        TableModel model = patientsTable.getModel();
        PatientDataClass data = new PatientDataClass();
        try {
            TableColumnModel tableColumnModel = patientsTable.getColumnModel();
            data.setId((int) model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("id")));
            data.setDoctorId(((ComboDictionaryItem)model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Doktor prowadzący"))).getId());
            data.setDepartmentId(((ComboDictionaryItem)model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Nazwa oddziału"))).getId());
        } catch (UserMessageException e) {
            throw new IllegalStateException("This should never happen, illegal values in database");
        }
        PatientsAddEdit box = new PatientsAddEdit("Edytuj dane pacjenta", data, false, connection);
        box.setVisible(true);
        if (data.isoKButtonClicked()) {
            String updateString = data.getUpdateString();
            try {
                DatabaseOperations.executeStatement(connection, updateString);
                refreshPatientsTab();
            } catch (UserMessageException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    TableModel getTableModel () throws UserMessageException {

        try (Statement stmt = connection.createStatement()) {
            String sql =
                    "SELECT PATIENTS.patient_id, PATIENTS.name, PATIENTS.surname, PATIENTS.main_doctor_id, DOCTORS.name, DOCTORS.surname, PATIENTS.department_id, DEPARTMENTS.name " +
                            "FROM PATIENTS " +
                            "LEFT JOIN DEPARTMENTS ON PATIENTS.department_id = DEPARTMENTS.department_id " +
                    "LEFT JOIN DOCTORS ON PATIENTS.main_doctor_id = DOCTORS.doctor_id ";

            String[] columnNames = new String[]{
                    "id", "Imie", "Nazwisko", "Doktor prowadzący", "Nazwa oddziału"

            };
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            Vector<String> columnNamesVector = new Vector<>(Arrays.asList(columnNames));
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> currentRow = new Vector<>();
                currentRow.add(rs.getInt(1));
                currentRow.add(rs.getString(2));
                currentRow.add(rs.getString(3));
                int main_doctor_id = rs.getInt(4);
                String main_doctor_name = rs.getString(5);
                String main_doctor_surname = rs.getString(6);
                if (main_doctor_surname != null)
                {
                    main_doctor_surname += " " + main_doctor_name;

                }
                int department_id = rs.getInt(7);
                String department_name = rs.getString(8);
                currentRow.add(new ComboDictionaryItem(main_doctor_id,main_doctor_surname));
                currentRow.add(new ComboDictionaryItem(department_id, department_name));

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

    private void refreshPatientsTab() throws UserMessageException {
        TableModel tableModel = getTableModel();
        patientsTable.setModel(tableModel);
        patientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (tableModel.getRowCount() > 0) {
            patientsTable.setRowSelectionInterval(0, 0);
        }
    }
    
}
