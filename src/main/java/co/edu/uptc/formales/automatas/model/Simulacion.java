package co.edu.uptc.formales.automatas.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.persistence.FileManager;

public class Simulacion implements IModel{

    private Automata automataDeseado;
    //private HashMap<String, Boolean> map;
    private FileManager fileManager;

    public Simulacion() {
        this.fileManager = new FileManager();
    }

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
        LinkedHashMap<String, Boolean> resultadoEvaluacion = new LinkedHashMap<>();
        Estado estadoInicial = automataDeseado.getEstadoInicial();
        List<Estado> estadosAceptacion = automataDeseado.getEstadosAceptacion();
        
        for (String cadena : cadenas) {
            boolean aceptada = false;
            if (automataDeseado.getTipo().equals(TipoAutomata.AFD)) {
                Estado estadoActual = estadoInicial;
                for (int index = 0; index < cadena.length(); index++) {
                    String simbolo = String.valueOf(cadena.charAt(index));
                    estadoActual = automataDeseado.validarSimbolo(estadoActual, simbolo);
                    if (estadoActual == null) break;
                }
                aceptada = (estadoActual != null && estadosAceptacion.contains(estadoActual));
                
            } else { // AFN
                List<Estado> estadosActuales = new ArrayList<>();
                estadosActuales.add(estadoInicial);
                
                for (int index = 0; index < cadena.length(); index++) {
                    String simbolo = String.valueOf(cadena.charAt(index));
                    estadosActuales = automataDeseado.validarSimbolo(estadosActuales, simbolo);
                    if (estadosActuales == null || estadosActuales.isEmpty()) break;
                }
                
                if (estadosActuales != null) {
                    for (Estado estado : estadosActuales) {
                        if (estadosAceptacion.contains(estado)) {
                            aceptada = true;
                            break;
                        }
                    }
                }
            }
            resultadoEvaluacion.put(cadena, aceptada);
        }
        return resultadoEvaluacion;
    }

    @Override
    public String obtenerTrazabilidad(String cadena) {
        String trazabilidad = "";
        
        if (automataDeseado.getTipo().equals(TipoAutomata.AFD)) {
            Estado estadoActual = automataDeseado.getEstadoInicial();
            trazabilidad = estadoActual.getNombre();
            for (int i = 0; i < cadena.length(); i++) {
                String simbolo = String.valueOf(cadena.charAt(i));
                estadoActual = automataDeseado.validarSimbolo(estadoActual, simbolo);
                if (estadoActual == null) return "No válida";
                trazabilidad += " -" + simbolo + "-> " + estadoActual.getNombre();
            }
        } else { // AFN
            List<Estado> estadosActuales = new ArrayList<>();
            estadosActuales.add(automataDeseado.getEstadoInicial());
            trazabilidad += "{" + estadosActuales.get(0).getNombre() + "}";
            
            for (int i = 0; i < cadena.length(); i++) {
                String simbolo = String.valueOf(cadena.charAt(i));
                estadosActuales = automataDeseado.validarSimbolo(estadosActuales, simbolo);
                if (estadosActuales.isEmpty()) return "No válida";
                trazabilidad += " -" + simbolo + "-> {" + 
                    estadosActuales.stream().map(Estado::getNombre).reduce((a,b) -> a + "," + b).orElse("") + "}";
            }
        }
        return trazabilidad;
    }

    public List<DTOs.TransicionDTO> getTransicionesBase(){
        List<DTOs.TransicionDTO> transicionesBase = new ArrayList<>();
        for(Transicion tBase:this.automataDeseado.getFuncionTransicion()){
            DTOs.TransicionDTO aux = tBase.toDTOBase();
            transicionesBase.add(aux);
        }
        return transicionesBase;
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

    @Override
    public boolean exportarAutomata(String ruta){
        System.out.println("LOG: RUTA RECIBIDA EN SIMULACION: " + ruta);
        try {
            System.out.println("LOG: AUTOMATA A EXPORTAR: " + automataDeseado.toString());
            fileManager.exportarAutomata(automataDeseado, ruta);
            System.out.println("LOG DESPUES DE EXPORTAR1.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        
    }
    @Override
    public boolean importarAutomata(String ruta){
        try {
            this.automataDeseado = fileManager.importarAutomata(ruta);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Automata getAutomataDeseado() {
        return automataDeseado;
    }
}