package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;

@Entity
@Table(name = "servicos")
public class Servico {

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nome_servico", length = 100, nullable = false)
    private String nomeServico = "";

    @Column(name = "descricao")
    private String descricao = "";

    @Column(name = "data", nullable = false)
    private String data = "";

    @Column(name = "valor", columnDefinition = "numeric(10, 2)")
    private double valor;

    // Com o banco, usamos o objeto inteiro e as anotacoes
    // private int clienteID;

    @ManyToOne // Muitos para um, varios servicos pertencem a um cliente
    @JoinColumn(name = "cliente_id", nullable = false, referencedColumnName = "id") // Faz a juncao da coluna ID do cliente
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    // Construtor - Ja atualizado com Cliente cliente
    public Servico(int id, String nomeServico, String descricao, String data,
                   double valor, Cliente cliente, Status status){
        this.id = id;
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

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    // Para pegar o ID do cliente
    public int getClienteId() { return cliente.getId(); }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}


