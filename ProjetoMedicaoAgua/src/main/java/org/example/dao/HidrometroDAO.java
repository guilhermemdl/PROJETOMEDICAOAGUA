package org.example.dao;

import org.example.control.HidrometroInterface;
import org.example.model.Hidrometro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HidrometroDAO implements HidrometroInterface {

    private ConexaoDB conexao = new ConexaoMySQL();

    @Override
    public void inserirHidrometro(Hidrometro hidrometro) throws SQLException {
        String sql = "INSERT INTO hidrometro (num_serie_hidrometro, marca, modelo) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hidrometro.getNum_serie_hidrometro()); 
            pstmt.setString(2, hidrometro.getMarca());
            pstmt.setString(3, hidrometro.getModelo());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao inserir hidrômetro: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void atualizarHidrometro(Hidrometro hidrometro) throws SQLException {
        String sql = "UPDATE hidrometro SET marca = ?, modelo = ? WHERE num_serie_hidrometro = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, hidrometro.getMarca());
            pstmt.setString(2, hidrometro.getModelo());
            pstmt.setString(3, hidrometro.getNum_serie_hidrometro());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao atualizar hidrômetro: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void excluirHidrometro(String numSerie) throws SQLException {
        String sql = "DELETE FROM hidrometro WHERE num_serie_hidrometro = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numSerie);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao excluir hidrômetro: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public Hidrometro buscarHidrometroPorSerie(String numSerie) throws SQLException {
        String sql = "SELECT * FROM hidrometro WHERE num_serie_hidrometro = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Hidrometro hidrometro = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, numSerie);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                hidrometro = new Hidrometro(
                    rs.getString("num_serie_hidrometro"),
                    rs.getString("marca"),
                    rs.getString("modelo")
                );
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar hidrômetro: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return hidrometro;
    }

    @Override
    public List<Hidrometro> listarHidrometros() throws SQLException {
        String sql = "SELECT * FROM hidrometro";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Hidrometro> lista = new ArrayList<>();
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                 Hidrometro hidrometro = new Hidrometro(
                    rs.getString("num_serie_hidrometro"),
                    rs.getString("marca"),
                    rs.getString("modelo")
                );
                lista.add(hidrometro);
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao listar hidrômetros: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return lista;
    }
}