package co.edu.uptc.formales.automatas.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import co.edu.uptc.formales.automatas.model.Automata;
import co.edu.uptc.formales.automatas.model.Estado;
import co.edu.uptc.formales.automatas.model.TipoAutomata;
import co.edu.uptc.formales.automatas.model.TipoEstado;
import co.edu.uptc.formales.automatas.model.Transicion;

class FileManagerTest {

    @Test
    void exportarAutomata_deberiaCrearJsonValido(@TempDir Path tempDir) throws IOException {
        FileManager fileManager = new FileManager();
        Automata automata = crearAutomataEjemploAfd();
        Path rutaArchivo = tempDir.resolve("automata.json");

        fileManager.exportarAutomata(automata, rutaArchivo.toString());

        assertTrue(Files.exists(rutaArchivo));
        String json = Files.readString(rutaArchivo);
        assertTrue(json.contains("\"tipo\":\"AFD\""));
        assertTrue(json.contains("\"estadoInicial\":\"q0\""));
        assertTrue(json.contains("\"origen\":\"q0\""));
    }

    @Test
    void importarAutomata_deberiaRecuperarEstructuraOriginal(@TempDir Path tempDir) throws IOException {
        FileManager fileManager = new FileManager();
        Automata automataOriginal = crearAutomataEjemploAfd();
        Path rutaArchivo = tempDir.resolve("automata-roundtrip.json");

        fileManager.exportarAutomata(automataOriginal, rutaArchivo.toString());
        Automata automataImportado = fileManager.importarAutomata(rutaArchivo.toString());

        assertNotNull(automataImportado);
        assertEquals(TipoAutomata.AFD, automataImportado.getTipo());
        assertEquals(new HashSet<>(Set.of("a", "b")), new HashSet<>(automataImportado.getAlfabeto()));
        assertEquals("q0", automataImportado.getEstadoInicial().getNombre());
        assertEquals(new HashSet<>(Set.of("q0", "q1")), nombresEstados(automataImportado.getEstados()));
        assertEquals(new HashSet<>(Set.of("q1")), nombresEstados(automataImportado.getEstadosAceptacion()));

        Map<String, Set<String>> transicionesEsperadas = new HashMap<>();
        transicionesEsperadas.put("q0|a", Set.of("q1"));
        transicionesEsperadas.put("q0|b", Set.of("q0"));
        transicionesEsperadas.put("q1|a", Set.of("q1"));
        transicionesEsperadas.put("q1|b", Set.of("q0"));

        Map<String, Set<String>> transicionesImportadas = new HashMap<>();
        for (Transicion transicion : automataImportado.getFuncionTransicion()) {
            String clave = transicion.getEstadoOrigen().getNombre() + "|" + transicion.getSimbolo();
            transicionesImportadas.put(clave, nombresEstados(transicion.getEstadoDestino()));
        }

        assertEquals(transicionesEsperadas, transicionesImportadas);
    }

    private Automata crearAutomataEjemploAfd() {
        List<String> alfabeto = new ArrayList<>(List.of("a", "b"));

        Estado q0 = new Estado("q0", TipoEstado.INITIAL);
        Estado q1 = new Estado("q1", TipoEstado.FINAL);

        List<Estado> estados = new ArrayList<>(List.of(q0, q1));
        List<Estado> estadosAceptacion = new ArrayList<>(List.of(q1));

        Automata automata = new Automata(TipoAutomata.AFD, alfabeto, q0, estados, estadosAceptacion);

        Transicion t1 = new Transicion(q0, "a");
        t1.setEstadoDestino(new ArrayList<>(List.of(q1)));

        Transicion t2 = new Transicion(q0, "b");
        t2.setEstadoDestino(new ArrayList<>(List.of(q0)));

        Transicion t3 = new Transicion(q1, "a");
        t3.setEstadoDestino(new ArrayList<>(List.of(q1)));

        Transicion t4 = new Transicion(q1, "b");
        t4.setEstadoDestino(new ArrayList<>(List.of(q0)));

        automata.setFuncionTransicion(new ArrayList<>(List.of(t1, t2, t3, t4)));
        return automata;
    }

    private Set<String> nombresEstados(List<Estado> estados) {
        return estados.stream().map(Estado::getNombre).collect(java.util.stream.Collectors.toSet());
    }
}
