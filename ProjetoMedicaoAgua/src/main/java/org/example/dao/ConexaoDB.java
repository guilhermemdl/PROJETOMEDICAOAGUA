package org.example.dao;
import java.sql.Connection;
public interface ConexaoDB
{
    Connection obterconexao() throws Exception;

    void fecharconexao(Connection banco);
}