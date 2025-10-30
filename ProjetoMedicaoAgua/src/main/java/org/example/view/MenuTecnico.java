package org.example.view;

import javax.swing.*;
import java.awt.*;

public class MenuTecnico extends JFrame {

    public MenuTecnico() {
        setTitle("Menu do Técnico - Painel de Controle");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnGerenciarPF = new JButton("Gerenciar Pessoas Físicas");
        JButton btnGerenciarPJ = new JButton("Gerenciar Pessoas Jurídicas");
        JButton btnGerenciarImoveis = new JButton("Gerenciar Imóveis");
        JButton btnGerenciarContratos = new JButton("Gerenciar Contratos");
        JButton btnGerenciarHidrometros = new JButton("Gerenciar Hidrômetros"); 

        panel.add(btnGerenciarPF);
        panel.add(btnGerenciarPJ);
        panel.add(btnGerenciarImoveis);
        panel.add(btnGerenciarContratos);
        panel.add(btnGerenciarHidrometros);

        add(panel);

        btnGerenciarPF.addActionListener(_ -> {
            ViewGerenciarPessoaFisica viewPF = new ViewGerenciarPessoaFisica(this);
            viewPF.setVisible(true);
        });

        btnGerenciarPJ.addActionListener(_ -> {
            ViewGerenciarPessoaJuridica viewPJ = new ViewGerenciarPessoaJuridica(this);
            viewPJ.setVisible(true);
        });

        btnGerenciarImoveis.addActionListener(_ -> {
            ViewGerenciarImoveis viewImoveis = new ViewGerenciarImoveis(this);
            viewImoveis.setVisible(true);
        });
        
        btnGerenciarContratos.addActionListener(_ -> {
             ViewGerenciarContratos viewContratos = new ViewGerenciarContratos(this);
             viewContratos.setVisible(true);
        });
        
        btnGerenciarHidrometros.addActionListener(_ -> {
             ViewGerenciarHidrometros viewHidrometros = new ViewGerenciarHidrometros(this);
             viewHidrometros.setVisible(true);
        });
    }
}