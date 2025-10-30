package org.example.model;

import java.util.Date;

public class Leitura {
    private int cod_leitura;
    private double valor_medido;
    private Date data_hora_leitura;
    private Hidrometro hidrometro;

    public Leitura() {}

    public Leitura(int cod_leitura, double valor_medido, Date data_hora_leitura, Hidrometro hidrometro) {
        this.cod_leitura = cod_leitura;
        this.valor_medido = valor_medido;
        this.data_hora_leitura = data_hora_leitura;
        this.hidrometro = hidrometro;
    }

    public int getCod_leitura() {
        return cod_leitura;
    }

    public void setCod_leitura(int cod_leitura) {
        this.cod_leitura = cod_leitura;
    }

    public double getValor_medido() {
        return valor_medido;
    }

    public void setValor_medido(double valor_medido) {
        this.valor_medido = valor_medido;
    }

    public Date getData_hora_leitura() {
        return data_hora_leitura;
    }

    public void setData_hora_leitura(Date data_hora_leitura) {
        this.data_hora_leitura = data_hora_leitura;
    }

    public Hidrometro getHidrometro() {
        return hidrometro;
    }

    public void setHidrometro(Hidrometro hidrometro) {
        this.hidrometro = hidrometro;
    }
}
