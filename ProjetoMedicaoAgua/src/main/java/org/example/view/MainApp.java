package org.example.view;

import javax.swing.*;
import java.awt.*;

public class MainApp extends JFrame {

    public MainApp() {
        setTitle("Sistema de Gerenciamento de Hidrômetros");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10)); 
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnCliente = new JButton("Área do Cliente");
        JButton btnTecnico = new JButton("Área do Técnico");

        btnCliente.setFont(new Font("Arial", Font.BOLD, 16));
        btnTecnico.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(btnCliente);
        panel.add(btnTecnico);

        add(panel);
        btnCliente.addActionListener(_ -> {
            MenuCliente menuCliente = new MenuCliente();
            menuCliente.setVisible(true);
        });


        btnTecnico.addActionListener(_ -> {
            MenuTecnico menuTecnico = new MenuTecnico();
            menuTecnico.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainApp mainApp = new MainApp();
            mainApp.setVisible(true);
        });
    }
}