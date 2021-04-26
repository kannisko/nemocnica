package org.nemocnica.ui;

import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.AppProperties;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

public class AdminPanelDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton addTestData;
    private JButton createTables;
    private JButton createDataBase;
    private JTextField databaseName;
    private JButton chooseFolderButton;
    private JButton buttonCancel;

    private AppProperties appProperties;


    public AdminPanelDialog() {
        this.appProperties = AppProperties.getInstance();

        setTitle("Nemocnica - Admin Panel");
        setContentPane(contentPane);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);
        databaseName.setText(this.appProperties.getDatabasenamePath());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        chooseFolderButton.addActionListener(e -> onChooseDatabaseFolder());

        createDataBase.addActionListener(e -> {
            onCreateDataBase();
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void onChooseDatabaseFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File(appProperties.getDatabasenamePath()));
        chooser.setDialogTitle("Wybierz katalog dla bazy danych:");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String databaseNameString = chooser.getSelectedFile().getAbsolutePath();
            databaseName.setText(databaseNameString);
            appProperties.setDatabasenamePath(databaseNameString);
        }
    }

    private void onCreateDataBase() {
        try {
            DatabaseOperations.createEmptyDatabase(appProperties.getDatabasenamePath());
            JOptionPane.showMessageDialog(this, "Dabase created succesfully");
        } catch (UserMessageException exception) {

            JOptionPane.showMessageDialog(this, exception.getMessage());
        }
    }

    public static void main(String[] args) {
        AdminPanelDialog dialog = new AdminPanelDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
