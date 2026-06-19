package br.edu.uniamerica.projetomensal.interfaces;

import java.util.List;

// Interface do tipo generico, nao sabe qual objeto manipular, "T" placeholder
// Possibilitado criar apenas uma interface
public interface Crud<T> {
    void salvar(T objeto);
    void excluir(Long id);
    void editar(T objeto);
    T buscarPorId(Long id);
    List<T> listar();
}
