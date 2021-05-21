package org.nemocnica.ui;

import org.nemocnica.utils.ComboDictionaryItem;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        /*addButton.addActionListener(e -> addNewDepartment());
        editButton.addActionListener(e -> editDepartment());
        deleteButton.addActionListener(e -> deleteDepartment());*/
        try {
            refreshPatientsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    public JPanel getPanel () {
        return panel;
    }
    TableModel getTableModel () throws UserMessageException {

        try (Statement stmt = connection.createStatement()) {
            String sql =
                    "SELECT PATIENTS.patient_id, PATIENTS.main_doctor_id, DOCTORS.name, DOCTORS.surname, PATIENTS.department_id " +
                            "FROM PATIENTS " +
                            "LEFT JOIN DEPARTMENTS ON PATIENTS.department_id = DEPARTMENTS.department_id " +
                    "LEFT JOIN DOCTORS ON PATIENTS.main_doctor_id = DOCTORS.doctor_id ";

            String[] columnNames = new String[]{
                    "id", "Doktor prowadzący", "Nazwa oddziału"

            };
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            Vector<String> columnNamesVector = new Vector<>(Arrays.asList(columnNames));
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> currentRow = new Vector<>();
                currentRow.add(rs.getInt(1));
                int main_doctor_id = rs.getInt(2);
                String main_doctor_name = rs.getString(3);
                String main_doctor_surname = rs.getString(4);
                if (main_doctor_surname != null)
                {
                    main_doctor_surname += " " + main_doctor_name;

                }
                currentRow.add(new ComboDictionaryItem(main_doctor_id,main_doctor_surname));
                currentRow.add(rs.getInt(5));
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
