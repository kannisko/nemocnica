package org.nemocnica.ui;

import org.apache.log4j.Layout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainChoise {


    MainChoise() {
        /* JFrame is a top level container (window)
         * where we would be adding our button
         */
        JFrame frame = new JFrame("Nemocnica na kraji mesta");
        frame.setSize(400, 300);
        frame.setResizable(false);
        frame.setLayout(new GridLayout(2,1,20,20));



        JButton adminButton = new JButton("Admin");
        adminButton.addActionListener(
                //LAMBDA taki myk, musisz poczytac co to
                e->{
                        AdminPanelDialog dialog = new AdminPanelDialog();
                        dialog.pack();
                        dialog.setVisible(true);
                }
            );

        frame.add(adminButton);

        JButton userButton = new JButton("User");
        //lambda znowu
        userButton.addActionListener(e-> JOptionPane.showMessageDialog(frame, "Jak wymyślę, wynik przyślę (okienko usera)"));
        frame.add(userButton);

        //position on center of the screen
        frame.setLocationRelativeTo(null);
        //show and run
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main(String[] args) {
        new MainChoise();
    }
}
