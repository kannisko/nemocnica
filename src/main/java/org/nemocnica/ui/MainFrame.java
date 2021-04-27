package org.nemocnica.ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame{
    MainFrame() {
        super("Nemocnica na kraji mesta");
        setSize(400, 300);
        //setResizable(false);
//        frame.setLayout(new GridLayout(2,1,20,20));



//        JButton adminButton = new JButton("Admin");
//        adminButton.addActionListener(
//                //LAMBDA taki myk, musisz poczytac co to
//                e->{
//                    AdminPanelDialog dialog = new AdminPanelDialog();
//                    dialog.pack();
//                    dialog.setVisible(true);
//                }
//        );
//
//        frame.add(adminButton);
//
//        JButton userButton = new JButton("User");
//        //lambda znowu
//        userButton.addActionListener(e-> JOptionPane.showMessageDialog(frame, "Jak wymyślę, wynik przyślę (okienko usera)"));
//        frame.add(userButton);

        //position on center of the screen
        setLocationRelativeTo(null);
        //show and run
//        frame.setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setNewContent(JPanel newPanel, boolean resizable){
        Container contentPane=getContentPane();
        contentPane.removeAll();
        contentPane.add(newPanel);
        pack();
        setResizable(resizable);
    }

    public void setAdminPanel(){
        setNewContent(new AdminPanel(this).getPanel(),false);
    }
    public void setUserAdminChooser(){
        setNewContent(new AdminUserChooser(this).getPanel(),false);
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setUserAdminChooser();
        frame.setVisible(true);
    }
}
