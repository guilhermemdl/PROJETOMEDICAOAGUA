package org.example.control;

import org.example.model.Leitura;
import java.sql.SQLException;
import java.util.List;

public interface LeituraInterface {
    void inserirLeitura(Leitura leitura) throws SQLException;
    void atualizarLeitura(Leitura leitura) throws SQLException;
    void excluirLeitura(int codLeitura) throws SQLException;
    Leitura buscarLeituraPorId(int codLeitura) throws SQLException;
    List<Leitura> listarLeituras() throws SQLException;
}
