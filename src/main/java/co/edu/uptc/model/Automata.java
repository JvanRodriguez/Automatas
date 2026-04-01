package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

public class Automata {
    private TipoAutomata tipo;
    private List<Estado> estados;
    private List<String> alfabeto;
    private Estado estadoInicial;
    private List<Transicion> funcionTransicion;
    private List<Estado> estadosAceptacion; 

    public Automata(TipoAutomata tipo, List<String> alfabeto, Estado estadoInicial, List<Estado> estados, List<Estado> estadosAceptacion) {
        this.tipo = tipo;
        this.alfabeto = alfabeto;
        this.estadoInicial = estadoInicial;
        this.estados = estados;
        this.estadosAceptacion = estadosAceptacion;
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

    public List<Estado> getEstadosAceptacion() {
        return this.estadosAceptacion;
    }

    public void setEstadosAceptacion(List<Estado> estadosAceptacion) {
        this.estadosAceptacion = estadosAceptacion;
    }

    public TipoAutomata getTipo() {
        return tipo;
    }

    public void setTipo(TipoAutomata tipo) {
        this.tipo = tipo;
    }

    public List<Estado> validarSimbolo(List<Estado> estadosActuales, String simbolo) {
        List<Estado> estadosSiguientes = new ArrayList<>();

        for (Transicion transicion : this.getFuncionTransicion()) {
            if (estadosActuales.contains(transicion.getEstadoOrigen())
                    && transicion.getSimbolo().equals(simbolo)) {
                estadosSiguientes.addAll(transicion.getEstadoDestinoAFN());
            }
        }
        return estadosSiguientes;
    }

    public Estado validarSimbolo(Estado estadoActual, String simbolo){
        for (Transicion transicionActual: this.getFuncionTransicion()){
            if (transicionActual.getSimbolo().equals(simbolo) && transicionActual.getEstadoOrigen().equals(estadoActual)){
                return transicionActual.getEstadoDestinoAFD();
            }
        }
        return null;
    }
}

