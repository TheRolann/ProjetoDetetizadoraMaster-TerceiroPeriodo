package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;

@Entity // Transforma classe em tabela
@Table(name = "clientes") // Nome da tabela
public class Cliente {
    // Atributos
    @Id // Novo ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID com novo auto-incremento
    private int id;

    @Column
    private String nomeEmpresa;
    private String documento; // Pode ser CNPJ ou CPF
    private String endereco;
    private String telefone;
    private String email;
    private Status status;

    // Construtor
    public Cliente(int id, String nomeEmpresa, String documento, String endereco, String telefone, String email, Status status) {
        this.id = id;
        this.nomeEmpresa = nomeEmpresa;
        this.documento = documento;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.status = status;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeEmpresa() { return nomeEmpresa; }
    public void setNomeEmpresa(String nomeEmpresa) { this.nomeEmpresa = nomeEmpresa; }

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getEndereco() { return endereco; }
    public void  setEndereco (String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone (String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail (String email) { this.email = email; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}

