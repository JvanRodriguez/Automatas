package co.edu.uptc.model;

import java.util.List;

public class Automata {
    private TipoAutomata tipo;
    private List<Estado> estados;
    private List<String> alfabeto;
    private Estado estadoInicial;
    private List<Transicion> funcionTransicion;

    public Automata(TipoAutomata tipo, List<String> alfabeto, Estado estadoInicial, List<Estado> estados) {
        this.tipo = tipo;
        this.alfabeto = alfabeto;
        this.estadoInicial = estadoInicial;
        this.estados = estados;
    }

    public List<String> getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(List<String> alfabeto) {
        this.alfabeto = alfabeto;
    }

    public Estado getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(Estado estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public List<Estado> getEstados() {
        return estados;
    }

    public void setEstados(List<Estado> estados) {
        this.estados = estados;
    }

    public List<Transicion> getFuncionTransicion() {
        return funcionTransicion;
    }

    public void setFuncionTransicion(List<Transicion> funcionTransicion) {
        this.funcionTransicion = funcionTransicion;
    }

    public TipoAutomata getTipo() {
        return tipo;
    }

    public void setTipo(TipoAutomata tipo) {
        this.tipo = tipo;
    }
}

