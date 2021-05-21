package org.nemocnica.ui;

import org.nemocnica.database.DatabaseOperations;
import org.nemocnica.utils.AppProperties;
import org.nemocnica.utils.UserMessageException;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.sql.Connection;

//panel użytkownika
//kontrolki dodawane "z ręki", aby pokazać,że tak też umiemy
//opis jat tego użyć wyczytany w oficjalnej dokumentacji Oracle
//https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
//ale inne panele robimy w edytorze graficznym, bo lepiej

public class UserPanel {
    //główna ramka programu
    private MainFrame topLevelFrame;
    //głowny element wizualny tego widoku
    private JPanel panel;

    private JTabbedPane tabbedPane;

    //połaczenie do bazy danych, jedno wspólne dla wszystkich zakładek doktorzy/pacjenci i co tam dalej
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
        this.panel.setLayout(new GridLayout(1, 1)); //layout manager, dzięki temu kontrolki w środki się resizują
        this.panel.setBorder(BorderFactory.createRaisedBevelBorder()); //ładna ramka, taka sama jak dlas wszystkich innych paneli


        this.tabbedPane = new JTabbedPane();

        JPanel doctorsPanel = new Doctors(connection).getPanel();
        tabbedPane.addTab("Lekarze", null, doctorsPanel,
                "Edycja lekarzy");


        JComponent departmentsPanel = new Departments(connection).getPanel();
        tabbedPane.addTab("Departmenty", null, departmentsPanel,
                "");

        JComponent patientsPanel = new Patients(connection).getPanel();


        tabbedPane.addTab("Pacjenci", null, patientsPanel,
                "");

        JComponent panel4 = makeTextPanel(
                "Panel #4 (has a preferred size of 410 x 50).");
        panel4.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Tab 4", null, panel4,
                "Does nothing at all");


        //aby nie tracić miejsca na ekranie dodajemu button do powrotu do głownego panelu do paska z zakładkami
        //ściagniete z http://www.java2s.com/Tutorial/Java/0240__Swing/AddButtontotabbar.htm
        //pustu panel, nigdy  nie będzie widoczy, button na zakładce bedzie wracał do głownego panelu
        tabbedPane.addTab("", null, new JPanel(),
                "");
        JButton button = new JButton("Do głównego");
        button.addActionListener(e->{
            if( this.topLevelFrame != null){
                this.topLevelFrame.setUserAdminChooser();
            }else{
                //tryb testowy, z lokalnego main
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
        UserPanel userPanel = new UserPanel(null);//nie mamy MainFrame, ale do szybkiego poglądu obędziemy się bez
        contentPane.add(userPanel.getPanel() );
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);//centruj na ekranie
        frame.setVisible(true);

    }

}
