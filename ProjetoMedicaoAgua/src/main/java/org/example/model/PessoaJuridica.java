package org.example.model;

public class PessoaJuridica extends Usuario {
    private String cnpj;
    private String razao_social;

    public PessoaJuridica() {
        super();
        this.tipo_p = "JURIDICA";
    }

    public PessoaJuridica(String nome, String email, String cnpj, String razaoSocial) {
        super(nome, email, "JURIDICA");
        this.cnpj = cnpj;
        this.razao_social = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razao_social;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razao_social = razaoSocial;
    }
}
