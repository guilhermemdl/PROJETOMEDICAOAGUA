package org.example.dao;

import org.example.control.AlertaInterface;
import org.example.model.Alerta;
import org.example.model.Leitura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO implements AlertaInterface {

    private ConexaoDB conexao = new ConexaoMySQL();
    private LeituraDAO leituraDAO = new LeituraDAO();

    @Override
    public void inserirAlerta(Alerta alerta) throws SQLException {
        String sql = "INSERT INTO alerta (data_hora_alerta, tipo_alerta, cod_leitura) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, new Timestamp(alerta.getData_hora_alerta().getTime()));
            pstmt.setString(2, alerta.getTipo_alerta());
            pstmt.setInt(3, alerta.getLeitura().getCod_leitura());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                alerta.setCod_alerta(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao inserir alerta: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void atualizarAlerta(Alerta alerta) throws SQLException {
        String sql = "UPDATE alerta SET data_hora_alerta = ?, tipo_alerta = ?, cod_leitura = ? WHERE cod_alerta = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(alerta.getData_hora_alerta().getTime()));
            pstmt.setString(2, alerta.getTipo_alerta());
            pstmt.setInt(3, alerta.getLeitura().getCod_leitura());
            pstmt.setInt(4, alerta.getCod_alerta());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao atualizar alerta: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void excluirAlerta(int codAlerta) throws SQLException {
        String sql = "DELETE FROM alerta WHERE cod_alerta = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codAlerta);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao excluir alerta: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public Alerta buscarAlertaPorId(int codAlerta) throws SQLException {
        String sql = "SELECT * FROM alerta WHERE cod_alerta = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Alerta alerta = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codAlerta);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Leitura leitura = leituraDAO.buscarLeituraPorId(rs.getInt("cod_leitura"));
                alerta = new Alerta(
                    rs.getInt("cod_alerta"),
                    rs.getTimestamp("data_hora_alerta"),
                    rs.getString("tipo_alerta"),
                    leitura
                );
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar alerta: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return alerta;
    }

    @Override
    public List<Alerta> listarAlertas() throws SQLException {
        String sql = "SELECT * FROM alerta";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Alerta> lista = new ArrayList<>();
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Leitura leitura = leituraDAO.buscarLeituraPorId(rs.getInt("cod_leitura"));
                Alerta alerta = new Alerta(
                    rs.getInt("cod_alerta"),
                    rs.getTimestamp("data_hora_alerta"),
                    rs.getString("tipo_alerta"),
                    leitura
                );
                lista.add(alerta);
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao listar alertas: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return lista;
    }
}