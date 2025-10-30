package org.example.dao;

import org.example.control.ImovelInterface;
import org.example.model.Endereco;
import org.example.model.Imovel;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ImovelDAO implements ImovelInterface {

    private ConexaoDB conexao = new ConexaoMySQL();
    private EnderecoDAO enderecoDAO = new EnderecoDAO();

    @Override
    public void inserirImovel(Imovel imovel) throws SQLException {
        try {
            enderecoDAO.inserirEndereco(imovel.getEndereco());
        } catch (SQLException e) {
            if (!e.getMessage().contains("(PK) Existente")) {
                throw e;
            }
        }
        
        String sql = "INSERT INTO imovel (tipo_imovel, cep_endereco) VALUES (?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, imovel.getTipo_imovel());
            pstmt.setString(2, imovel.getEndereco().getCep());
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                imovel.setCod_imovel(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao inserir imóvel: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void atualizarImovel(Imovel imovel) throws SQLException {    
        enderecoDAO.atualizarEndereco(imovel.getEndereco());
        
        String sql = "UPDATE imovel SET tipo_imovel = ?, cep_endereco = ? WHERE cod_imovel = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, imovel.getTipo_imovel());
            pstmt.setString(2, imovel.getEndereco().getCep());
            pstmt.setInt(3, imovel.getCod_imovel());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao atualizar imóvel: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public void excluirImovel(int codImovel) throws SQLException {
        String sql = "DELETE FROM imovel WHERE cod_imovel = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codImovel);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao excluir imóvel: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    @Override
    public Imovel buscarImovelPorId(int codImovel) throws SQLException {
        String sql = "SELECT * FROM imovel WHERE cod_imovel = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Imovel imovel = null;
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, codImovel);
            rs = pstmt.executeQuery();
            if (rs.next()) {
               
                Endereco endereco = enderecoDAO.buscarEnderecoPorCep(rs.getString("cep_endereco"));
                imovel = new Imovel(
                    rs.getInt("cod_imovel"),
                    rs.getString("tipo_imovel"),
                    endereco
                );
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao buscar imóvel: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return imovel;
    }

    @Override
    public List<Imovel> listarImoveis() throws SQLException {
        String sql = "SELECT * FROM imovel";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Imovel> lista = new ArrayList<>();
        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Endereco endereco = enderecoDAO.buscarEnderecoPorCep(rs.getString("cep_endereco"));
                Imovel imovel = new Imovel(
                    rs.getInt("cod_imovel"),
                    rs.getString("tipo_imovel"),
                    endereco
                );
                lista.add(imovel);
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao listar imóveis: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
        return lista;
    }
}