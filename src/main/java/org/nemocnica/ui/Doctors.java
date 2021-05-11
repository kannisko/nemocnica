package org.nemocnica.ui;

import javax.swing.*;

public class Doctors {
    private JPanel panel;
    private JTable doctorsTable;
    private JScrollBar doctorsScroll;
    private JButton addButton;
    private JButton deleteButton;
    private JButton editButton;

    public Doctors(){

        addButton.addActionListener(e->addNewDoctor());
        editButton.addActionListener(e->editDoctor());
    }
    public JPanel getPanel(){
        return panel;
    }

    private void addNewDoctor(){
        //czyste dane, bo nowy
        DoctorDataClass data = new DoctorDataClass();
        DoctorsAddEdit box = new DoctorsAddEdit("Wprowadź nowego lekarza",data,true);
        box.setVisible(true);
        if( data.isoKButtonClicked()){
            JOptionPane.showMessageDialog(null, "OK kliknięte - trzeba dodawać nowego");
        }
    }

    private void editDoctor(){
        //dane tego co wybrane w tabeli
        DoctorDataClass data = new DoctorDataClass();
        data.setFirstName("Koziołek");
        data.setLastName("Matołek");

        DoctorsAddEdit box = new DoctorsAddEdit("Edytuj dane lekarza",data,false);
        box.setVisible(true);
        if( data.isoKButtonClicked()){
            JOptionPane.showMessageDialog(null, "OK kliknięte - trzeba będzie zmienic dane");
        }

    }


    private void createUIComponents() {
        String[][] data = {
                { "Jan", "Kowalski" },
                { "Teresa", "Orlowski" }
        };
        String[] columnNames = { "Imię","Nazwisko" };
        doctorsTable = new JTable(data,columnNames);
    }
}
