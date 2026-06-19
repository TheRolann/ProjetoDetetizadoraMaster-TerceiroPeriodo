package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;
import org.mindrot.jbcrypt.BCrypt;

// Classe que representa um funcionario no banco de dados
@Entity
@Table(name = "funcionarios")
public class Funcionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "cpf", length = 14, nullable = false, unique = true)
    private String cpf;

    // Hash da senha de acesso do funcionario (BCrypt, nunca texto puro)
    @Column(name = "senha", length = 100)
    private String senha;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "endereco", columnDefinition = "text")
    private String endereco;

    @Column(name = "salario", columnDefinition = "numeric(10,2)")
    private double salario;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    private Cargo cargo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Funcionario() {}

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

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    // Retorna o hash armazenado (uso interno/JPA). Nunca expor a senha em texto puro.
    public String getSenha() { return senha; }

    // Recebe a senha em texto puro e armazena apenas o hash BCrypt.
    // BCrypt.gensalt() gera um salt aleatorio novo a cada chamada,
    // entao a mesma senha produz hashes diferentes - isso e esperado.
    public void setSenha(String senhaTextoPuro) {
        this.senha = BCrypt.hashpw(senhaTextoPuro, BCrypt.gensalt());
    }

    // Regrava um hash ja existente (ex: vindo do banco). Nao faz hash em cima de hash.
    public void setSenhaHash(String hashJaExistente) {
        this.senha = hashJaExistente;
    }

    // Confere se a senha informada (texto puro) bate com o hash salvo
    public boolean senhaCorreta(String senhaInformada) {
        if (this.senha == null || senhaInformada == null) return false;
        try {
            return BCrypt.checkpw(senhaInformada, this.senha);
        } catch (IllegalArgumentException e) {
            // Valor salvo nao e um hash BCrypt valido (ex: registro antigo em texto puro)
            return false;
        }
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
