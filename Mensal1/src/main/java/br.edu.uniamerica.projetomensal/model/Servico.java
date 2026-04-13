package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "servicos")
public class Servico {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome_servico", length = 100, nullable = false)
    private String nomeServico;

    @Column(name = "descricao", columnDefinition = "text")
    private String descricao;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "valor", columnDefinition = "numeric(10,2)")
    private double valor;

    @ManyToOne // Muitos para um, varios servicos pertencem a um cliente
    @JoinColumn(name = "cliente_id", nullable = false, referencedColumnName = "id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToMany
    @JoinTable(
            name = "funcionario_servico",
            joinColumns = @JoinColumn(name = "servico_id"),
            inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private List<Funcionario> funcionarios;

    // Cria instancia/molde vazio para o JPA/Hibernate
    public Servico() {}

    // Construtor
    public Servico(String nomeServico, String descricao, LocalDate data, double valor, Cliente cliente, Status status) {
        this.nomeServico = nomeServico;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
        this.cliente = cliente;
        this.status = status;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeServico() { return nomeServico; }
    public void setNomeServico(String nomeServico) { this.nomeServico = nomeServico; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public int getClienteId() { return cliente.getId(); }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<Funcionario> getFuncionarios() { return funcionarios; }
    public void setFuncionarios(List<Funcionario> funcionarios) { this.funcionarios = funcionarios; }
}


