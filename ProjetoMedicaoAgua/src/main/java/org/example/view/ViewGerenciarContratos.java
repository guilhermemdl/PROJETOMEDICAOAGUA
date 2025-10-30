package org.example.view;

import org.example.dao.*;
import org.example.model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ViewGerenciarContratos extends JDialog {

    private ContratoDAO contratoDAO;
    private PessoaFisicaDAO pfDAO;
    private PessoaJuridicaDAO pjDAO;
    private ImovelDAO imovelDAO;
    private HidrometroDAO hidrometroDAO;
    
    private JTable table;
    private DefaultTableModel tableModel;

    public ViewGerenciarContratos(Frame owner) {
        super(owner, "Gerenciar Contratos", true);
        this.contratoDAO = new ContratoDAO();
        this.pfDAO = new PessoaFisicaDAO();
        this.pjDAO = new PessoaJuridicaDAO();
        this.imovelDAO = new ImovelDAO();
        this.hidrometroDAO = new HidrometroDAO();

        setSize(800, 600);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());

        String[] colunas = {"ID Contrato", "Cliente (ID)", "Cliente (Nome)", "Imóvel (ID)", "Hidrômetro (Série)", "Data Início", "Status"};
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
        btnAdicionar.addActionListener(_ -> adicionarContrato());
        btnEditar.addActionListener(_ -> editarContrato());
        btnExcluir.addActionListener(_ -> excluirContrato());
    }

    private void carregarTabela() {
        tableModel.setRowCount(0);
        try {
            List<Contrato> lista = contratoDAO.listarContratos();
            for (Contrato c : lista) {
                tableModel.addRow(new Object[]{
                        c.getCod_contrato(),
                        c.getUsuario().getCodUsuario(),
                        c.getUsuario().getNome(),
                        c.getImovel().getCod_imovel(),
                        c.getHidrometro().getNum_serie_hidrometro(),
                        c.getData_inicio(),
                        c.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar lista: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adicionarContrato() {
        try {
            int codUsuario = Integer.parseInt(JOptionPane.showInputDialog(this, "ID do Cliente (Usuário):"));
            int codImovel = Integer.parseInt(JOptionPane.showInputDialog(this, "ID do Imóvel:"));
            String numSerie = JOptionPane.showInputDialog(this, "Nº de Série do Hidrômetro:");
            String dataStr = JOptionPane.showInputDialog(this, "Data Início (AAAA-MM-DD):");
            String status = JOptionPane.showInputDialog(this, "Status (ATIVO/INATIVO):");
            
            Usuario usuario = pfDAO.buscarPessoaFisicaPorId(codUsuario); 
            if (usuario == null) {
                usuario = pjDAO.buscarPessoaJuridicaPorId(codUsuario); 
            }
            if (usuario == null) throw new Exception("Usuário (Cliente) não encontrado com ID: " + codUsuario);

            Imovel imovel = imovelDAO.buscarImovelPorId(codImovel);
            if (imovel == null) throw new Exception("Imóvel não encontrado com ID: " + codImovel);
            
            Hidrometro hidrometro = hidrometroDAO.buscarHidrometroPorSerie(numSerie);
            if (hidrometro == null) throw new Exception("Hidrômetro não encontrado com Série: " + numSerie);
            
            Date dataInicio = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);

            Contrato contrato = new Contrato(0, usuario, imovel, hidrometro, dataInicio, status);
            
            contratoDAO.inserirContrato(contrato);
            JOptionPane.showMessageDialog(this, "Contrato cadastrado com sucesso!");
            carregarTabela();

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "ID deve ser um número.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar contrato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarContrato() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um contrato na tabela para editar.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Contrato contrato = contratoDAO.buscarContratoPorId(id);
            
            int codUsuario = Integer.parseInt(JOptionPane.showInputDialog(this, "ID do Cliente (Usuário):", contrato.getUsuario().getCodUsuario()));
            int codImovel = Integer.parseInt(JOptionPane.showInputDialog(this, "ID do Imóvel:", contrato.getImovel().getCod_imovel()));
            String numSerie = JOptionPane.showInputDialog(this, "Nº de Série do Hidrômetro:", contrato.getHidrometro().getNum_serie_hidrometro());
            String dataStr = JOptionPane.showInputDialog(this, "Data Início (AAAA-MM-DD):", new SimpleDateFormat("yyyy-MM-dd").format(contrato.getData_inicio()));
            String status = JOptionPane.showInputDialog(this, "Status (ATIVO/INATIVO):", contrato.getStatus());

            Usuario usuario = pfDAO.buscarPessoaFisicaPorId(codUsuario);
            if (usuario == null) usuario = pjDAO.buscarPessoaJuridicaPorId(codUsuario);
            if (usuario == null) throw new Exception("Usuário (Cliente) não encontrado com ID: " + codUsuario);

            Imovel imovel = imovelDAO.buscarImovelPorId(codImovel);
            if (imovel == null) throw new Exception("Imóvel não encontrado com ID: " + codImovel);
            
            Hidrometro hidrometro = hidrometroDAO.buscarHidrometroPorSerie(numSerie);
            if (hidrometro == null) throw new Exception("Hidrômetro não encontrado com Série: " + numSerie);
            
            Date dataInicio = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
            
            // Atualiza o objeto
            contrato.setUsuario(usuario);
            contrato.setImovel(imovel);
            contrato.setHidrometro(hidrometro);
            contrato.setData_inicio(dataInicio);
            contrato.setStatus(status);

            contratoDAO.atualizarContrato(contrato);
            JOptionPane.showMessageDialog(this, "Contrato atualizado com sucesso!");
            carregarTabela();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao editar contrato: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirContrato() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um contrato na tabela para excluir.");
            return;
        }

        try {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Tem certeza que deseja excluir o contrato ID " + id + "?",
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                contratoDAO.excluirContrato(id);
                JOptionPane.showMessageDialog(this, "Contrato excluído com sucesso!");
                carregarTabela();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}