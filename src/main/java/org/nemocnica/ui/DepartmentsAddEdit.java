package org.nemocnica.ui;

import javax.swing.*;
import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.UserMessageException;

import java.awt.event.*;
import java.sql.Connection;

public class DepartmentsAddEdit extends JDialog{
    private JPanel contentPane;
    private JButton buttonCancel;
    private JButton buttonOK;
    private JTextField departmentName;
    private DepartmentDataClass data;
    boolean add;
    public DepartmentsAddEdit(String title, DepartmentDataClass data, boolean add, Connection connection) {
        this.data = data;
        this.add = add;
        setTitle(title);
        setContentPane(contentPane);
        pack();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);

        if(!this.add){
            departmentName.setText(data.getDepartmentName());
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
            data.setDepartmentName(departmentName.getText());

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
}
