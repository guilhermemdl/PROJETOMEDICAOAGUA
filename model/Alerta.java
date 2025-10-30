package org.example.model;
import java.util.Date;
public class Alerta {
    private int cod_alerta;
    private Date data_hora_alerta;
    private String tipo_alerta;
    private Leitura leitura;

    public Alerta() {}

    public Alerta(int cod_alerta, Date data_hora_alerta, String tipo_alerta, Leitura leitura) {
        this.cod_alerta = cod_alerta;
        this.data_hora_alerta = data_hora_alerta;
        this.tipo_alerta = tipo_alerta;
        this.leitura = leitura;
    }

    public int getCod_alerta() {
        return cod_alerta;
    }

    public void setCod_alerta(int cod_alerta) {
        this.cod_alerta = cod_alerta;
    }

    public Date getData_hora_alerta() {
        return data_hora_alerta;
    }

    public void setData_hora_alerta(Date data_hora_alerta) {
        this.data_hora_alerta = data_hora_alerta;
    }

    public String getTipo_alerta() {
        return tipo_alerta;
    }

    public void setTipo_alerta(String tipo_alerta) {
        this.tipo_alerta = tipo_alerta;
    }

    public Leitura getLeitura() {
        return leitura;
    }

    public void setLeitura(Leitura leitura) {
        this.leitura = leitura;
    }
}