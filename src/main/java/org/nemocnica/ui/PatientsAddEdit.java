package org.nemocnica.ui;

import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.ComboDictionaryItem;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.Vector;

public class PatientsAddEdit extends JDialog {
    private JPanel contentPane;
    private JComboBox doctorCombo;
    private JComboBox departmentCombo;
    private JButton buttonCancel;
    private JButton buttonOK;

    private PatientDataClass data;
    boolean add;

    public PatientsAddEdit(String title, PatientDataClass data, boolean add, Connection connection) {
        this.data = data;
        this.add = add;
        setTitle(title);
        setContentPane(contentPane);
        pack();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);
        fillDoctorsComboBox(connection);
        fillDepartmentsComboBox(connection);

        if (!this.add) {
            doctorCombo.setSelectedItem(new ComboDictionaryItem(data.getDoctorId(), null));
            departmentCombo.setSelectedItem(new ComboDictionaryItem(data.getDepartmentId(),null));
        }

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            data.setDoctorId(((ComboDictionaryItem)doctorCombo.getSelectedItem()).getId());
            data.setDepartmentId(((ComboDictionaryItem)departmentCombo.getSelectedItem()).getId());

            data.setoKButtonClicked(true);
            dispose();
        }
        catch (UserMessageException exception) {
            JOptionPane.showMessageDialog(contentPane, exception.getMessage());
        }
    }

    private void onCancel() {
        data.setoKButtonClicked(false);
        dispose();
    }

    private void fillDoctorsComboBox(Connection connection) {
        Vector<ComboDictionaryItem> items = DatabaseOperations.getComboDictionary(connection, "DOCTORS", "doctor_id", "name");
        DefaultComboBoxModel model = new DefaultComboBoxModel(items);
        doctorCombo.setModel(model);
    }

    private void fillDepartmentsComboBox(Connection connection) {
        Vector<ComboDictionaryItem> items = DatabaseOperations.getComboDictionary(connection,"DEPARTMENTS", "department_id", "name");
        DefaultComboBoxModel model = new DefaultComboBoxModel(items);
        departmentCombo.setModel(model);
    }
}
