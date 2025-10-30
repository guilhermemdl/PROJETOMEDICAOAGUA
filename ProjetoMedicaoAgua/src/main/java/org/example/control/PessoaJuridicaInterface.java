package org.example.control;

import org.example.model.PessoaJuridica;
import java.sql.SQLException;
import java.util.List;
import org.example.model.Alerta;
import org.example.model.Leitura; 

public interface PessoaJuridicaInterface {
    void inserirPessoaJuridica(PessoaJuridica pessoaJuridica) throws SQLException;
    void atualizarPessoaJuridica(PessoaJuridica pessoaJuridica) throws SQLException;
    void excluirPessoaJuridica(int codUsuario) throws SQLException;
    PessoaJuridica buscarPessoaJuridicaPorId(int codUsuario) throws SQLException;
    List<PessoaJuridica> listarPessoasJuridicas() throws SQLException;
    List<Alerta> buscarAlertasproCNPJ(String CNPJ) throws SQLException;

    List<Leitura> buscarLeiturasCNPJ(String cnpj) throws SQLException;
    PessoaJuridica buscarPessoaJuridicaPorCNPJ(String cnpj) throws SQLException;
}