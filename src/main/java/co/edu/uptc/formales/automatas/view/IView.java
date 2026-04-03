package co.edu.uptc.formales.automatas.view;

import java.util.List;
import java.util.Map;
import co.edu.uptc.formales.automatas.DTO.DTOs.TransicionDTO;
import co.edu.uptc.formales.automatas.model.TipoAutomata;

public interface IView {

    public void mostrarTrazabilidad(Map<String,String> resultadosTrazabilidad);

    public void mostrarResultadosPrueba(Map<String,Boolean> resultados);

    public List<String> getCadenasPruebas();

    public List<String> getEstadosAceptacion();

    public TransicionDTO getTransicion(String estado, String simbolo);

    public String getEstadoInicial();

    public List<String> getAlfabeto();

    public TipoAutomata getTipoAutomata();

    public List<String> getEstados();

    public List<TransicionDTO> getTransiciones(List<String> estados, List<String> simbolos);

    public List<String> getCadenaPrueba();
    
}
