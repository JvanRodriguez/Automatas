package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

public class AutomataNoDeterminista extends Automata {
    private List<Estado> estadosAceptacion;

    public AutomataNoDeterminista(TipoAutomata tipo,List<String> alfabeto, Estado estadoInicial, List<Estado> estados, List<Estado> estadosAceptacion){
        super(tipo, alfabeto, estadoInicial, estados);
        this.estadosAceptacion = estadosAceptacion;
    }

    public List<Estado> getEstadosAceptacion() {
        return estadosAceptacion;
    }

    public void setEstadosAceptacion(List<Estado> estadosAceptacion) {
        this.estadosAceptacion = estadosAceptacion;
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
}
