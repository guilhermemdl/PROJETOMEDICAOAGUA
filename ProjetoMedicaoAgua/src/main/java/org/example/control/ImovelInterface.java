package org.example.control;

import org.example.model.Imovel;
import java.sql.SQLException;
import java.util.List;

public interface ImovelInterface {
    void inserirImovel(Imovel imovel) throws SQLException;
    void atualizarImovel(Imovel imovel) throws SQLException;
    void excluirImovel(int codImovel) throws SQLException;
    Imovel buscarImovelPorId(int codImovel) throws SQLException;
    List<Imovel> listarImoveis() throws SQLException;
}
