package br.edu.uniamerica.projetomensal.model;

import br.edu.uniamerica.projetomensal.model.enums.Status;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "agenda")
public class Agenda {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "data_agendada", nullable = false) private LocalDate data;
    @Column(nullable = false) private LocalTime hora;
    @Column(columnDefinition = "text") private String observacoes;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Status status;
    @ManyToOne @JoinColumn(name = "servico_id", nullable = false) private Servico servico;
    @ManyToOne @JoinColumn(name = "funcionario_id") private Funcionario funcionario;
    public Agenda() {}
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) { this.hora = hora; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Servico getServico() { return servico; }
    public void setServico(Servico servico) { this.servico = servico; }
    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
}
