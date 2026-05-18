package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;

// Classe que representa um funcionario no banco de dados
@Entity
@Table(name = "funcionarios") // Nome da tabela no banco
public class Funcionario {
    // Identificador unico do funcionario
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID gerado automaticamente
    private int id;

    // Nome completo do funcionario
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    // CPF do funcionario
    @Column(name = "cpf", length = 14, nullable = false, unique = true)
    private String cpf;

    // Senha de acesso do funcionario
    @Column(name = "senha", length = 100)
    private String senha;

    // Telefone de contato
    @Column(name = "telefone", length = 20)
    private String telefone;

    // Email de contato
    @Column(name = "email", length = 100)
    private String email;

    // Endereco completo
    @Column(name = "endereco", columnDefinition = "text")
    private String endereco;

    // Salario do funcionario com casa decimal no banco
    @Column(name = "salario", columnDefinition = "numeric(10,2)")
    private double salario;

    // Cargo do funcionario guardado como texto
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    private Cargo cargo;

    // Status do funcionario guardado como texto
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // Construtor vazio exigido pelo JPA
    public Funcionario() {}

    // Construtor para criar um funcionario com os dados principais
    public Funcionario(String nome, String cpf, String telefone, String email, String endereco, double salario, Cargo cargo, Status status) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
        this.salario = salario;
        this.cargo = cargo;
        this.status = status;
    }

    // Getters e setters para acessar e alterar os dados

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    // Define a senha do funcionario
    public void setSenha(String senha) { this.senha = senha; }

    // Confere se a senha informada bate com a senha salva
    public boolean senhaCorreta(String senhaInformada) { // Metodo para verificacao sem expor. Sem hash
        return this.senha != null && this.senha.equals(senhaInformada);
    }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
