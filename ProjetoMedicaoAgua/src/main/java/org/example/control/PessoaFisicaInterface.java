package org.example.control;

import org.example.model.Alerta;
import org.example.model.Leitura;
import org.example.model.PessoaFisica;
import java.sql.SQLException;
import java.util.List;

public interface PessoaFisicaInterface {
    void inserirPessoaFisica(PessoaFisica pessoaFisica) throws SQLException;
    void atualizarPessoaFisica(PessoaFisica pessoaFisica) throws SQLException;
    void excluirPessoaFisica(int codUsuario) throws SQLException;
    PessoaFisica buscarPessoaFisicaPorId(int codUsuario) throws SQLException;
    List<PessoaFisica> listarPessoasFisicas() throws SQLException;
    List<Alerta> buscarAlertasproCNPJ(String CNPJ) throws SQLException;
    List<Leitura> buscarLeiturasCNPJ(String CNPJ) throws SQLException; 
    
    PessoaFisica buscarPessoaFisicaPorCPF(String cpf) throws SQLException;
}