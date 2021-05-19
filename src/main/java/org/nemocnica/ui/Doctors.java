package org.nemocnica.ui;

import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.sql.*;
import java.util.Vector;

public class Doctors {
    private JPanel panel;
    private JTable doctorsTable;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;

    Connection connection;

    public Doctors(Connection connection) {
        this.connection = connection;
        addButton.addActionListener(e -> addNewDoctor());
        editButton.addActionListener(e -> editDoctor());

        try {
            refreshDoctorsTab();
        } catch (UserMessageException e) {
            e.printStackTrace();
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    private void addNewDoctor() {
        //czyste dane, bo nowy
        DoctorDataClass data = new DoctorDataClass();
        DoctorsAddEdit box = new DoctorsAddEdit("Wprowadź nowego lekarza", data, true);
        box.setVisible(true);
        if (data.isoKButtonClicked()) {
            JOptionPane.showMessageDialog(null, "OK kliknięte - trzeba dodawać nowego");
        }
    }

    private void editDoctor() {
        //dane tego co wybrane w tabeli
        DoctorDataClass data = new DoctorDataClass();
        data.setFirstName("Koziołek");
        data.setLastName("Matołek");

        DoctorsAddEdit box = new DoctorsAddEdit("Edytuj dane lekarza", data, false);
        box.setVisible(true);
        if (data.isoKButtonClicked()) {
            JOptionPane.showMessageDialog(null, "OK kliknięte - trzeba będzie zmienic dane");
        }

    }

    TableModel getTableModel() throws UserMessageException {
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM DOCTORS";
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            Vector<String> columnNames = new Vector<>();
            for (int col = 1; col <= columnCount; col++) {
                columnNames.add(rsmd.getColumnName(col));
            }
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> currentRow = new Vector<>();
                for (int col = 1; col <= columnCount; col++) {
                    Object obj = rs.getObject(col);
                    currentRow.add(obj);
                }
                data.add(currentRow);
            }

            //tak zwana anonimowa klasa, nadpisuje niektóre metody
            return new DefaultTableModel(data, columnNames) {

                // https://stackoverflow.com/questions/21350011/java-how-to-disable-the-jtable-editable-when-click-it
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // or a condition at your choice with row and column
                }
            };

    } catch(
    SQLException exception)

    {
        throw new UserMessageException(exception.getMessage());
    }

}

    private void refreshDoctorsTab() throws UserMessageException {
        TableModel tableModel = getTableModel();
        doctorsTable.setModel(tableModel);
    }
}
