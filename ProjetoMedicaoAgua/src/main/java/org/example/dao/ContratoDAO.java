package org.example.dao;

import org.example.control.ContratoInterface;
import org.example.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContratoDAO implements ContratoInterface {
    
    private ConexaoDB conexao = new ConexaoMySQL();
    private PessoaFisicaDAO pfDAO = new PessoaFisicaDAO();
    private PessoaJuridicaDAO pjDAO = new PessoaJuridicaDAO();
    private ImovelDAO imovelDAO = new ImovelDAO();
    private HidrometroDAO hidrometroDAO = new HidrometroDAO();

    @Override
    public void inserirContrato(Contrato contrato) throws SQLException {
        String sql = "INSERT INTO contrato (data_inicio, status, cod_usuario, cod_imovel, num_serie_hidrometro) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, new Timestamp(contrato.getData_inicio().getTime()));
            pstmt.setString(2, contrato.getStatus());
            pstmt.setInt(3, contrato.getUsuario().getCodUsuario());
            pstmt.setInt(4, contrato.getImovel().getCod_imovel());
            pstmt.setString(5, contrato.getHidrometro().getNum_serie_hidrometro());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                contrato.setCod_contrato(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao inserir contrato: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void atualizarContrato(Contrato contrato) throws SQLException {
        String sql = "UPDATE contrato SET data_inicio = ?, status = ?, cod_usuario = ?, cod_imovel = ?, num_serie_hidrometro = ? WHERE cod_contrato = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(contrato.getData_inicio().getTime()));
            pstmt.setString(2, contrato.getStatus());
            pstmt.setInt(3, contrato.getUsuario().getCodUsuario());
            pstmt.setInt(4, contrato.getImovel().getCod_imovel());
            pstmt.setString(5, contrato.getHidrometro().getNum_serie_hidrometro());
            pstmt.setInt(6, contrato.getCod_contrato());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao atualizar contrato: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void excluirContrato(int codContrato) throws SQLException {
        String sql = "DELETE FROM contrato WHERE cod_contrato = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codContrato);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao excluir contrato: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public Contrato buscarContratoPorId(int codContrato) throws SQLException {
        String sql = "SELECT * FROM contrato WHERE cod_contrato = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Contrato contrato = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codContrato);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                contrato = construirContrato(rs);
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar contrato: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return contrato;
    }

    @Override
    public List<Contrato> listarContratos() throws SQLException {
        String sql = "SELECT * FROM contrato";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Contrato> lista = new ArrayList<>();
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                lista.add(construirContrato(rs));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao listar contratos: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return lista;
    }
    
    private Contrato construirContrato(ResultSet rs) throws SQLException {
        int codUsuario = rs.getInt("cod_usuario");
        
        Usuario usuario = pfDAO.buscarPessoaFisicaPorId(codUsuario);
        if (usuario == null) {
            usuario = pjDAO.buscarPessoaJuridicaPorId(codUsuario);
        }
        
        Imovel imovel = imovelDAO.buscarImovelPorId(rs.getInt("cod_imovel"));
        Hidrometro hidrometro = hidrometroDAO.buscarHidrometroPorSerie(rs.getString("num_serie_hidrometro"));
        
        return new Contrato(
            rs.getInt("cod_contrato"),
            usuario,
            imovel,
            hidrometro,
            rs.getTimestamp("data_inicio"),
            rs.getString("status")
        );
    }
}