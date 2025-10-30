package org.example.dao;

import org.example.control.EnderecoInterface;
import org.example.model.Endereco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnderecoDAO implements EnderecoInterface {
    
    private ConexaoDB conexao = new ConexaoMySQL();

    @Override
    public void inserirEndereco(Endereco endereco) throws SQLException {
        String sql = "INSERT INTO endereco (cep, rua, bairro, cidade, estado) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, endereco.getCep());
            pstmt.setString(2, endereco.getRua());
            pstmt.setString(3, endereco.getBairro());
            pstmt.setString(4, endereco.getCidade());
            pstmt.setString(5, endereco.getEstado());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao inserir endereço: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void atualizarEndereco(Endereco endereco) throws SQLException {
        String sql = "UPDATE endereco SET rua = ?, bairro = ?, cidade = ?, estado = ? WHERE cep = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, endereco.getRua());
            pstmt.setString(2, endereco.getBairro());
            pstmt.setString(3, endereco.getCidade());
            pstmt.setString(4, endereco.getEstado());
            pstmt.setString(5, endereco.getCep());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao atualizar endereço: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void excluirEndereco(String cep) throws SQLException {
        String sql = "DELETE FROM endereco WHERE cep = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cep);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao excluir endereço: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public Endereco buscarEnderecoPorCep(String cep) throws SQLException {
        String sql = "SELECT * FROM endereco WHERE cep = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Endereco endereco = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, cep);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                endereco = new Endereco(
                    rs.getString("cep"),
                    rs.getString("rua"),
                    rs.getString("bairro"),
                    rs.getString("cidade"),
                    rs.getString("estado")
                );
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar endereço: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return endereco;
    }

    @Override
    public List<Endereco> listarEnderecos() throws SQLException {
        String sql = "SELECT * FROM endereco";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Endereco> lista = new ArrayList<>();
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Endereco endereco = new Endereco(
                    rs.getString("cep"),
                    rs.getString("rua"),
                    rs.getString("bairro"),
                    rs.getString("cidade"),
                    rs.getString("estado")
                );
                lista.add(endereco);
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao listar endereços: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return lista;
    }
}