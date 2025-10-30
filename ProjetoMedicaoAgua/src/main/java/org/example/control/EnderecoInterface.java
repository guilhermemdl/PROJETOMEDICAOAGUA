package org.example.control;

import org.example.model.Endereco;
import java.sql.SQLException;
import java.util.List;

public interface EnderecoInterface {
    void inserirEndereco(Endereco endereco) throws SQLException;
    void atualizarEndereco(Endereco endereco) throws SQLException;
    void excluirEndereco(String cep) throws SQLException;
    Endereco buscarEnderecoPorCep(String cep) throws SQLException;
    List<Endereco> listarEnderecos() throws SQLException;
}
