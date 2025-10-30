package org.example.model;

public class Imovel {
    private int cod_imovel;
    private String tipo_imovel;
    private Endereco endereco;

    public Imovel() {}

    public Imovel(int cod_imovel, String tipo_imovel, Endereco endereco) {
        this.cod_imovel = cod_imovel;
        this.tipo_imovel = tipo_imovel;
        this.endereco = endereco;
    }

    public int getCod_imovel() {
        return cod_imovel;
    }

    public void setCod_imovel(int cod_imovel) {
        this.cod_imovel = cod_imovel;
    }

    public String getTipo_imovel() {
        return tipo_imovel;
    }

    public void setTipo_imovel(String tipo_imovel) {
        this.tipo_imovel = tipo_imovel;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
