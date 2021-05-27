package org.nemocnica.ui;

import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.database.ROW_EXPORTER;
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
    private JButton exportData;

    private AppProperties appProperties;
    private MainFrame topLevelFrame;

    public AdminPanel(MainFrame topLevelFrame) {
        this.topLevelFrame = topLevelFrame;
        this.appProperties = AppProperties.getInstance();

        databaseName.setText(this.appProperties.getDatabasenamePath());

        backButton.addActionListener(e->topLevelFrame.setUserAdminChooser());

        chooseFolderButton.addActionListener(e -> onChooseDatabaseFolder());

        createDataBase.addActionListener(e -> onCreateDataBase());
        createTables.addActionListener(e->onCreateTables());
        addTestData.addActionListener(e->onAddTestData());
        exportData.addActionListener(e->onExportData());
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

    private void onCreateTables() {
        try {
            DatabaseOperations.createTables(appProperties.getDatabasenamePath());
            JOptionPane.showMessageDialog(topLevelFrame, "Tables created succesfully");
        } catch (UserMessageException exception) {
            JOptionPane.showMessageDialog(topLevelFrame, exception.getMessage());
        }

    }
    private void onAddTestData() {
        try {
            DatabaseOperations.addTestData(appProperties.getDatabasenamePath());
            JOptionPane.showMessageDialog(topLevelFrame, "Test data added");
        } catch (UserMessageException exception) {
            JOptionPane.showMessageDialog(topLevelFrame, exception.getMessage());
        }
    }

    private void onExportData(){
        //katalog do którego wywalić tablice, może doróbcie jakis wybór?
        String exportToPath = "nemocnica.dump";
        ROW_EXPORTER exporter = ROW_EXPORTER.TO_SQL_INSERT; //też może jakiś wybór zrobić?
        try{
        DatabaseOperations.exportDataBase(appProperties.getDatabasenamePath(),exportToPath,exporter);
            JOptionPane.showMessageDialog(topLevelFrame, "Tables exported");
        } catch (UserMessageException exception) {
            JOptionPane.showMessageDialog(topLevelFrame, exception.getMessage());
        }

    }
}
