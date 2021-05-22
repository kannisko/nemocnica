package org.nemocnica.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    MainFrame() {
        super("Aplikacja szpitalna");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setNewContent(JPanel newPanel, boolean resizable){
        Container contentPane=getContentPane();
        contentPane.removeAll();
        contentPane.add(newPanel);
        pack();
        setResizable(resizable);
        setLocationRelativeTo(null);
    }

    public void setAdminPanel(){
        setNewContent(new AdminPanel(this).getPanel(),false);
    }

    public void setUserAdminChooser(){
        setNewContent(new AdminUserChooser(this).getPanel(),false);
    }

    public void setUserPanel(){
        setNewContent( new UserPanel(this).getPanel(),true);
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setUserAdminChooser();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
