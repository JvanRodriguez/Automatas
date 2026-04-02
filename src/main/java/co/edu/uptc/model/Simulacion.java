package co.edu.uptc.model;

import java.util.HashMap;
import java.util.List;

public class Simulacion implements IModel{
    private Automata automata;
    private HashMap<String, Boolean> lotes;
    private Estado estadoFinalCadenaActual;

    @Override
    public void crearAutomata(List<Estado> estados, List<String> alfabeto, Estado inicial, List<Estado> aceptacion, TipoAutomata tipo) {
        if (tipo.equals(TipoAutomata.AFD)) {
            this.automata = new AutomataDeterminista(tipo, alfabeto, inicial, estados, aceptacion)
        } else {
            this.automata = new AutomataNoDeterminista(tipo, alfabeto, inicial, estados, aceptacion);
            //comentario chistoso para hacer push y lo que sea lol xd omg
        }
    }

    @Override
    public boolean agregarFuncionTransicion(List<Transicion> transiciones) {
        this.automata.setFuncionTransicion(transiciones);
    }

    public Automata getAutomata() {
        return automata;
    }
    public void setAutomata(Automata automata) {
        this.automata = automata;
    }
    public HashMap<String, Boolean> getLotes() {
        return lotes;
    }
    public void setLotes(HashMap<String, Boolean> lotes) {
        this.lotes = lotes;
    }
    public Estado getEstadoFinalCadenaActual() {
        return estadoFinalCadenaActual;
    }
    public void setEstadoFinalCadenaActual(Estado estadoFinalCadenaActual) {
        this.estadoFinalCadenaActual = estadoFinalCadenaActual;
    }

    
}