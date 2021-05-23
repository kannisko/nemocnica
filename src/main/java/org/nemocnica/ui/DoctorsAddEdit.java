package org.nemocnica.ui;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.Vector;

import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.ComboDictionaryItem;
import org.nemocnica.utils.UserMessageException;

public class DoctorsAddEdit extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField firstName;
    private JTextField lastName;
    private JTextField specialization;
    private JTextField position;
    private JTextField salary;
    private JComboBox departmentCombo;
    private JComboBox chiefCombo;

    private DoctorDataClass data;
    boolean add; //dodajemy nowego czy edytujemy?

    public DoctorsAddEdit(String title, DoctorDataClass data, boolean add, Connection connection) {
        this.data = data;
        this.add = add;
        setTitle(title);
        setContentPane(contentPane);
        pack();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);
        fillDepartmentsComboBox(connection);
        fillChiefDoctorCombo(connection);

        if(!this.add){
            firstName.setText(data.getFirstName());
            lastName.setText(data.getLastName());
            specialization.setText(data.getSpecialization());
            position.setText(data.getPosition());
            chiefCombo.setSelectedItem(new ComboDictionaryItem(data.getChiefDoctorId(),null));
            departmentCombo.setSelectedItem(new ComboDictionaryItem(data.getDepartmentId(),null));
            salary.setText(data.getSalary().toString());
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
            data.setFirstName(firstName.getText());
            data.setLastName(lastName.getText());
            data.setSpecialization(specialization.getText());
            data.setPosition(position.getText());
            data.setChiefDoctorId(((ComboDictionaryItem)chiefCombo.getSelectedItem()).getId());

            data.setDepartmentId(((ComboDictionaryItem)departmentCombo.getSelectedItem()).getId());
            data.setSalary(salary.getText());

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

    private void fillDepartmentsComboBox(Connection connection){
        Vector<ComboDictionaryItem> items = DatabaseOperations.getComboDictionary(connection, false,"DEPARTMENTS", "department_id", "name");
        DefaultComboBoxModel model = new DefaultComboBoxModel(items);
        departmentCombo.setModel(model);
    }

    private void fillChiefDoctorCombo(Connection connection){
        Vector<ComboDictionaryItem> items = DatabaseOperations.getComboDictionary(connection, true,"DOCTORS", "doctor_id", "surname","name");
        DefaultComboBoxModel model = new DefaultComboBoxModel(items);
        chiefCombo.setModel(model);
    }
}
