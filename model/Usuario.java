package org.example.model;
import java.time.LocalDateTime;
public abstract class Usuario {
    protected int cod_usuario;
    protected String nome;
    protected String email;
    protected LocalDateTime dataCadastro;
    protected String tipo_p;

    public Usuario(){}

    public Usuario(String nome, String email){}

    public Usuario(String nome, String email, String tipo_p) {
        this.nome = nome;
        this.email = email;
        this.tipo_p = tipo_p;
        this.dataCadastro = LocalDateTime.now();
    }
    public int getCodUsuario() { return cod_usuario; }
    public void setCodUsuario(int codUsuario) { this.cod_usuario = codUsuario; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTipoP() { return tipo_p; }
    public void setTipoP(String tipoP) { this.tipo_p = tipoP; }

    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDateTime dataCadastro) { this.dataCadastro = dataCadastro; }
}
