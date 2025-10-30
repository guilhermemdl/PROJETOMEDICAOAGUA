package org.example.view;

import org.example.dao.HidrometroDAO;
import org.example.model.Hidrometro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewGerenciarHidrometros extends JDialog {

    private HidrometroDAO hidrometroDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewGerenciarHidrometros(Frame owner) {
        super(owner, "Gerenciar Hidrômetros", true);
        this.hidrometroDAO = new HidrometroDAO();

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        String[] colunas = {"Nº Série", "Marca", "Modelo"};
        tableModel = new DefaultTableModel(colunas, 0);
        table = new JTable(tableModel);
        container.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar Lista");

        panelBotoes.add(btnAdicionar);
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnExcluir);
        panelBotoes.add(btnAtualizar);
        container.add(panelBotoes, BorderLayout.SOUTH);

        carregarTabela();

        btnAtualizar.addActionListener(_ -> carregarTabela());
        btnAdicionar.addActionListener(_ -> adicionarHidrometro());
        btnEditar.addActionListener(_ -> editarHidrometro());
        btnExcluir.addActionListener(_ -> excluirHidrometro());
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Hidrometro> lista = hidrometroDAO.listarHidrometros();
            for (Hidrometro h : lista) {
                tableModel.addRow(new Object[]{
                        h.getNum_serie_hidrometro(),
                        h.getMarca(),
                        h.getModelo()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarHidrometro() {
        try {
            String numSerie = JOptionPane.showInputDialog(this, "Nº de Série:");
            if (numSerie == null) return;
            String marca = JOptionPane.showInputDialog(this, "Marca:");
            if (marca == null) return;
            String modelo = JOptionPane.showInputDialog(this, "Modelo:");
            if (modelo == null) return;

            Hidrometro hidrometro = new Hidrometro(numSerie, marca, modelo);

            hidrometroDAO.inserirHidrometro(hidrometro);
            JOptionPane.showMessageDialog(this, "Hidrômetro cadastrado com sucesso!");
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar hidrômetro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarHidrometro() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um hidrômetro na tabela para editar.");
            return;
        }

        try {
            String numSerieOriginal = (String) tableModel.getValueAt(selectedRow, 0);
            Hidrometro hidrometro = hidrometroDAO.buscarHidrometroPorSerie(numSerieOriginal);
            
            String marca = JOptionPane.showInputDialog(this, "Marca:", hidrometro.getMarca());
            if (marca == null) return;
            String modelo = JOptionPane.showInputDialog(this, "Modelo:", hidrometro.getModelo());
            if (modelo == null) return;

            hidrometro.setMarca(marca);
            hidrometro.setModelo(modelo);
            
            hidrometroDAO.atualizarHidrometro(hidrometro);
            JOptionPane.showMessageDialog(this, "Hidrômetro atualizado com sucesso!");
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar hidrômetro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirHidrometro() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um hidrômetro na tabela para excluir.");
            return;
        }

        try {
            String numSerie = (String) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o hidrômetro Nº " + numSerie + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                hidrometroDAO.excluirHidrometro(numSerie);
                JOptionPane.showMessageDialog(this, "Hidrômetro excluído com sucesso!");
                carregarTabela();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir. Verifique se o hidrômetro está sendo usado em um contrato.", "Erro de Exclusão", JOptionPane.ERROR_MESSAGE);
        }
    }
}