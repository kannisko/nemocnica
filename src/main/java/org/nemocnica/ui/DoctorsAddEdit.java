package org.nemocnica.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DoctorsAddEdit extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField firstName;
    private JTextField lastName;
    private DoctorDataClass data;
    boolean add; //dodajemy nowego czy edytujemy?
    public DoctorsAddEdit(String title,DoctorDataClass data,boolean add) {
        this.data = data;
        this.add = add;
        setTitle(title);
        setContentPane(contentPane);
        pack();
        setResizable(false);
        setModal(true);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);

        if( !this.add){
            firstName.setText(data.getFirstName());
            lastName.setText(data.getLastName());
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
        // add your code here
        // pozbieraj dane z okienka, może jakaś validacja? coś nie może być puste?
        data.setFirstName(firstName.getText());
        data.setLastName(lastName.getText());
        data.setoKButtonClicked(true);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        data.setoKButtonClicked(false);
        dispose();
    }


}
