package org.example.model;

import java.time.LocalDate;

public class PessoaFisica extends Usuario {
    private String cpf;
    private LocalDate dataNascimento;

    public PessoaFisica() {
        super();
        this.tipo_p = "FISICA";
    }

    public PessoaFisica(String nome, String email, String cpf, LocalDate dataNascimento) {
        super(nome, email, "FISICA");
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
