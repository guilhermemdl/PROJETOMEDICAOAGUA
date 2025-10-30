package org.example.dao;

import org.example.control.PessoaFisicaInterface;
import org.example.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaFisicaDAO implements PessoaFisicaInterface {

    private ConexaoDB conexao;
    private UsuarioDAO usuarioDAO;

    public PessoaFisicaDAO() {
        this.conexao = new ConexaoMySQL();
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    public void inserirPessoaFisica(PessoaFisica pessoaFisica) throws SQLException {
        String sqlPf = "INSERT INTO pessoa_fisica (cod_usuario, cpf, data_nasc) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmtPf = null;

        try {
            conn = conexao.obterconexao();
            conn.setAutoCommit(false); 
            
            usuarioDAO.inserirUsuario(pessoaFisica, conn); 

            pstmtPf = conn.prepareStatement(sqlPf);
            pstmtPf.setInt(1, pessoaFisica.getCodUsuario());
            pstmtPf.setString(2, pessoaFisica.getCpf());
            pstmtPf.setDate(3, Date.valueOf(pessoaFisica.getDataNascimento()));
            pstmtPf.executeUpdate();

            conn.commit(); 

        } catch (Exception e) {
            if (conn != null) conn.rollback(); 
            throw new SQLException("Erro ao inserir pessoa física: " + e.getMessage());
        } finally {
            if (pstmtPf != null) pstmtPf.close();
            if (conn != null) conn.setAutoCommit(true);
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void atualizarPessoaFisica(PessoaFisica pessoaFisica) throws SQLException {
        usuarioDAO.atualizarUsuario(pessoaFisica);

        String sqlPf = "UPDATE pessoa_fisica SET cpf = ?, data_nasc = ? WHERE cod_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sqlPf);
            pstmt.setString(1, pessoaFisica.getCpf());
            pstmt.setDate(2, Date.valueOf(pessoaFisica.getDataNascimento()));
            pstmt.setInt(3, pessoaFisica.getCodUsuario());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao atualizar dados de pessoa física: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void excluirPessoaFisica(int codUsuario) throws SQLException {
        String sqlPf = "DELETE FROM pessoa_fisica WHERE cod_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmtPf = null;

        try {
            conn = conexao.obterconexao();
            conn.setAutoCommit(false); 

            pstmtPf = conn.prepareStatement(sqlPf);
            pstmtPf.setInt(1, codUsuario);
            pstmtPf.executeUpdate();
            
            usuarioDAO.excluirUsuario(codUsuario, conn);

            conn.commit(); 
        } catch (Exception e) {
            if (conn != null) conn.rollback(); 
            throw new SQLException("Erro ao excluir pessoa física: " + e.getMessage());
        } finally {
            if (pstmtPf != null) pstmtPf.close();
            if (conn != null) conn.setAutoCommit(true);
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public PessoaFisica buscarPessoaFisicaPorId(int codUsuario) throws SQLException {
        String sql = "SELECT * FROM usuario u JOIN pessoa_fisica pf ON u.cod_usuario = pf.cod_usuario WHERE u.cod_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PessoaFisica pf = null;

        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codUsuario);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pf = new PessoaFisica();
                pf.setCodUsuario(rs.getInt("cod_usuario"));
                pf.setNome(rs.getString("nome"));
                pf.setEmail(rs.getString("email"));
                pf.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                pf.setCpf(rs.getString("cpf"));
                pf.setDataNascimento(rs.getDate("data_nasc").toLocalDate());
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar pessoa física: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return pf;
    }

    @Override
    public List<PessoaFisica> listarPessoasFisicas() throws SQLException {
        String sql = "SELECT * FROM usuario u JOIN pessoa_fisica pf ON u.cod_usuario = pf.cod_usuario";
         Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PessoaFisica> lista = new ArrayList<>();

        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                PessoaFisica pf = new PessoaFisica();
                pf.setCodUsuario(rs.getInt("cod_usuario"));
                pf.setNome(rs.getString("nome"));
                pf.setEmail(rs.getString("email"));
                pf.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                pf.setCpf(rs.getString("cpf"));
                pf.setDataNascimento(rs.getDate("data_nasc").toLocalDate());
                lista.add(pf);
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao listar pessoas físicas: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return lista;
    }

    @Override
    public List<Alerta> buscarAlertasproCNPJ(String cpf) throws SQLException {
        String sql = "SELECT a.* FROM alerta a " +
                     "JOIN leitura l ON a.cod_leitura = l.cod_leitura " +
                     "JOIN hidrometro h ON l.num_serie_hidrometro = h.num_serie_hidrometro " +
                     "JOIN contrato c ON h.num_serie_hidrometro = c.num_serie_hidrometro " +
                     "JOIN usuario u ON c.cod_usuario = u.cod_usuario " +
                     "JOIN pessoa_fisica pf ON u.cod_usuario = pf.cod_usuario " +
                     "WHERE pf.cpf = ?";
        
        List<Alerta> alertas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cpf);
            rs = pstmt.executeQuery();
            
            AlertaDAO alertaDAO = new AlertaDAO(); 
            while (rs.next()) {
                alertas.add(alertaDAO.buscarAlertaPorId(rs.getInt("cod_alerta")));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar alertas por CPF: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return alertas;
    }

    @Override
    public List<Leitura> buscarLeiturasCNPJ(String cpf) throws SQLException {
        String sql = "SELECT l.* FROM leitura l " +
                     "JOIN hidrometro h ON l.num_serie_hidrometro = h.num_serie_hidrometro " +
                     "JOIN contrato c ON h.num_serie_hidrometro = c.num_serie_hidrometro " +
                     "JOIN usuario u ON c.cod_usuario = u.cod_usuario " +
                     "JOIN pessoa_fisica pf ON u.cod_usuario = pf.cod_usuario " +
                     "WHERE pf.cpf = ?";
        
        List<Leitura> leituras = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cpf);
            rs = pstmt.executeQuery();
            
            LeituraDAO leituraDAO = new LeituraDAO(); 
            while (rs.next()) {
                leituras.add(leituraDAO.buscarLeituraPorId(rs.getInt("cod_leitura")));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar leituras por CPF: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return leituras;
    }

    @Override
    public PessoaFisica buscarPessoaFisicaPorCPF(String cpf) throws SQLException {
        String sql = "SELECT * FROM usuario u JOIN pessoa_fisica pf ON u.cod_usuario = pf.cod_usuario WHERE pf.cpf = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PessoaFisica pf = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cpf);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pf = new PessoaFisica();
                pf.setCodUsuario(rs.getInt("cod_usuario"));
                pf.setNome(rs.getString("nome"));
                pf.setEmail(rs.getString("email"));
                pf.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                pf.setCpf(rs.getString("cpf"));
                pf.setDataNascimento(rs.getDate("data_nasc").toLocalDate());
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar pessoa física por CPF: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return pf;
    }
}