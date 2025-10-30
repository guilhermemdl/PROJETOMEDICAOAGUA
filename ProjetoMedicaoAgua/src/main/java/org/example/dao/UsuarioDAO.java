package org.example.dao;

import org.example.control.UsuarioInterface;
import org.example.model.Usuario;
import java.sql.*;
import java.util.List;

public class UsuarioDAO implements UsuarioInterface {

    private ConexaoDB conexao = new ConexaoMySQL();

    public void inserirUsuario(Usuario usuario, Connection conn) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, data_cadastro, tipo_p) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setTimestamp(3, Timestamp.valueOf(usuario.getDataCadastro()));
            pstmt.setString(4, usuario.getTipoP());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setCodUsuario(rs.getInt(1));
            }
        } catch (Exception e) {
            throw new SQLException("Erro ao inserir usuário base: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    @Override
    public void inserirUsuario(Usuario usuario) throws SQLException {
         Connection conn = null;
         try {
            conn = conexao.obterconexao();
            inserirUsuario(usuario, conn);
         } catch (Exception e) {
             throw new SQLException(e.getMessage());
         } finally {
             conexao.fecharconexao(conn);
         }
    }

    @Override
    public void atualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuario SET nome = ?, email = ? WHERE cod_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = conexao.obterconexao();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setInt(3, usuario.getCodUsuario());
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao atualizar usuário: " + e.getMessage());
        } finally {
            if (pstmt != null) pstmt.close();
            conexao.fecharconexao(conn);
        }
    }

    public void excluirUsuario(int idUsuario, Connection conn) throws SQLException {
        String sqlUsuario = "DELETE FROM usuario WHERE cod_usuario = ?";
        PreparedStatement pstmtUsuario = null;
        try {
            pstmtUsuario = conn.prepareStatement(sqlUsuario);
            pstmtUsuario.setInt(1, idUsuario);
            pstmtUsuario.executeUpdate();
        } catch (Exception e) {
            throw new SQLException("Erro ao excluir usuário base: " + e.getMessage());
        } finally {
            if (pstmtUsuario != null) pstmtUsuario.close();
        }
    }
    
    @Override
    public void excluirUsuario(int idUsuario) throws SQLException {
         throw new UnsupportedOperationException("Use PessoaFisicaDAO ou PessoaJuridicaDAO para excluir.");
    }

    @Override
    public Usuario buscarUsuarioPorId(int idUsuario) throws SQLException {
        throw new UnsupportedOperationException("Use PessoaFisicaDAO ou PessoaJuridicaDAO para buscar.");
    }

    @Override
    public List<Usuario> listarUsuarios() throws SQLException {
        throw new UnsupportedOperationException("Use ListarPessoasFisicas ou ListarPessoasJuridicas.");
    }
}