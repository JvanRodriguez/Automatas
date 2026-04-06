package co.edu.uptc.formales.automatas.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.DTO.DTOs.TransicionDTO;
import co.edu.uptc.formales.automatas.model.TipoAutomata;

public class View implements IView {
    private final Scanner scanner = new Scanner(System.in);

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
    public void mostrarTrazabilidad(Map<String, String> resultadosTrazabilidad) {
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
        List<String> lista = new ArrayList<>();
        mostrarMensaje(" ", "INFO");
        mostrarMensaje("Ingrese una por una las cadenas a evaluar (o ingrese fin para terminar):", "INFO");
        while (true) {
            String input = entradaString();
            if (input.equalsIgnoreCase("fin")) {
                break;
            }
            if (!input.trim().isEmpty()) {
                lista.add(input);
            }
        }
        return lista;
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
                if (estadosDisponibles.contains(estado)) {
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
        //mostrarMensaje(estado + "-" + simbolo + "->", "INFO");
        System.out.print(estado + " -" + simbolo + "-> ");
        String estadoDestino = entradaString();
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
        Set<String> alfabetoSet = new HashSet<>();
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
            } else if (simbolo.length() != 1) {
                mostrarMensaje("El simbolo debe ser un solo caracter", "ERROR");
            } else if (!alfabetoSet.add(simbolo)) {
                mostrarMensaje("El simbolo '" + simbolo + "' ya fue ingresado", "ERROR");
            } else {
                mostrarMensaje("Simbolo agregado. Alfabeto actual: " + alfabetoSet, "INFO");
            }
        }

        return new ArrayList<>(alfabetoSet);
    }

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
        Set<String> estadosSet = new HashSet<>();
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

        return new ArrayList<>(estadosSet);
    }

    @Override
    public List<TransicionDTO> getTransiciones(List<String> estados, List<String> simbolos) {
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
