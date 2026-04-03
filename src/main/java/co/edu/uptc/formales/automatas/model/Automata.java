package co.edu.uptc.formales.automatas.model;

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
        List<Estado> nuevosEstados = new ArrayList<>();
        for (Estado estadoActual : estadosActuales) {
            for (Transicion transicion : this.getFuncionTransicion()) {
                String simb = transicion.getSimbolo();
                if (simb != null && simb.equals(simbolo) 
                        && transicion.getEstadoOrigen().equals(estadoActual)) {
                    nuevosEstados.addAll(transicion.getEstadoDestino());
                }
            }
        }
        return nuevosEstados;
    }

    public Estado validarSimbolo(Estado estadoActual, String simbolo){
        for (Transicion transicionActual : this.getFuncionTransicion()) {
            String simb = transicionActual.getSimbolo();
            if (simb != null && simb.equals(simbolo) 
                    && transicionActual.getEstadoOrigen().equals(estadoActual)) {
                return transicionActual.getEstadoDestino().getFirst();
            }
        }
        return null;
    }
}

