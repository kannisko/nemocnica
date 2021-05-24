package org.nemocnica.ui;

import javax.swing.*;

public class AdminUserChooser {
    private JButton adminButton;
    private JButton userButton;
    private JPanel panel;
    private JButton exitButton;

    public JPanel getPanel() {
        return panel;
    }

    public AdminUserChooser( MainFrame topLevelFrame){

        exitButton.addActionListener(e->topLevelFrame.dispose());

        adminButton.addActionListener(e->topLevelFrame.setAdminPanel());
        userButton.addActionListener(e->topLevelFrame.setUserPanel());
        //e->JOptionPane.showMessageDialog(topLevelFrame, "Jak wymyślę, wynik przyślę (okienko usera)"));
    }
}
