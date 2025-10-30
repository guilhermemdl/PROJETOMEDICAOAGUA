package org.example.dao;

import org.example.control.PessoaJuridicaInterface;
import org.example.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PessoaJuridicaDAO implements PessoaJuridicaInterface {

    private ConexaoDB conexao;
    private UsuarioDAO usuarioDAO;

    public PessoaJuridicaDAO() {
        this.conexao = new ConexaoMySQL();
        this.usuarioDAO = new UsuarioDAO();
    }

    @Override
    public void inserirPessoaJuridica(PessoaJuridica pessoaJuridica) throws SQLException {
        String sqlPj = "INSERT INTO pessoa_juridica (cod_usuario, cnpj, razao_social) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmtPj = null;

        try {
            conn = conexao.obterconexao();
            conn.setAutoCommit(false); 

            usuarioDAO.inserirUsuario(pessoaJuridica, conn); 

            pstmtPj = conn.prepareStatement(sqlPj);
            pstmtPj.setInt(1, pessoaJuridica.getCodUsuario());
            pstmtPj.setString(2, pessoaJuridica.getCnpj());
            pstmtPj.setString(3, pessoaJuridica.getRazaoSocial());
            pstmtPj.executeUpdate();

            conn.commit(); 

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new SQLException("Erro ao inserir pessoa jurídica: " + e.getMessage());
        } finally {
            if (pstmtPj != null) pstmtPj.close();
            if (conn != null) conn.setAutoCommit(true);
            conexao.fecharconexao(conn);
        }
    }
    
    @Override
    public void atualizarPessoaJuridica(PessoaJuridica pessoaJuridica) throws SQLException {
        usuarioDAO.atualizarUsuario(pessoaJuridica);

        String sqlPj = "UPDATE pessoa_juridica SET cnpj = ?, razao_social = ? WHERE cod_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sqlPj);
            pstmt.setString(1, pessoaJuridica.getCnpj());
            pstmt.setString(2, pessoaJuridica.getRazaoSocial());
            pstmt.setInt(3, pessoaJuridica.getCodUsuario());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao atualizar dados de pessoa jurídica: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void excluirPessoaJuridica(int codUsuario) throws SQLException {
        String sqlPj = "DELETE FROM pessoa_juridica WHERE cod_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmtPj = null;

        try {
            conn = conexao.obterconexao();
            conn.setAutoCommit(false); 

            pstmtPj = conn.prepareStatement(sqlPj);
            pstmtPj.setInt(1, codUsuario);
            pstmtPj.executeUpdate();
            
            usuarioDAO.excluirUsuario(codUsuario, conn);

            conn.commit(); 
        } catch (Exception e) {
            if (conn != null) conn.rollback(); 
            throw new SQLException("Erro ao excluir pessoa jurídica: " + e.getMessage());
        } finally {
            if (pstmtPj != null) pstmtPj.close();
            if (conn != null) conn.setAutoCommit(true);
            conexao.fecharconexao(conn);
        }
    }
    
    @Override
    public PessoaJuridica buscarPessoaJuridicaPorId(int codUsuario) throws SQLException {
        String sql = "SELECT * FROM usuario u JOIN pessoa_juridica pj ON u.cod_usuario = pj.cod_usuario WHERE u.cod_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PessoaJuridica pj = null;

        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codUsuario);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                pj = new PessoaJuridica();
                pj.setCodUsuario(rs.getInt("cod_usuario"));
                pj.setNome(rs.getString("nome"));
                pj.setEmail(rs.getString("email"));
                pj.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                pj.setCnpj(rs.getString("cnpj"));
                pj.setRazaoSocial(rs.getString("razao_social"));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar pessoa jurídica: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return pj;
    }
    
    @Override
    public List<PessoaJuridica> listarPessoasJuridicas() throws SQLException {
        String sql = "SELECT * FROM usuario u JOIN pessoa_juridica pj ON u.cod_usuario = pj.cod_usuario";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PessoaJuridica> lista = new ArrayList<>();

        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                PessoaJuridica pj = new PessoaJuridica();
                pj.setCodUsuario(rs.getInt("cod_usuario"));
                pj.setNome(rs.getString("nome"));
                pj.setEmail(rs.getString("email"));
                pj.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                pj.setCnpj(rs.getString("cnpj"));
                pj.setRazaoSocial(rs.getString("razao_social"));
                lista.add(pj);
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao listar pessoas jurídicas: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return lista;
    }

    @Override
    public List<Alerta> buscarAlertasproCNPJ(String cnpj) throws SQLException {
        String sql = "SELECT a.* FROM alerta a " +
                     "JOIN leitura l ON a.cod_leitura = l.cod_leitura " +
                     "JOIN hidrometro h ON l.num_serie_hidrometro = h.num_serie_hidrometro " +
                     "JOIN contrato c ON h.num_serie_hidrometro = c.num_serie_hidrometro " +
                     "JOIN usuario u ON c.cod_usuario = u.cod_usuario " +
                     "JOIN pessoa_juridica pj ON u.cod_usuario = pj.cod_usuario " +
                     "WHERE pj.cnpj = ?";
        
        List<Alerta> alertas = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cnpj);
            rs = pstmt.executeQuery();
            
            AlertaDAO alertaDAO = new AlertaDAO(); 
            while (rs.next()) {
                alertas.add(alertaDAO.buscarAlertaPorId(rs.getInt("cod_alerta")));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar alertas por CNPJ: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return alertas;
    }

    @Override
    public List<Leitura> buscarLeiturasCNPJ(String cnpj) throws SQLException {
        String sql = "SELECT l.* FROM leitura l " +
                     "JOIN hidrometro h ON l.num_serie_hidrometro = h.num_serie_hidrometro " +
                     "JOIN contrato c ON h.num_serie_hidrometro = c.num_serie_hidrometro " +
                     "JOIN usuario u ON c.cod_usuario = u.cod_usuario " +
                     "JOIN pessoa_juridica pj ON u.cod_usuario = pj.cod_usuario " +
                     "WHERE pj.cnpj = ?";
        
        List<Leitura> leituras = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cnpj);
            rs = pstmt.executeQuery();
            
            LeituraDAO leituraDAO = new LeituraDAO(); 
            while (rs.next()) {
                leituras.add(leituraDAO.buscarLeituraPorId(rs.getInt("cod_leitura")));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar leituras por CNPJ: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return leituras;
    }

    @Override
    public PessoaJuridica buscarPessoaJuridicaPorCNPJ(String cnpj) throws SQLException {
        String sql = "SELECT * FROM usuario u JOIN pessoa_juridica pj ON u.cod_usuario = pj.cod_usuario WHERE pj.cnpj = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        PessoaJuridica pj = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cnpj);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                pj = new PessoaJuridica();
                pj.setCodUsuario(rs.getInt("cod_usuario"));
                pj.setNome(rs.getString("nome"));
                pj.setEmail(rs.getString("email"));
                pj.setDataCadastro(rs.getTimestamp("data_cadastro").toLocalDateTime());
                pj.setCnpj(rs.getString("cnpj"));
                pj.setRazaoSocial(rs.getString("razao_social"));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar pessoa jurídica por CNPJ: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return pj;
    }
}