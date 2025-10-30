package org.example.control;

import org.example.model.Hidrometro;
import java.sql.SQLException;
import java.util.List;

public interface HidrometroInterface {
    void inserirHidrometro(Hidrometro hidrometro) throws SQLException;
    void atualizarHidrometro(Hidrometro hidrometro) throws SQLException;
    void excluirHidrometro(String numSerie) throws SQLException;
    Hidrometro buscarHidrometroPorSerie(String numSerie) throws SQLException;
    List<Hidrometro> listarHidrometros() throws SQLException;
}
