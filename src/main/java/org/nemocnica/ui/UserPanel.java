package org.nemocnica.ui;

import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.AppProperties;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class UserPanel {
    //główna ramka programu
    private MainFrame topLevelFrame;
    //głowny element wizualny tego widoku
    private JPanel panel;

    private JTabbedPane tabbedPane;

    private Connection connection;
    public UserPanel(MainFrame topLevelFrame) {
        AppProperties appProperties = AppProperties.getInstance();
        String databaseName = appProperties.getDatabasenamePath();
        try {
            this.connection = DatabaseOperations.connectToDatabase(databaseName);
        } catch (UserMessageException e) {
            JOptionPane.showMessageDialog(topLevelFrame, e.getMessage());
        }

        this.topLevelFrame = topLevelFrame;
        this.panel = new JPanel();
        this.panel.setLayout(new GridLayout(1, 1));
        this.panel.setBorder(BorderFactory.createRaisedBevelBorder());

        this.tabbedPane = new JTabbedPane();

        JPanel doctorsPanel = new Doctors(connection).getPanel();
        tabbedPane.addTab("Lekarze", null, doctorsPanel,
                "Edycja lekarzy");


        JComponent departmentsPanel = new Departments(connection).getPanel();
        tabbedPane.addTab("Departamenty", null, departmentsPanel,
                "");

        JComponent patientsPanel = new Patients(connection).getPanel();
        tabbedPane.addTab("Pacjenci", null, patientsPanel,
                "");

        JComponent nursesPanel = new Nurses(connection).getPanel();
        tabbedPane.addTab("Pielęgniarki", null, nursesPanel,
                "");

        tabbedPane.addTab("", null, new JPanel(),
                "");
        JButton button = new JButton("Do głównego");
        button.addActionListener(e->{
            if( this.topLevelFrame != null){
                this.topLevelFrame.setUserAdminChooser();
            }else{

                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "cofnie do głownego panelu wyboru");

            }
        });

        tabbedPane.setTabComponentAt(4,button);



        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);//jaby zakładek było tyle,że się nie mieszczą
        panel.add(tabbedPane);

    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    public JPanel getPanel() {
        return panel;
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("testowe okienko");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        UserPanel userPanel = new UserPanel(null);
        contentPane.add(userPanel.getPanel() );
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);//centruj na ekranie
        frame.setVisible(true);

    }

}
