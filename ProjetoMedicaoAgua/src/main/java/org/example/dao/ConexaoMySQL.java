package org.example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoMySQL implements ConexaoDB {
    
    private static final String URL = "jdbc:mysql://localhost:3306/monitoramentoconsumodeagua";
    private static final String USUARIO = "root";
    private static final String SENHA = "";

    @Override
    public Connection obterconexao() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
            System.out.println("Conexão Estabelecida com monitoramentoconsumodeagua");
            return conexao;
        } catch (ClassNotFoundException e) {
            throw new Exception("Driver JDBC nao encontrado" + e.getMessage());
        } catch (SQLException e) {
            throw new Exception("Erro ao conectar ao banco: " + e.getMessage());
        }

    }

    @Override
    public void fecharconexao(Connection banco) {
        if (banco != null) {
            try {
                banco.close();
                System.out.println("Conexão Fechada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}