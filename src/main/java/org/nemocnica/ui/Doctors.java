package org.nemocnica.ui;

import org.nemocnica.database.DatabaseOperations;
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
        deleteButton.addActionListener(e -> deleteDoctor());

        try {
            refreshDoctorsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    private void deleteDoctor(){
        int selectedRow = doctorsTable.getSelectedRow();
        if( selectedRow < 0){ //nic nie zaznaczone, może pusto?
            return;
        }
        //id jest pierwszą kolumną, wartośc wyciagamy z modelu, zakładamy,że jset typu int
        Object objectId = doctorsTable.getModel().getValueAt(selectedRow,0);

        try {
            DatabaseOperations.deleteFromTable(connection,"DOCTORS","DOCTOR_ID",objectId.toString());
            refreshDoctorsTab();
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void addNewDoctor() {
        //czyste dane, bo nowy
        DoctorDataClass data = new DoctorDataClass();
        DoctorsAddEdit box = new DoctorsAddEdit("Wprowadź nowego lekarza", data, true);
        box.setVisible(true);
        if (data.isoKButtonClicked()) {
            //pobierz string dla inserta dla bazy i wykonaj
            String insertString  = data.getInsertString();
            try {
                DatabaseOperations.executeStatement(connection,insertString);
                refreshDoctorsTab();
            } catch (UserMessageException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
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
        //try-with-resources - zamknie statement nawet jak wyjatek czy cos takiego
        //https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
        try (Statement stmt = connection.createStatement() ){
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

        } catch (SQLException exception) {
            throw new UserMessageException(exception.getMessage());
        }

    }

    private void refreshDoctorsTab() throws UserMessageException {
        TableModel tableModel = getTableModel();
        doctorsTable.setModel(tableModel);
        // https://stackoverflow.com/questions/18309113/jtable-how-to-force-user-to-select-exactly-one-row
        doctorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorsTable.setRowSelectionInterval(0,0);
    }
}
