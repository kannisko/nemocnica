package org.nemocnica.ui;

import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.AppProperties;
import org.nemocnica.utils.ComboDictionaryItem;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;


public class Departments {

    private JPanel panel;
    private JTable departmentsTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;

    Connection connection;

    public Departments(Connection connection) {
        this.connection = connection;
        addButton.addActionListener(e -> addNewDepartment());
        editButton.addActionListener(e -> editDepartment());
        deleteButton.addActionListener(e -> deleteDepartment());
        try {
            refreshDepartmentsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public JPanel getPanel () {
        return panel;
    }

    private void deleteDepartment() {
        int selectedRow = departmentsTable.getSelectedRow();
        if (selectedRow < 0) { //nic nie zaznaczone, może pusto?
            return;
        }
        //id jest pierwszą kolumną, wartośc wyciagamy z modelu, zakładamy,że jest typu int
        Object objectId = departmentsTable.getModel().getValueAt(selectedRow, 0);

        try {
            DatabaseOperations.deleteFromTable(connection, "DEPARTMENTS", "DEPARTMENT_ID", objectId.toString());
            refreshDepartmentsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void addNewDepartment() {
        DepartmentDataClass data = new DepartmentDataClass();
        DepartmentsAddEdit box = new DepartmentsAddEdit("Wprowadź nowy departament", data, true, connection);
        box.setVisible(true);
        if(data.isoKButtonClicked()) {
            String insertString = data.getInsertString();
            try {
                DatabaseOperations.executeStatement(connection, insertString);
                refreshDepartmentsTab();
            } catch (UserMessageException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    private void editDepartment() {
        int selectedRow = departmentsTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        TableModel model = departmentsTable.getModel();
        DepartmentDataClass data = new DepartmentDataClass();
        try {
            TableColumnModel tableColumnModel = departmentsTable.getColumnModel();
            data.setId((int) model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("id")));
            data.setDepartmentName(model.getValueAt(selectedRow, tableColumnModel.getColumnIndex("Nazwa")).toString());
        } catch (UserMessageException e) {
            throw new IllegalStateException("This should never happen, illegal values in database");
        }
        DepartmentsAddEdit box = new DepartmentsAddEdit("Edytuj dane departamentu", data, false, connection);
        box.setVisible(true);
        if (data.isoKButtonClicked()) {
            String updateString = data.getUpdateString();
            try {
                DatabaseOperations.executeStatement(connection, updateString);
                refreshDepartmentsTab();
            } catch (UserMessageException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }

    TableModel getTableModel () throws UserMessageException {

        try (Statement stmt = connection.createStatement()) {
            String sql =
                    "SELECT DEPARTMENTS.department_id, DEPARTMENTS.name " +
                            "FROM departments ";

            String[] columnNames = new String[]{
                    "id", "Nazwa"

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

    private void refreshDepartmentsTab () throws UserMessageException {
        TableModel tableModel = getTableModel();
        departmentsTable.setModel(tableModel);
        departmentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (tableModel.getRowCount() > 0) {
            departmentsTable.setRowSelectionInterval(0, 0);
        }
    }

    }
