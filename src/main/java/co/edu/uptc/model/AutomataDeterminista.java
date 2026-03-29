package co.edu.uptc.model;

import java.util.List;

public class AutomataDeterminista extends Automata {
    private Estado estadoAceptacion;

    public AutomataDeterminista(TipoAutomata tipo, List<String> alfabeto, Estado estadoInicial, List<Estado> estados, Estado estadoAceptacion){
        super(tipo,alfabeto,estadoInicial,estados);
        this.estadoAceptacion = estadoAceptacion;
    }

    public Estado getEstadoAceptacion() {
        return estadoAceptacion;
    }

    public void setEstadoAceptacion(Estado estadoAceptacion) {
        this.estadoAceptacion = estadoAceptacion;
    }
}
