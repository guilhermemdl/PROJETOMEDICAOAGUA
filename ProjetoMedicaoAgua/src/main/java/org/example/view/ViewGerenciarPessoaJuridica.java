package org.example.view;

import org.example.dao.PessoaJuridicaDAO;
import org.example.model.PessoaJuridica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewGerenciarPessoaJuridica extends JDialog {

    private PessoaJuridicaDAO pessoaJuridicaDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewGerenciarPessoaJuridica(Frame owner) {
        super(owner, "Gerenciar Pessoas Jurídicas", true);
        this.pessoaJuridicaDAO = new PessoaJuridicaDAO();

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        String[] colunas = {"ID", "Nome Fantasia", "Email", "CNPJ", "Razão Social"};
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
        btnAdicionar.addActionListener(_ -> adicionarPessoaJuridica());
        btnEditar.addActionListener(_ -> editarPessoaJuridica());
        btnExcluir.addActionListener(_ -> excluirPessoaJuridica());
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<PessoaJuridica> lista = pessoaJuridicaDAO.listarPessoasJuridicas();
            for (PessoaJuridica pj : lista) {
                tableModel.addRow(new Object[]{
                        pj.getCodUsuario(),
                        pj.getNome(),
                        pj.getEmail(),
                        pj.getCnpj(),
                        pj.getRazaoSocial()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarPessoaJuridica() {
        try {
            String nome = JOptionPane.showInputDialog(this, "Nome Fantasia:");
            if (nome == null) return;
            String email = JOptionPane.showInputDialog(this, "Email:");
            if (email == null) return;
            String cnpj = JOptionPane.showInputDialog(this, "CNPJ (só números):");
            if (cnpj == null) return;
            String razaoSocial = JOptionPane.showInputDialog(this, "Razão Social:");
            if (razaoSocial == null) return;

            PessoaJuridica novoCliente = new PessoaJuridica(nome, email, cnpj, razaoSocial);

            pessoaJuridicaDAO.inserirPessoaJuridica(novoCliente);
            JOptionPane.showMessageDialog(this, "Cliente (PJ) cadastrado com sucesso!");
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarPessoaJuridica() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente na tabela para editar.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            PessoaJuridica pj = pessoaJuridicaDAO.buscarPessoaJuridicaPorId(id);

            String nome = JOptionPane.showInputDialog(this, "Nome Fantasia:", pj.getNome());
            if (nome == null) return;
            String email = JOptionPane.showInputDialog(this, "Email:", pj.getEmail());
            if (email == null) return;
            String cnpj = JOptionPane.showInputDialog(this, "CNPJ:", pj.getCnpj());
            if (cnpj == null) return;
            String razaoSocial = JOptionPane.showInputDialog(this, "Razão Social:", pj.getRazaoSocial());
            if (razaoSocial == null) return;

            pj.setNome(nome);
            pj.setEmail(email);
            pj.setCnpj(cnpj);
            pj.setRazaoSocial(razaoSocial);

            pessoaJuridicaDAO.atualizarPessoaJuridica(pj);
            JOptionPane.showMessageDialog(this, "Cliente (PJ) atualizado com sucesso!");
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPessoaJuridica() {
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
                pessoaJuridicaDAO.excluirPessoaJuridica(id);
                JOptionPane.showMessageDialog(this, "Cliente (PJ) excluído com sucesso!");
                carregarTabela();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}