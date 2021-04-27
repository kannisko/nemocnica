package org.nemocnica.ui;

import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.AppProperties;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import java.awt.event.*;

public class AdminPanel{
    private JPanel panel;
    private JButton backButton;
    private JButton addTestData;
    private JButton createTables;
    private JButton createDataBase;
    private JTextField databaseName;
    private JButton chooseFolderButton;
    private JButton buttonCancel;

    private AppProperties appProperties;
    private MainFrame topLevelFrame;

    public AdminPanel(MainFrame topLevelFrame) {
        this.topLevelFrame = topLevelFrame;
        this.appProperties = AppProperties.getInstance();

//        setTitle("Nemocnica - Admin Panel");
//        setContentPane(panel);
//        setModal(true);
//        setResizable(false);
//        setLocationRelativeTo(null);
//        getRootPane().setDefaultButton(backButton);
        databaseName.setText(this.appProperties.getDatabasenamePath());

        backButton.addActionListener(e->topLevelFrame.setUserAdminChooser());

        chooseFolderButton.addActionListener(e -> onChooseDatabaseFolder());

        createDataBase.addActionListener(e -> {
            onCreateDataBase();
        });
    }

    public JPanel getPanel() {
        return panel;
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
            JOptionPane.showMessageDialog(topLevelFrame, "Dabase created succesfully");
        } catch (UserMessageException exception) {

            JOptionPane.showMessageDialog(topLevelFrame, exception.getMessage());
        }
    }

}
