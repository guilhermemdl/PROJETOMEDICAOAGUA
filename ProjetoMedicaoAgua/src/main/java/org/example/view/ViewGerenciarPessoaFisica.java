package org.example.view;

import org.example.dao.PessoaFisicaDAO;
import org.example.model.PessoaFisica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ViewGerenciarPessoaFisica extends JDialog {

    private PessoaFisicaDAO pessoaFisicaDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewGerenciarPessoaFisica(Frame owner) {
        super(owner, "Gerenciar Pessoas Físicas", true); 
        this.pessoaFisicaDAO = new PessoaFisicaDAO();

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        String[] colunas = {"ID", "Nome", "Email", "CPF", "Data Nasc."};
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
        btnAdicionar.addActionListener(_ -> adicionarPessoaFisica());
        btnEditar.addActionListener(_ -> editarPessoaFisica());
        btnExcluir.addActionListener(_ -> excluirPessoaFisica());
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<PessoaFisica> lista = pessoaFisicaDAO.listarPessoasFisicas();
            for (PessoaFisica pf : lista) {
                tableModel.addRow(new Object[]{
                        pf.getCodUsuario(),
                        pf.getNome(),
                        pf.getEmail(),
                        pf.getCpf(),
                        pf.getDataNascimento()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarPessoaFisica() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome:");
            if (nome == null) return;
            String email = JOptionPane.showInputDialog(this, "Email:");
            if (email == null) return;
            String cpf = JOptionPane.showInputDialog(this, "CPF (só números):");
            if (cpf == null) return;
            String dataNascStr = JOptionPane.showInputDialog(this, "Data Nasc. (AAAA-MM-DD):");
            if (dataNascStr == null) return;

            LocalDate dataNasc = LocalDate.parse(dataNascStr);
            PessoaFisica novoCliente = new PessoaFisica(nome, email, cpf, dataNasc);

            pessoaFisicaDAO.inserirPessoaFisica(novoCliente);
            JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            carregarTabela();

        } catch (DateTimeParseException dtp) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use AAAA-MM-DD.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPessoaFisica() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para editar.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            PessoaFisica pf = pessoaFisicaDAO.buscarPessoaFisicaPorId(id);

            String nome = JOptionPane.showInputDialog(this, "Nome:", pf.getNome());
            if (nome == null) return;
            String email = JOptionPane.showInputDialog(this, "Email:", pf.getEmail());
            if (email == null) return;
            String cpf = JOptionPane.showInputDialog(this, "CPF:", pf.getCpf());
            if (cpf == null) return;
            String dataNascStr = JOptionPane.showInputDialog(this, "Data Nasc. (AAAA-MM-DD):", pf.getDataNascimento().toString());
            if (dataNascStr == null) return;

            pf.setNome(nome);
            pf.setEmail(email);
            pf.setCpf(cpf);
            pf.setDataNascimento(LocalDate.parse(dataNascStr));

            pessoaFisicaDAO.atualizarPessoaFisica(pf);
            JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPessoaFisica() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para excluir.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String nome = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir " + nome + " (ID: " + id + ")?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                pessoaFisicaDAO.excluirPessoaFisica(id);
                JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                carregarTabela();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}