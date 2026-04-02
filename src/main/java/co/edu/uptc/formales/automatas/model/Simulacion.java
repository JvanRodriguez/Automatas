package co.edu.uptc.formales.automatas.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.edu.uptc.formales.automatas.DTO.DTOs;

public class Simulacion implements IModel{

    private Automata automataDeseado;
    private Estado estadoFinalCadenaActual;
    private HashMap<String, Boolean> map;


    @Override
    public void crearAutomata(List<Estado> estados, List<String> alfabeto, Estado inicial, List<Estado> aceptacion, TipoAutomata tipo) {
        this.automataDeseado = new Automata(tipo, alfabeto, inicial, estados, aceptacion);
        crearFuncionTransicionBase(estados, alfabeto);
    }

    @Override
    public void crearFuncionTransicionBase(List<Estado> estados, List<String> alfabeto) {
        List<Transicion> transicionesBase = new ArrayList<Transicion>();
        for (int i = 0; i < estados.size(); i++) {
            for (int j = 0; j < alfabeto.size(); j++) {
                Transicion transicionActual = new Transicion(estados.get(i), alfabeto.get(j));
                transicionesBase.add(transicionActual);
            }
        }
        this.automataDeseado.setFuncionTransicion(transicionesBase);
    }

    @Override
    public HashMap<String, Boolean> evaluarCadenasPrueba(List<String> cadenas) {
        HashMap<String, Boolean> resultadoEvaluacion = new HashMap<>();
        Estado estadoActual = automataDeseado.getEstadoInicial();
        List<Estado> estadosAceptacion = automataDeseado.getEstadosAceptacion();
        for (String cadena : cadenas) {
            for (int index = 0; index < cadena.length() ; index++) {
                String simbolo = String.valueOf(cadena.charAt(index));
                if(automataDeseado.getTipo().equals(TipoAutomata.AFD)){
                    automataDeseado.validarSimbolo(estadoActual, simbolo);
                }
            }
            if (estadosAceptacion.contains(estadoActual)) {
                resultadoEvaluacion.put(cadena, true);
            } else {
                resultadoEvaluacion.put(cadena, false);
            }
        }
        return resultadoEvaluacion;
    }

    @Override
    public String obtenerTrazabilidad(String cadena) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'obtenerTrazabilidad'");
    }

    //Metodo que completa la transicion con los objetos TransicionDTO

    public void completarTransicion(DTOs.TransicionDTO dtoLleno) {
        // Busca transicion incompleta original
        Transicion transicionOriginal = buscarTransicion(dtoLleno.origen(), dtoLleno.simbolo());
        // Busca el objeto Estado que coincide con el/los destino que el usuario ingreso
        List<Estado> destino = buscarEstado(dtoLleno.destino());

        // Settear a la transicion original la lista de estados destino
        if (transicionOriginal != null && destino != null) {
            transicionOriginal.setEstadoDestino(destino);
        }
    }

    public Transicion buscarTransicion (String origen, String simbolo){
        for (int i = 0; i < this.automataDeseado.getFuncionTransicion().size(); i++){
            Transicion aux = this.automataDeseado.getFuncionTransicion().get(i);
            if (aux.getEstadoOrigen().getNombre().equals(origen) && aux.getSimbolo().equals(simbolo)){
                return this.automataDeseado.getFuncionTransicion().get(i);
            }
        }
        return null;
    }

    public List<Estado> buscarEstado(List<String> nombresDestino) {
        return this.automataDeseado.getEstados().stream()
                // Busca el nombre que se ingresa en la lista de estados del automata
                .filter(e -> nombresDestino.stream()
                        .anyMatch(nombre -> nombre.trim().equalsIgnoreCase(e.getNombre())))
                .toList();
    }
}