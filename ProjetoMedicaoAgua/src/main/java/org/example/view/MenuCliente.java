package org.example.view;

import org.example.dao.PessoaFisicaDAO;
import org.example.dao.PessoaJuridicaDAO;
import org.example.model.Alerta;
import org.example.model.Leitura;
import org.example.model.PessoaFisica;
import org.example.model.PessoaJuridica;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuCliente extends JFrame {

    private PessoaFisicaDAO pessoaFisicaDAO;
    private PessoaJuridicaDAO pessoaJuridicaDAO;

    public MenuCliente() {
        this.pessoaFisicaDAO = new PessoaFisicaDAO();
        this.pessoaJuridicaDAO = new PessoaJuridicaDAO();
        
        setTitle("Menu do Cliente");
        setSize(500, 400);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton btnVerLeituras = new JButton("Ver Minhas Leituras");
        JButton btnVerAlertas = new JButton("Ver Meus Alertas");
        JButton btnMeusDados = new JButton("Meus Dados Cadastrais");

        panel.add(btnVerLeituras);
        panel.add(btnVerAlertas);
        panel.add(btnMeusDados);

        add(panel);

        btnVerLeituras.addActionListener(_ -> {
            String id = JOptionPane.showInputDialog(this, "Digite seu CPF ou CNPJ:");
            if (id == null || id.trim().isEmpty()) {
                return;
            }
            
            try {
                List<Leitura> leituras;
                if (id.length() <= 11) {
                    leituras = pessoaFisicaDAO.buscarLeiturasCNPJ(id);
                } else { 
                    leituras = pessoaJuridicaDAO.buscarLeiturasCNPJ(id);
                }
                
                if (leituras.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nenhuma leitura encontrada para este documento.");
                    return;
                }

                JTextArea textArea = new JTextArea(15, 40);
                textArea.setEditable(false);
                StringBuilder sb = new StringBuilder("--- Suas Leituras ---\n");
                for (Leitura l : leituras) {
                    sb.append("Data: ").append(l.getData_hora_leitura());
                    sb.append(" | Valor: ").append(l.getValor_medido());
                    sb.append(" | Hidrômetro: ").append(l.getHidrometro().getNum_serie_hidrometro());
                    sb.append("\n");
                }
                textArea.setText(sb.toString());
                JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Minhas Leituras", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar leituras: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnVerAlertas.addActionListener(_ -> {
            String id = JOptionPane.showInputDialog(this, "Digite seu CPF ou CNPJ:");
            if (id == null || id.trim().isEmpty()) {
                return;
            }

            try {
                List<Alerta> alertas;
                if (id.length() <= 11) {
                    alertas = pessoaFisicaDAO.buscarAlertasproCNPJ(id);
                } else {
                    alertas = pessoaJuridicaDAO.buscarAlertasproCNPJ(id);
                }

                if (alertas.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Nenhum alerta encontrado para este documento.");
                    return;
                }

                JTextArea textArea = new JTextArea(15, 40);
                textArea.setEditable(false);
                StringBuilder sb = new StringBuilder("--- Seus Alertas ---\n");
                for (Alerta a : alertas) {
                    sb.append("Data: ").append(a.getData_hora_alerta());
                    sb.append(" | Tipo: ").append(a.getTipo_alerta());
                    sb.append(" | Leitura (ID): ").append(a.getLeitura().getCod_leitura());
                    sb.append("\n");
                }
                textArea.setText(sb.toString());
                JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Meus Alertas", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar alertas: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnMeusDados.addActionListener(_ -> {
            String id = JOptionPane.showInputDialog(this, "Digite seu CPF ou CNPJ para ver seus dados:");
            if (id == null || id.trim().isEmpty()) {
                return;
            }
            
            try {
                JTextArea textArea = new JTextArea(15, 40);
                textArea.setEditable(false);
                StringBuilder sb = new StringBuilder("--- Seus Dados Cadastrais ---\n\n");

                if (id.length() <= 11) {
                    PessoaFisica pf = pessoaFisicaDAO.buscarPessoaFisicaPorCPF(id);
                    if (pf == null) {
                        JOptionPane.showMessageDialog(this, "Nenhum cliente (PF) encontrado com este CPF.");
                        return;
                    }
                    sb.append("Tipo: Pessoa Física\n");
                    sb.append("ID Cliente: ").append(pf.getCodUsuario()).append("\n");
                    sb.append("Nome: ").append(pf.getNome()).append("\n");
                    sb.append("Email: ").append(pf.getEmail()).append("\n");
                    sb.append("CPF: ").append(pf.getCpf()).append("\n");
                    sb.append("Data Nasc.: ").append(pf.getDataNascimento()).append("\n");
                    sb.append("Data Cadastro: ").append(pf.getDataCadastro().toLocalDate()).append("\n");
                    
                } else {
                    PessoaJuridica pj = pessoaJuridicaDAO.buscarPessoaJuridicaPorCNPJ(id);
                    if (pj == null) {
                        JOptionPane.showMessageDialog(this, "Nenhum cliente (PJ) encontrado com este CNPJ.");
                        return;
                    }
                    sb.append("Tipo: Pessoa Jurídica\n");
                    sb.append("ID Cliente: ").append(pj.getCodUsuario()).append("\n");
                    sb.append("Nome Fantasia: ").append(pj.getNome()).append("\n");
                    sb.append("Email: ").append(pj.getEmail()).append("\n");
                    sb.append("Razão Social: ").append(pj.getRazaoSocial()).append("\n");
                    sb.append("CNPJ: ").append(pj.getCnpj()).append("\n");
                    sb.append("Data Cadastro: ").append(pj.getDataCadastro().toLocalDate()).append("\n");
                }
                
                textArea.setText(sb.toString());
                JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Meus Dados", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao buscar dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}