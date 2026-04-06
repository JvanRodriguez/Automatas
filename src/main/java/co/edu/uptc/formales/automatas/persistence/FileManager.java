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

/**
 * Gestor de archivos para la persistencia de autómatas.
 * Permite importar y exportar la definición de los autómatas en formato JSON,
 * encargándose de la serialización y deserialización a través de la librería Gson,
 * además de realizar conversiones entre los DTOs y el modelo de dominio.
 */
public class FileManager{

    private final Gson gson = new Gson();

    /**
     * Importa un autómata desde un archivo JSON.
     * 
     * @param ruta La ruta del archivo desde donde se cargará el autómata.
     * @return El objeto Automata convertido a partir de los datos leídos.
     * @throws IOException Si ocurre un error de lectura durante la importación.
     */
    public Automata importarAutomata(String ruta) throws IOException {
        try (FileReader reader = new FileReader(ruta)) {
            DTOs.AutomataDTO dto = gson.fromJson(reader, DTOs.AutomataDTO.class);
            return this.convertirAModelo(dto);
        }
    }
    
    /**
     * Exporta un autómata y lo guarda como un archivo JSON.
     * 
     * @param automata El autómata a exportar.
     * @param ruta La ruta o directorio destino donde se guardará el autómata. Si es
     *             un directorio, se creará un archivo automático llamado "automata.json".
     * @throws IOException Si ocurre un error de escritura durante la exportación.
     */
    public void exportarAutomata(Automata automata, String ruta) throws IOException {
        ruta = ruta.endsWith(".json") ? ruta : ruta + "\\automata.json";
        System.out.println("Exportando automata a: " + ruta);
        DTOs.AutomataDTO dto = this.convertirADTO(automata);
        try (FileWriter writer = new FileWriter(ruta)) {
            gson.toJson(dto, writer);
        }
        
    }

    /**
     * Convierte un objeto de transferencia de datos (AutomataDTO) en un objeto
     * de dominio representativo de un autómata. Inicializa estados, alfabetos
     * y las listas de transiciones completas basándose en los datos del DTO.
     * 
     * @param automataDto El autómata proveniente de su forma de DTO.
     * @return El autómata real reconstruido.
     */
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

    /**
     * Convierte un modelo de autómata al formato DTO para que sea persistido 
     * de forma simple y en texto por Gson.
     * 
     * @param automata El autómata a ser convertido.
     * @return El AutomataDTO con la versión serializada.
     */
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