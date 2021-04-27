package org.nemocnica.ui;

import javax.swing.*;

public class AdminUserChooser {
    private JButton adminButton;
    private JButton userButton;
    private JPanel panel;
    private JButton exitButton;
    private MainFrame topLevelFrame;


    public JPanel getPanel() {
        return panel;
    }
    public AdminUserChooser( MainFrame topLevelFrame){
        this.topLevelFrame = topLevelFrame;

        exitButton.addActionListener(e->{
            topLevelFrame.dispose();
        });

        adminButton.addActionListener(e->topLevelFrame.setAdminPanel());
        userButton.addActionListener(e->JOptionPane.showMessageDialog(topLevelFrame, "Jak wymyślę, wynik przyślę (okienko usera)"));
    }
}
