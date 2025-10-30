package org.example.model;

public class Hidrometro {
    private String num_serie_hidrometro; 
    private String marca;
    private String modelo;

    public Hidrometro() {}

    public Hidrometro(String num_serie_hidrometro, String marca, String modelo) {
        this.num_serie_hidrometro = num_serie_hidrometro;
        this.marca = marca;
        this.modelo = modelo;
    }

    public String getNum_serie_hidrometro() {
        return num_serie_hidrometro;
    }

    public void setNum_serie_hidrometro(String num_serie_hidrometro) {
        this.num_serie_hidrometro = num_serie_hidrometro;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
}