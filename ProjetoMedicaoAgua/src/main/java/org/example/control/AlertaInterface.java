package org.example.control;

import org.example.model.Alerta;
import java.sql.SQLException;
import java.util.List;

public interface AlertaInterface {
    void inserirAlerta(Alerta alerta) throws SQLException;
    void atualizarAlerta(Alerta alerta) throws SQLException;
    void excluirAlerta(int codAlerta) throws SQLException;
    Alerta buscarAlertaPorId(int codAlerta) throws SQLException;
    List<Alerta> listarAlertas() throws SQLException;
}
