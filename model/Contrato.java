package org.example.model;

import java.util.Date;

public class Contrato {
    private int cod_contrato;
    private Usuario usuario;
    private Imovel imovel;
    private Hidrometro hidrometro;
    private Date data_inicio;
    private String status;

    public Contrato() {}

    public Contrato(int cod_contrato, Usuario usuario, Imovel imovel, Hidrometro hidrometro, Date data_inicio, String status) {
        this.cod_contrato = cod_contrato;
        this.usuario = usuario;
        this.imovel = imovel;
        this.hidrometro = hidrometro;
        this.data_inicio = data_inicio;
        this.status = status;
    }

    public int getCod_contrato() {
        return cod_contrato;
    }

    public void setCod_contrato(int cod_contrato) {
        this.cod_contrato = cod_contrato;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Imovel getImovel() {
        return imovel;
    }

    public void setImovel(Imovel imovel) {
        this.imovel = imovel;
    }

    public Hidrometro getHidrometro() {
        return hidrometro;
    }

    public void setHidrometro(Hidrometro hidrometro) {
        this.hidrometro = hidrometro;
    }

    public Date getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(Date data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
