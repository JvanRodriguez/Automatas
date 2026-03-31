package co.edu.uptc.model;

import java.util.List;

public interface IModel {

    public void crearAutomata(List<Estado> estados, List<String> alfabeto, Estado inicial, List<Estado> aceptacion, TipoAutomata tipo);
    public boolean agregarFuncionTransicion(List<Transicion> transiciones);
    
}
