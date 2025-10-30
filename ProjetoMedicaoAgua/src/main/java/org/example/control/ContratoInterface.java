package org.example.control;

import org.example.model.Contrato;
import java.sql.SQLException;
import java.util.List;

public interface ContratoInterface {
    void inserirContrato(Contrato contrato) throws SQLException;
    void atualizarContrato(Contrato contrato) throws SQLException;
    void excluirContrato(int codContrato) throws SQLException;
    Contrato buscarContratoPorId(int codContrato) throws SQLException;
    List<Contrato> listarContratos() throws SQLException;
}
