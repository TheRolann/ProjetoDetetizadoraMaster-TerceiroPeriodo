package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;

import java.util.List;

// Classe que representa um cliente no banco de dados
@Entity // Transforma a classe em tabela
@Table(name = "clientes") // Define o nome da tabela
public class Cliente {
    // Identificador unico do cliente
    @Id // Campo chave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID gerado automaticamente
    private int id;

    // Nome da empresa do cliente
    @Column(name = "nome_empresa", length = 150, nullable = false)
    private String nomeEmpresa;

    // Documento do cliente, pode ser CNPJ ou CPF
    @Column(name = "documento", length = 18, nullable = false, unique = true)
    private String documento;

    // Endereco completo do cliente
    @Column(name = "endereco", columnDefinition = "text")
    private String endereco;

    // Telefone de contato
    @Column(name = "telefone", length = 20)
    private String telefone;

    // Email de contato
    @Column(name = "email", length = 100)
    private String email;

    // Status do cliente guardado como texto no banco
    @Enumerated(EnumType.STRING) // Indica que o valor vem de um enum
    @Column(name = "status", nullable = false)
    private Status status;

    // Um cliente pode ter varios servicos ligados a ele
    @OneToMany(mappedBy = "clienteEntity")
    private List<Servico> servicoEntities;

    // Construtor vazio exigido pelo JPA
    public Cliente() {}

    // Construtor para criar um cliente com os dados principais
    public Cliente(String nomeEmpresa, String documento, String endereco, String telefone, String email, Status status) {
        this.nomeEmpresa = nomeEmpresa;
        this.documento = documento;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.status = status;
    }

    // Getters e setters para acessar e alterar os dados
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

    // Faz o ComboBox mostrar o nome da empresa ao inves do endereco do objeto
    @Override
    public String toString() {
        return nomeEmpresa;
    }
}

