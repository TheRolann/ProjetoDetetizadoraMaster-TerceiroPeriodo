package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Cargo;
import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;

    import java.math.BigDecimal;
    import java.util.List;

    @Entity
    @Table(name = "funcionarios")
    public class Funcionario {
        // Atributos
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) // "IDENTITY" banco responsalvel por gerar
        private Long id;

    @Column(name = "nome", length = 100,  nullable = false)
    private String nome;

    @Column(name = "cpf", length = 14,  nullable = false, unique = true)
    private String cpf;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "salario", columnDefinition = "numeric(10,2)") // length e para String, aqui definimos uma definicao
    private double salario;

    @Enumerated(EnumType.STRING) // "Enumerated": Avisa o hibernate que o atributo e ENUM
    @Column(name = "cargo", nullable = false)
    private Cargo cargo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

        @ManyToMany(mappedBy = "servicos")
        private List<Funcionario> funcionarios;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "endereco_id")
        private Endereco endereco;


        // Construtor vazio para estutura para o JPA
        public Funcionario() {}

         // Construtor
        public Funcionario(String nome, String cpf, String telefone, String email, BigDecimal salario, Cargo cargo, Status status) {
            this.nome = nome;
            this.cpf = cpf;
            this.telefone = telefone;
            this.email = email;
            this.salario = salario;
            this.cargo = cargo;
            this.status = status;
        }
    // Construtor vazio para estrutura para o JPA
    public Funcionario() {}

     // Construtor
    public Funcionario(String nome, String cpf, String telefone, String email, double salario, Cargo cargo, Status status) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
        this.salario = salario;
        this.cargo = cargo;
        this.status = status;
    }

    // Getters e Setters

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}
