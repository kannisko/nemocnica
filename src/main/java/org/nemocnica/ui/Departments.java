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
        /*addButton.addActionListener(e -> addNewDepartment());
        editButton.addActionListener(e -> editDepartment());
        deleteButton.addActionListener(e -> deleteDepartment());*/
        try {
            refreshDepartmentsTab();
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
