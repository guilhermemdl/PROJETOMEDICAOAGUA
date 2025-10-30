package org.example.dao;

import org.example.control.LeituraInterface;
import org.example.model.Hidrometro;
import org.example.model.Leitura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LeituraDAO implements LeituraInterface {
    
    private ConexaoDB conexao = new ConexaoMySQL();
    private HidrometroDAO hidrometroDAO = new HidrometroDAO(); 

    @Override
    public void inserirLeitura(Leitura leitura) throws SQLException {
        String sql = "INSERT INTO leitura (valor_medido, data_hora_leitura, num_serie_hidrometro) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setDouble(1, leitura.getValor_medido());
            pstmt.setTimestamp(2, new Timestamp(leitura.getData_hora_leitura().getTime()));
            pstmt.setString(3, leitura.getHidrometro().getNum_serie_hidrometro());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                leitura.setCod_leitura(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao inserir leitura: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void atualizarLeitura(Leitura leitura) throws SQLException {
        String sql = "UPDATE leitura SET valor_medido = ?, data_hora_leitura = ?, num_serie_hidrometro = ? WHERE cod_leitura = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, leitura.getValor_medido());
            pstmt.setTimestamp(2, new Timestamp(leitura.getData_hora_leitura().getTime()));
            pstmt.setString(3, leitura.getHidrometro().getNum_serie_hidrometro());
            pstmt.setInt(4, leitura.getCod_leitura());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao atualizar leitura: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void excluirLeitura(int codLeitura) throws SQLException {
        String sql = "DELETE FROM leitura WHERE cod_leitura = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codLeitura);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao excluir leitura: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public Leitura buscarLeituraPorId(int codLeitura) throws SQLException {
        String sql = "SELECT * FROM leitura WHERE cod_leitura = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Leitura leitura = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codLeitura);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Hidrometro hidrometro = hidrometroDAO.buscarHidrometroPorSerie(rs.getString("num_serie_hidrometro"));
                leitura = new Leitura(
                    rs.getInt("cod_leitura"),
                    rs.getDouble("valor_medido"),
                    rs.getTimestamp("data_hora_leitura"),
                    hidrometro
                );
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar leitura: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return leitura;
    }

    @Override
    public List<Leitura> listarLeituras() throws SQLException {
        String sql = "SELECT * FROM leitura";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Leitura> lista = new ArrayList<>();
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Hidrometro hidrometro = hidrometroDAO.buscarHidrometroPorSerie(rs.getString("num_serie_hidrometro"));
                Leitura leitura = new Leitura(
                    rs.getInt("cod_leitura"),
                    rs.getDouble("valor_medido"),
                    rs.getTimestamp("data_hora_leitura"),
                    hidrometro
                );
                lista.add(leitura);
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao listar leituras: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return lista;
    }
}