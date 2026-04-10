package br.edu.uniamerica.projetomensal.model;

import jakarta.persistence.*;

@Entity
@Table(name = "enderecos")
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rua", nullable = false, length = 150)
    private String rua;
    @Column(name = "cidade", nullable = false, length = 80)
    private String cidade;
    @Column(name = "numero", nullable = false, length = 25)
    private String numero;

    @OneToOne(mappedBy = "endereco")
    private Funcionario funcionario;

    public Endereco() {

    }
    public Endereco(String rua, String cidade, String numero) {
        this.rua = rua;
        this.cidade = cidade;
        this.numero = numero;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getRua() {return rua;}
    public void setRua(String rua) {this.rua = rua;}

    public String getCidade() {return cidade;}
    public void setCidade(String cidade) {this.cidade = cidade;}

    public String getNumero() {return numero;}
    public void setNumero(String numero) {this.numero = numero;}
}
