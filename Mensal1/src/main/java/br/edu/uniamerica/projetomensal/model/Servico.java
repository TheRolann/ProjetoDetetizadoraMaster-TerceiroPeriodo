package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

// Classe que representa um servico no banco de dados
@Entity
@Table(name = "servicos") // Nome da tabela no banco
public class Servico {

    // Identificador unico do servico
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID gerado automaticamente
    private int id;

    // Nome do servico
    @Column(name = "nome_servico", length = 100, nullable = false)
    private String nomeServico;

    // Descricao do que sera feito
    @Column(name = "descricao", columnDefinition = "text")
    private String descricao;

    // Data em que o servico acontece
    @Column(name = "data", nullable = false)
    private LocalDate data;

    // Valor cobrado pelo servico
    @Column(name = "valor", columnDefinition = "numeric(10,2)")
    private double valor;

    // Muitos servicos podem pertencer a um cliente
    @ManyToOne // Relacao muitos para um
    @JoinColumn(name = "cliente_id", nullable = false, referencedColumnName = "id")
    private Cliente clienteEntity;

    // Status do servico guardado como texto
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // Varios funcionarios podem participar de um servico
    @ManyToMany
    @JoinTable(
            name = "funcionario_servico",
            joinColumns = @JoinColumn(name = "servico_id"),
            inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    private List<Funcionario> funcionarioEntities;

    // Construtor vazio exigido pelo JPA
    public Servico() {}

    // Construtor para criar um servico com os dados principais
    public Servico(String nomeServico, String descricao, LocalDate data, double valor, Cliente clienteEntity, Status status) {
        this.nomeServico = nomeServico;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
        this.clienteEntity = clienteEntity;
        this.status = status;
    }

    // Getters e setters para acessar e alterar os dados
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

    public Cliente getCliente() { return clienteEntity; }
    public void setCliente(Cliente clienteEntity) { this.clienteEntity = clienteEntity; }

    // Retorna o ID do cliente ligado a este servico
    public int getClienteId() { return clienteEntity.getId(); }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<Funcionario> getFuncionarios() { return funcionarioEntities; }
    public void setFuncionarios(List<Funcionario> funcionarioEntities) { this.funcionarioEntities = funcionarioEntities; }
}


