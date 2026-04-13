package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;

import java.util.List;

@Entity // Transforma classe em tabela
@Table(name = "clientes") // Nome da tabela
public class Cliente {
    // Atributos
    @Id // Novo ID
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID com novo auto-incremento
    private int id;

    @Column(name = "nome_empresa", length = 150, nullable = false)
    private String nomeEmpresa;

    @Column(name = "documento", length = 18, nullable = false, unique = true)
    private String documento; // Pode ser CNPJ ou CPF

    @Column(name = "endereco", columnDefinition = "text")
    private String endereco;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "email", length = 100)
    private String email;

    @Enumerated(EnumType.STRING) // Indica que é um ENUM
    @Column(name = "status", nullable = false)
    private Status status;

    // Relacionamento com Servicos
    @OneToMany(mappedBy = "cliente")
    private List<Servico> servicos;

    // Cria instancia/molde vazio para o JPA/Hibernate
    public Cliente() {}

    // Construtor
    public Cliente(String nomeEmpresa, String documento, String endereco, String telefone, String email, Status status) {
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
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}

