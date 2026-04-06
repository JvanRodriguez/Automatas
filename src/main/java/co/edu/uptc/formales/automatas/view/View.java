package co.edu.uptc.formales.automatas.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.DTO.DTOs.TransicionDTO;
import co.edu.uptc.formales.automatas.model.TipoAutomata;

public class View implements IView {
    private final Scanner scanner = new Scanner(System.in);
    private List<String> estadosDisponibles = new ArrayList<>();
    private String estadoInicialSeleccionado = "";

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showMessageNL(String message) {
        System.out.print(message);
    }

    public void showInt(int data){
        System.out.println(data);
    }

    @Override
    public String entradaString() {
        return scanner.nextLine();
    }

    @Override
    public int entradaInt() {
        String entrada = scanner.nextLine();
        return Integer.parseInt(entrada.trim());
    }

    @Override
    public void mostrarTrazabilidad(LinkedHashMap<String, String> resultadosTrazabilidad) {
        mostrarMensaje(" ", "INFO");
        mostrarMensaje("          --> TRAZABILIDAD <--", "INFO");
        resultadosTrazabilidad.forEach((cadena, trazabilidad) ->
            mostrarMensaje("Cadena: " + cadena + " || Recorrido: " + trazabilidad, "INFO")
        );
    }

    @Override
    public void mostrarResultadosPrueba(Map<String, Boolean> resultados) {
        if (resultados == null || resultados.isEmpty()) {
            mostrarMensaje("No hay resultados para mostrar", "INFO");
            return;
        }

        resultados.forEach((cadena, aceptada) -> {
            String resultado = aceptada ? "ACEPTADA" : "RECHAZADA";
            mostrarMensaje("Cadena: \"" + cadena + "\" -> " + resultado, "INFO");
        });
    }

    @Override
    public List<String> getCadenasPruebas() {
        List<String> cadenas = new ArrayList<>();
        boolean continuar = true;
        
        showMessage("Ingrese las cadenas a evaluar (presione ENTER sin escribir para cadena vacía)");
        showMessage("Escriba 'fin' para terminar:");
        
        while (continuar) {
            String input = entradaString();
            
            if (input.equalsIgnoreCase("fin")) {
                if (cadenas.isEmpty()) {
                    showMessage("Debe ingresar al menos una cadena");
                } else {
                    continuar = false;
                }
            } else if (input.isEmpty()) {
                cadenas.add("");
                showMessage("Cadena vacía agregada. Actuales: " + cadenas);
            } else {
                cadenas.add(input);
                showMessage("Cadena \"" + input + "\" agregada. Actuales: " + cadenas);
            }
        }
        
        return cadenas;
    }

    @Override
    public List<String> getEstadosAceptacion(List<String> estadosDisponibles) {
        String estadosString = String.join(", ", estadosDisponibles);
        List<String> aceptacion = new ArrayList<>();
        boolean valido = false;

        mostrarMensaje(" ", "INFO");
        mostrarMensaje("          --> ESTADO(S) DE ACEPTACION <--", "INFO");
        while (!valido) {
            mostrarMensaje("Ingrese los estados de aceptacion separados por comas", "INFO");
            mostrarMensaje("Estados disponibles: " + estadosString, "INFO");
            String input = entradaString();

            String[] partes = input.split(",");
            List<String> temp = new ArrayList<>();
            boolean todosValidos = true;

            for (String parte : partes) {
                String estado = parte.trim();
                if (estado.equals(this.estadoInicialSeleccionado)) {
                    mostrarMensaje("El estado inicial (" + estado + ") no puede ser de aceptacion", "ERROR");
                    todosValidos = false;
                    break;
                } else if (estadosDisponibles.contains(estado)) {
                    if (!temp.contains(estado)) {
                        temp.add(estado);
                    }
                } else {
                    mostrarMensaje("Estado invalido: " + estado, "ERROR");
                    todosValidos = false;
                    break;
                }
            }

            if (todosValidos && !temp.isEmpty()) {
                aceptacion = temp;
                valido = true;
            } else if (temp.isEmpty()) {
                mostrarMensaje("Debe ingresar al menos un estado de aceptacion", "ERROR");
            }
        }

        return aceptacion;
    }

    @Override
    public TransicionDTO getTransicion(String estado, String simbolo) {
        String estadoDestino = "";
        while (true) {
            System.out.print(estado + " -" + simbolo + "-> ");
            estadoDestino = entradaString();
            if (estadoDestino.trim().isEmpty()) {
                mostrarMensaje("Debe ingresar un estado valido", "ERROR");
                continue;
            }
            
            List<String> destinos = dividirPorComas(estadoDestino);
            boolean todosValidos = true;
            for (String destino : destinos) {
                if (!this.estadosDisponibles.contains(destino)) {
                    mostrarMensaje("El estado de destino '" + destino + "' no pertenece al automata", "ERROR");
                    todosValidos = false;
                    break;
                }
            }
            
            if (todosValidos) {
                break;
            }
        }
        List<String> destinos = dividirPorComas(estadoDestino);
        return new DTOs.TransicionDTO(estado, simbolo, destinos);
    }

    @Override
    public String getEstadoInicial(List<String> estadosDisponibles) {
        String estadosString = String.join(", ", estadosDisponibles);
        boolean valido = false;
        String estadoInicial = "";

        mostrarMensaje(" ", "INFO");
        mostrarMensaje("          --> ESTADO INICIAL <--", "INFO");
        while (!valido) {
            mostrarMensaje("Ingrese uno de los siguientes estados como estado inicial: " + estadosString, "INFO");
            estadoInicial = entradaString();
            if (estadosDisponibles.contains(estadoInicial)) {
                valido = true;
                this.estadoInicialSeleccionado = estadoInicial;
            } else {
                mostrarMensaje("Estado invalido, ingrese uno de los estados mostrados", "ERROR");
            }
        }
        return estadoInicial;
    }

    @Override
    public List<String> getAlfabeto() {
        mostrarMensaje(" ", "INFO");
        mostrarMensaje("          --> ALFABETO <--", "INFO");
        Set<String> alfabetoSet = new LinkedHashSet<>();
        boolean continuar = true;

        while (continuar) {
            mostrarMensaje("Ingrese uno por uno los simbolos para el alfabeto (o 'fin' para terminar):", "INFO");
            String simbolo = entradaString();

            if (simbolo.equalsIgnoreCase("fin")) {
                if (alfabetoSet.isEmpty()) {
                    mostrarMensaje("Debe ingresar al menos un simbolo", "ERROR");
                } else {
                    continuar = false;
                }
            } else if (!alfabetoSet.add(simbolo)) {
                mostrarMensaje("El simbolo '" + simbolo + "' ya fue ingresado", "ERROR");
            } else {
                mostrarMensaje("Simbolo agregado. Alfabeto actual: " + alfabetoSet, "INFO");
            }
        }

        return new ArrayList<>(alfabetoSet);
    }

    //En caso de que quiera manejarel tipo de automata finito no determinista (AFN) o el tipo de automata finito determinista (AFD)
    @Override
    public TipoAutomata getTipoAutomata() {
        mostrarMensaje(" ", "INFO");
        mostrarMensaje("          --> TIPO DE AUTOMATA <--", "INFO");
        while (true) {
            mostrarMensaje("Seleccione uno de los siguientes tipos de automata:", "INFO");
            mostrarMensaje("1. Automata Finito Determinista (AFD)", "INFO");
            mostrarMensaje("2. Automata Finito NO Determinista (AFN)", "INFO");
            try {
                int tipoNum = entradaInt();
                if (tipoNum == 1) {
                    return TipoAutomata.AFD;
                }
                if (tipoNum == 2) {
                    return TipoAutomata.AFN;
                }
                mostrarMensaje("Seleccion invalida, ingrese una opcion mostrada (1 o 2)", "ERROR");
            } catch (NumberFormatException e) {
                mostrarMensaje("Debe ingresar un numero valido", "ERROR");
            }
        }
    }

    @Override
    public List<String> getEstados() {
        Set<String> estadosSet = new LinkedHashSet<>();
        boolean continuar = true;

        mostrarMensaje(" ", "INFO");
        mostrarMensaje("          --> ESTADOS <--", "INFO");
        while (continuar) {
            mostrarMensaje("Ingrese uno por uno el nombre de los estados (o 'fin' para terminar):", "INFO");
            String estado = entradaString();

            if (estado.equalsIgnoreCase("fin")) {
                if (estadosSet.isEmpty()) {
                    mostrarMensaje("Debe ingresar al menos un estado", "ERROR");
                } else {
                    continuar = false;
                }
            } else if (estado.trim().isEmpty()) {
                mostrarMensaje("El nombre del estado no puede estar vacio", "ERROR");
            } else if (!estadosSet.add(estado)) {
                mostrarMensaje("El estado '" + estado + "' ya fue ingresado", "ERROR");
            } else {
                mostrarMensaje("Estado agregado. Estados actuales: " + estadosSet, "INFO");
            }
        }

        this.estadosDisponibles = new ArrayList<>(estadosSet);
        return this.estadosDisponibles;
    }

    @Override
    public List<TransicionDTO> getTransiciones(List<String> estados, List<String> simbolos) {
        this.estadosDisponibles = new ArrayList<>(estados);
        List<TransicionDTO> transiciones = new ArrayList<>();
        for (String estado : estados) {
            for (String simbolo : simbolos) {
                transiciones.add(getTransicion(estado, simbolo));
            }
        }
        return transiciones;
    }

    @Override
    public List<String> getCadenaPrueba() {
        return getCadenasPruebas();
    }

    @Override
    public void mostrarMensaje(String mensaje, String tipo) {
        if (tipo == null || tipo.isBlank() || "INFO".equalsIgnoreCase(tipo)) {
            System.out.println(mensaje);
            return;
        }
        System.out.println("[" + tipo + "] " + mensaje);
    }

    @Override
    public String getRutaExportar() {
        mostrarMensaje(" ", "INFO");
        mostrarMensaje("          --> EXPORTAR AUTOMATA <--", "INFO");
        mostrarMensaje("Ingrese la ruta donde se guardara el archivo JSON", "INFO");
        return entradaString();
    }

    @Override
    public String getRutaImportar() {
        mostrarMensaje(" ", "INFO");
        mostrarMensaje("          --> IMPORTAR AUTOMATA <--", "INFO");
        mostrarMensaje("Ingrese la ruta del archivo JSON a importar", "INFO");
        return entradaString();
    }

    private List<String> dividirPorComas(String texto) {
        if (texto == null || texto.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(texto.split(","))
                .map(String::trim)
                .filter(valor -> !valor.isEmpty())
                .toList();
    }
}
