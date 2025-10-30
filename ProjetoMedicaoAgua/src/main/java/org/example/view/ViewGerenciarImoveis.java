package org.example.view;

import org.example.dao.ImovelDAO;
import org.example.model.Endereco;
import org.example.model.Imovel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ViewGerenciarImoveis extends JDialog {

    private ImovelDAO imovelDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewGerenciarImoveis(Frame owner) {
        super(owner, "Gerenciar Imóveis", true);
        this.imovelDAO = new ImovelDAO();

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        String[] colunas = {"ID", "Tipo", "CEP", "Rua", "Bairro", "Cidade"};
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
        btnAdicionar.addActionListener(_ -> adicionarImovel());
        btnEditar.addActionListener(_ -> editarImovel());
        btnExcluir.addActionListener(_ -> excluirImovel());
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Imovel> lista = imovelDAO.listarImoveis();
            for (Imovel imovel : lista) {
                tableModel.addRow(new Object[]{
                        imovel.getCod_imovel(),
                        imovel.getTipo_imovel(),
                        imovel.getEndereco().getCep(),
                        imovel.getEndereco().getRua(),
                        imovel.getEndereco().getBairro(),
                        imovel.getEndereco().getCidade()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarImovel() {
        try {
            String cep = JOptionPane.showInputDialog(this, "CEP (só números):");
            if (cep == null) return;
            String rua = JOptionPane.showInputDialog(this, "Rua:");
            if (rua == null) return;
            String bairro = JOptionPane.showInputDialog(this, "Bairro:");
            if (bairro == null) return;
            String cidade = JOptionPane.showInputDialog(this, "Cidade:");
            if (cidade == null) return;
            String estado = JOptionPane.showInputDialog(this, "Estado (Ex: SP):");
            if (estado == null) return;

            String tipoImovel = JOptionPane.showInputDialog(this, "Tipo do Imóvel (Ex: Residencial):");
            if (tipoImovel == null) return;

            Endereco endereco = new Endereco(cep, rua, bairro, cidade, estado);
            Imovel imovel = new Imovel(0, tipoImovel, endereco);

            imovelDAO.inserirImovel(imovel);
            JOptionPane.showMessageDialog(this, "Imóvel cadastrado com sucesso!");
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar imóvel: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarImovel() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um imóvel na tabela para editar.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Imovel imovel = imovelDAO.buscarImovelPorId(id);
            
            String cep = JOptionPane.showInputDialog(this, "CEP:", imovel.getEndereco().getCep());
            if (cep == null) return;
            String rua = JOptionPane.showInputDialog(this, "Rua:", imovel.getEndereco().getRua());
            if (rua == null) return;
            String bairro = JOptionPane.showInputDialog(this, "Bairro:", imovel.getEndereco().getBairro());
            if (bairro == null) return;
            String cidade = JOptionPane.showInputDialog(this, "Cidade:", imovel.getEndereco().getCidade());
            if (cidade == null) return;
            String estado = JOptionPane.showInputDialog(this, "Estado:", imovel.getEndereco().getEstado());
            if (estado == null) return;
            
            String tipoImovel = JOptionPane.showInputDialog(this, "Tipo do Imóvel:", imovel.getTipo_imovel());
            if (tipoImovel == null) return;

            imovel.getEndereco().setCep(cep);
            imovel.getEndereco().setRua(rua);
            imovel.getEndereco().setBairro(bairro);
            imovel.getEndereco().setCidade(cidade);
            imovel.getEndereco().setEstado(estado);
            imovel.setTipo_imovel(tipoImovel);
            
            imovelDAO.atualizarImovel(imovel);
            JOptionPane.showMessageDialog(this, "Imóvel atualizado com sucesso!");
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar imóvel: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirImovel() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um imóvel na tabela para excluir.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String cep = (String) tableModel.getValueAt(selectedRow, 2);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o imóvel ID " + id + " (CEP: " + cep + ")?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                imovelDAO.excluirImovel(id);
                JOptionPane.showMessageDialog(this, "Imóvel excluído com sucesso!");
                carregarTabela();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir. Verifique se o imóvel está sendo usado em um contrato.", "Erro de Exclusão", JOptionPane.ERROR_MESSAGE);
        }
    }
}