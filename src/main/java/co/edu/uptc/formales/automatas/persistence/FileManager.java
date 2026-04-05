package co.edu.uptc.formales.automatas.persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.DTO.DTOs.AutomataDTO;
import co.edu.uptc.formales.automatas.DTO.DTOs.TransicionDTO;
import co.edu.uptc.formales.automatas.model.Automata;
import co.edu.uptc.formales.automatas.model.Estado;
import co.edu.uptc.formales.automatas.model.TipoAutomata;
import co.edu.uptc.formales.automatas.model.TipoEstado;
import co.edu.uptc.formales.automatas.model.Transicion;

public class FileManager{

    private final Gson gson = new Gson();

    public Automata importarAutomata(String ruta) throws IOException {
        FileReader reader = new FileReader(ruta);
        DTOs.AutomataDTO dto = gson.fromJson(reader, DTOs.AutomataDTO.class);
        return this.convertirAModelo(dto);
    }
    
    public void exportarAutomata(Automata automata, String ruta) throws IOException {
        System.out.println("Exportando automata a: " + ruta);
        DTOs.AutomataDTO dto = this.convertirADTO(automata);
        FileWriter writer = new FileWriter(ruta);
        gson.toJson(dto, writer);
        writer.close();
        
    }

    private Automata convertirAModelo(DTOs.AutomataDTO automataDto) {
        Map<String, Estado> mapaEstados = new HashMap<>();

        for (String nombre : automataDto.estados()) {
            if (nombre.equals(automataDto.estadoInicial())) {
                mapaEstados.put(nombre, new Estado(nombre, TipoEstado.INITIAL));
            }else if (automataDto.estadosAceptacion().contains(nombre)) {
                mapaEstados.put(nombre, new Estado(nombre, TipoEstado.FINAL));
            }else{
            mapaEstados.put(nombre, new Estado(nombre, TipoEstado.DEFAULT)); 
            }           
        }
        Estado inicial = mapaEstados.get(automataDto.estadoInicial());

        List<Estado> aceptacion = automataDto.estadosAceptacion()
                .stream()
                .map(mapaEstados::get)
                .toList();

        Automata automata = new Automata(
                TipoAutomata.valueOf(automataDto.tipo()),
                automataDto.alfabeto(),
                inicial,
                new ArrayList<>(mapaEstados.values()),
                aceptacion
        );

        List<Transicion> transiciones = new ArrayList<>();
        for (TransicionDTO t : automataDto.transiciones()) {
            Estado origen = mapaEstados.get(t.origen());
            Transicion transicion = new Transicion(origen, t.simbolo());
            List<Estado> destinos = t.destino()
                    .stream()
                    .map(mapaEstados::get)
                    .toList();
            transicion.setEstadoDestino(destinos);
            transiciones.add(transicion);
        }

        automata.setFuncionTransicion(transiciones);

        return automata;
        
    }

    private DTOs.AutomataDTO convertirADTO(Automata automata) {
        List<String> estados = automata.getEstados()
            .stream()
            .map(Estado::getNombre)
            .toList();

        List<String> aceptacion = automata.getEstadosAceptacion()
            .stream()
            .map(Estado::getNombre)
            .toList();

        List<TransicionDTO> transiciones = automata.getFuncionTransicion()
            .stream()
            .map(t -> new TransicionDTO(
                    t.getEstadoOrigen().getNombre(),
                    t.getSimbolo(),
                    t.getEstadoDestino().stream()
                            .map(Estado::getNombre)
                            .toList()
            ))
            .toList();

        return new AutomataDTO(
            automata.getTipo().name(),
            automata.getAlfabeto(),
            automata.getEstadoInicial().getNombre(),
            estados,
            aceptacion,
            transiciones);
    }
}