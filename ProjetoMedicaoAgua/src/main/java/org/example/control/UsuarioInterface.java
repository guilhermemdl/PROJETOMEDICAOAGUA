package org.example.control;
import org.example.model.Usuario;
import java.util.List;
import java.sql.SQLException;

public interface UsuarioInterface {
    void inserirUsuario(Usuario usuario) throws SQLException;
    void atualizarUsuario(Usuario usuario) throws SQLException;
    void excluirUsuario(int idUsuario) throws SQLException;
    Usuario buscarUsuarioPorId(int idUsuario) throws SQLException;
    List<Usuario> listarUsuarios() throws SQLException;
}

