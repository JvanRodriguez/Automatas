package co.edu.uptc.formales.automatas.model;

import java.util.HashMap;
import java.util.List;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.DTO.DTOs.TransicionDTO;

public interface IModel {

    public void crearAutomata(List<Estado> estados, List<String> alfabeto, Estado inicial, List<Estado> aceptacion, TipoAutomata tipo);
    public void crearFuncionTransicionBase(List<Estado> estados, List<String> alfabeto);
    public HashMap<String, Boolean> evaluarCadenasPrueba(List<String> cadenas);
    public String obtenerTrazabilidad(String cadena);
    public List<DTOs.TransicionDTO> getTransicionesBase();
    public void completarTransicion(TransicionDTO aux);
    
}
