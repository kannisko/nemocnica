package org.nemocnica.ui;

import org.nemocnica.utils.ComboDictionaryItem;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.util.Arrays;
import java.util.Vector;

public class Nurses {
    
    private JPanel panel;
    private JTable nursesTable;
    private JButton editButton;
    private JButton addButton;
    private JButton deleteButton;

    Connection connection;

    public Nurses(Connection connection) {
        this.connection = connection;
        /*addButton.addActionListener(e -> addNewDepartment());
        editButton.addActionListener(e -> editDepartment());
        deleteButton.addActionListener(e -> deleteDepartment());*/
        try {
            refreshNursesTab();
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
                    "SELECT NURSES.nurse_id, NURSES.name, NURSES.surname, NURSES.chief_nurse_id, NURSESHEAD.name, NURSESHEAD.surname, NURSES.department_id, DEPARTMENTS.name, NURSES.salary "+
                            "FROM NURSES " +
                            "LEFT JOIN DEPARTMENTS ON NURSES.department_id = DEPARTMENTS.department_id " +
                            "LEFT JOIN NURSES AS NURSESHEAD ON NURSES.chief_nurse_id = NURSESHEAD.nurse_id";

            String[] columnNames = new String[]{
                    "id", "Imię", "Nazwisko", "Przełozony", "Departament", "Płaca"

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

                Integer chief_nurse_id = rs.getInt(4);
                String chief_name = rs.getString(5);
                String chief_surname = rs.getString(6);
                if( chief_surname != null){
                    chief_surname += " " + chief_name;
                }
                currentRow.add(new ComboDictionaryItem(chief_nurse_id,chief_surname));

                Integer department_id = rs.getInt(7);
                String department_name = rs.getString(8);
                currentRow.add(new ComboDictionaryItem(department_id,department_name));
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

    private void refreshNursesTab() throws UserMessageException {
        TableModel tableModel = getTableModel();
        nursesTable.setModel(tableModel);
        nursesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        if (tableModel.getRowCount() > 0) {
            nursesTable.setRowSelectionInterval(0, 0);
        }
    }
    
    
}
