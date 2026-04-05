package co.edu.uptc.formales.automatas.presenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.DTO.DTOs.TransicionDTO;
import co.edu.uptc.formales.automatas.model.Automata;
import co.edu.uptc.formales.automatas.model.Estado;
import co.edu.uptc.formales.automatas.model.IModel;
import co.edu.uptc.formales.automatas.model.Simulacion;
import co.edu.uptc.formales.automatas.model.TipoAutomata;
import co.edu.uptc.formales.automatas.model.TipoEstado;
import co.edu.uptc.formales.automatas.model.Transicion;
import co.edu.uptc.formales.automatas.persistence.FileManager;
import co.edu.uptc.formales.automatas.view.IView;
import co.edu.uptc.formales.automatas.view.View;

public class Presenter implements IPresenter{
    private IModel simulacion;
    private View view;

    public Presenter(){
        this.simulacion = new Simulacion();
        this.view = new View();
    }

    public void inicioMenu(){
        int option = -1;
        int subOption = -1;
        int importOption = -1;
        view.showMessage("Bienvenido a la Simulación de Automatas");
        while (option!=0) {
            showMenu();
            option = view.entradaInt();
            if(option == 1){
                crearAutomata();
                while(subOption!=0){
                    showSubmenu();
                    subOption = view.entradaInt();
                    if(subOption == 1){
                        view.showMessage(" ");
                        view.showMessage("          --> EXPORTAR AUTÓMATA <--");
                        view.showMessage("Ingrese la ruta donde se guardará el archivo");
                        simulacion.exportarAutomata(view.entradaString());
                    }else if(subOption == 2){
                        evaluarCadenasyGenerarTrazabilidad();
                    }
                }
            }else if(option == 2){
                view.showMessage(" ");
                view.showMessage("          --> IMPORTAR AUTÓMATA <--");
                view.showMessage("Ingrese la ruta del archivo JSON a importar");
                simulacion.importarAutomata(view.entradaString());
                while (importOption!=0) {
                    showMenuImportado();
                    importOption = view.entradaInt();
                    if (importOption == 1) {
                        evaluarCadenasyGenerarTrazabilidad();
                    }
                }

            }
        }
    }

    private void evaluarCadenasyGenerarTrazabilidad(){
        List<String> listaCadenas = recibirCadenas();
        evaluarLotes(listaCadenas);
        generarTrazabilidad(listaCadenas);

    }

    private void generarTrazabilidad(List<String> listaCadenas) {
        view.showMessage(" ");
        view.showMessage("          --> TRAZABILIDAD <--");
        HashMap<String, String> resultadosTrazabilidad = new HashMap<>();
        for (String string : listaCadenas) {
            String trazabildiad = simulacion.obtenerTrazabilidad(string);
            resultadosTrazabilidad.put(string, trazabildiad);
        }
        mostrarTrazabilidad(resultadosTrazabilidad);
    }

    private void mostrarTrazabilidad(HashMap<String,String> resultadosTrazabilidad) {
        resultadosTrazabilidad.forEach((cadena, trazabilidad) -> {
            view.showMessage("Cadena: " + cadena + " || Recorrido: " + trazabilidad);
        });
    }

    private void evaluarLotes(List<String> listaCadenas) {
        HashMap<String, Boolean> resultadosLotes = simulacion.evaluarCadenasPrueba(listaCadenas);
        mostrarResultadosLotes(resultadosLotes);
    }

    private void mostrarResultadosLotes(HashMap<String, Boolean> resultadosLotes) {
        if (resultadosLotes == null || resultadosLotes.isEmpty()) {
            view.showMessage("No hay resultados para mostrar");
            return;
        }
        
        resultadosLotes.forEach((cadena, aceptada) -> {
            String resultado = aceptada ? "ACEPTADA" : "RECHAZADA";
            view.showMessage("Cadena: \"" + cadena + "\" → " + resultado);
        });
    }

    private List<String> recibirCadenas(){
        List<String> lista = new ArrayList<>();
        view.showMessage(" ");
        view.showMessage("Ingrese una por una las cadenas a evaluar (o ingrese fin para terminar):");
        while (true) {
            String input = view.entradaString();
            if (input.equalsIgnoreCase("fin")) {
                break;
            }
            if (!input.trim().isEmpty()) {
                lista.add(input);
            }
        }
        return lista;
    }

    private void showSubmenu() {
        view.showMessage(" ");
        view.showMessage("          --> AUTOMATA ACTUAL <--");
        view.showMessage("Digite <<1>> para EXPORTAR archivo automata en formato JSON.");
        view.showMessage("Digite <<2>> para EVALUAR POR LOTES.");
        view.showMessage("Digite <<0>> para VOLVER al menú principal.");
    }

    private void showMenuImportado() {
        view.showMessage(" ");
        view.showMessage("          --> AUTOMATA ACTUAL <--");
        view.showMessage("Digite <<1>> para EVALUAR POR LOTES.");
        view.showMessage("Digite <<0>> para VOLVER al menú principal.");
    }

    private void rellenarTransiciones() {
        List<DTOs.TransicionDTO> transicionesBase = simulacion.getTransicionesBase();
        List<String> estadosValidos = simulacion.getAutomataDeseado().getEstados().stream()
                .map(Estado::getNombre)
                .collect(Collectors.toList());
        view.showMessage(" ");
        view.showMessage("          --> TRANSICIONES <--");
        view.showMessage("Estados válidos: " + String.join(", ", estadosValidos));
        view.showMessage(" ");
        for (DTOs.TransicionDTO transicionDTO : transicionesBase) {
            List<String> estadosParaDTO = null;
            boolean validado = false;
            while (!validado) {
                view.showMessageNL(transicionDTO.origen() + " -" + transicionDTO.simbolo() + "-> ");
                String estadoDestino = view.entradaString();
                if (estadoDestino.trim().isEmpty()) {
                    estadosParaDTO = new ArrayList<>();
                    validado = true;
                } else {
                    estadosParaDTO = dividirPorComas(estadoDestino);
                    List<String> invalidos = estadosParaDTO.stream()
                            .filter(e -> !estadosValidos.contains(e))
                            .collect(Collectors.toList());
                    if (invalidos.isEmpty()) {
                        validado = true;
                    } else {
                        view.showMessage("Error: Estado(s) inválido(s): " + String.join(", ", invalidos));
                        view.showMessage("Estados válidos: " + String.join(", ", estadosValidos));
                    }
                }
            }
            
            simulacion.completarTransicion(new TransicionDTO(
                transicionDTO.origen(), 
                transicionDTO.simbolo(), 
                estadosParaDTO
            ));
        }
        
        view.showMessage("✓ Todas las transiciones han sido completadas");
    }

    private void showMenu() {
        view.showMessage(" ");
        view.showMessage("          --> OPCIONES <--");
        view.showMessage("Digite <<1>> para CREAR un automata");
        view.showMessage("Digite <<2>> para CARGAR un automata");
        view.showMessage("Digite <<0>> para SALIR del programa");
    }

    @Override
    public void crearAutomata() {
        List<String> alfabeto = obtenerAlfabeto();
        List<String> estados = obtenerEstados();
        String estadoInicialV = obtenerEstadoInicial(estados);
        List<String> estadosAceptacionV = obtenerEstadosAceptacion(estados);
        List<Estado> estadosModel = crearEstados(estados);
        TipoAutomata tipo = obtenerTipoAutomata();
        Estado estadoInicial = asignarEstadoInicial(estadosModel, estadoInicialV);
        List<Estado> estadosAceptacion = asignarEstadosFinales(estadosModel, estadosAceptacionV);
        simulacion.crearAutomata(estadosModel, alfabeto, estadoInicial, estadosAceptacion, tipo);
        simulacion.crearFuncionTransicionBase(estadosModel, alfabeto);
        rellenarTransiciones();
        mostrarAutomataCreado();
    }

    private List<Estado> crearEstados(List<String> estados) {
        List<Estado> estadosModel = new ArrayList<>();
        for (String estadoNombre : estados) {
            Estado aux = new Estado(estadoNombre, TipoEstado.DEFAULT);
            estadosModel.add(aux);
        }
        return estadosModel;
    }

    private Estado asignarEstadoInicial(List<Estado> estadosModel, String estadoInicial) {
        for (Estado estado : estadosModel) {
            if(estado.getNombre().equals(estadoInicial)){
                estado.setTipo(TipoEstado.INITIAL);
                return estado;
            }
        }
        return null;
    }

    private List<Estado> asignarEstadosFinales(List<Estado> estadosModel, List<String> estadosAceptacion) {
        List<Estado> estadosAceptacionAux = new ArrayList<>();
        for (Estado estado : estadosModel) {
            if(estadosAceptacion.contains(estado.getNombre())){
                estado.setTipo(TipoEstado.FINAL);
                estadosAceptacionAux.add(estado);
            }
        }
        return estadosAceptacionAux;
    }

    private TipoAutomata obtenerTipoAutomata() {
        int tipoNum = 0;
        boolean valido = false;
        view.showMessage(" ");
        view.showMessage("          --> TIPO DE AUTOMATA <--");
        while (!valido) {
            view.showMessage("Seleccione uno de los siguientes tipos de autómata:");
            view.showMessage("1. Autómata Finito Determinista (AFD)");
            view.showMessage("2. Autómata Finito NO Determinista (AFN)");
            tipoNum = view.entradaInt();
            if (tipoNum == 1) {
                return TipoAutomata.AFD;
            } else if (tipoNum == 2) {
                return TipoAutomata.AFN;
            } else {
                view.showMessage("Selección inválida, ingrese una opción mostrada (1 o 2)");
            }
        }
        return null;
    }

    private List<String> obtenerEstados() {
        Set<String> estadosSet = new HashSet<>();
        boolean continuar = true;
        
        view.showMessage(" ");
        view.showMessage("          --> ESTADOS <--");
        while (continuar) {
            view.showMessage("Ingrese uno por uno el nombre de los estados (o 'fin' para terminar):");
            String estado = view.entradaString();
            
            if (estado.equalsIgnoreCase("fin")) {
                if (estadosSet.isEmpty()) {
                    view.showMessage("Debe ingresar al menos un estado");
                } else {
                    continuar = false;
                }
            } else if (estado.trim().isEmpty()) {
                view.showMessage("El nombre del estado no puede estar vacío");
            } else if (!estadosSet.add(estado)) {
                view.showMessage("El estado '" + estado + "' ya fue ingresado");
            } else {
                view.showMessage("Estado agregado. Estados actuales: " + estadosSet);
            }
        }
        
        return new ArrayList<>(estadosSet);
    }

    private List<String> obtenerAlfabeto() {
        view.showMessage(" ");
        view.showMessage("          --> ALFABETO <--");
        Set<String> alfabetoSet = new HashSet<>();
        boolean continuar = true;
        
        while (continuar) {
            view.showMessage("Ingrese uno por uno los símbolos para el alfabeto (o 'fin' para terminar):");
            String simbolo = view.entradaString();
            
            if (simbolo.equalsIgnoreCase("fin")) {
                if (alfabetoSet.isEmpty()) {
                    view.showMessage("Debe ingresar al menos un símbolo \n");
                } else {
                    continuar = false;
                }
            } else if (simbolo.length() != 1) {
                view.showMessage("El símbolo debe ser un solo carácter");
            } else if (!alfabetoSet.add(simbolo)) {
                view.showMessage("El símbolo '" + simbolo + "' ya fue ingresado");
            } else {
                view.showMessage("Símbolo agregado. Alfabeto actual: " + alfabetoSet);
            }
        }
        
        return new ArrayList<>(alfabetoSet);
    }

    private void mostrarAutomataCreado() {
        view.showMessage(" ");
        view.showMessage("          --> RESULTADO DE AUTOMATA <--");
        Automata automataActual = simulacion.getAutomataDeseado();
        String estados = estadosToString(automataActual.getEstados());
        String alfabeto = toString(automataActual.getAlfabeto());
        String estadoInicial = automataActual.getEstadoInicial().getNombre();
        String estadosAceptacion = estadosToString(automataActual.getEstadosAceptacion());
        view.showMessage("AUTOMATA[ Estados:{"+estados+"}  Alfabeto:{"+alfabeto+"}  Estado Inicial: "+ estadoInicial+"  Estado(s) de Aceptación: { "+estadosAceptacion+" }");
        mostrarTransiciones(automataActual);
    }

    private void mostrarTransiciones(Automata automata){
        List<Transicion> transiciones = automata.getFuncionTransicion();
        view.showMessage("TRANSICIONES:");
        for (Transicion transicion : transiciones) {
            DTOs.TransicionDTO DTOshow = transicion.toDTO();
            view.showMessageNL(DTOshow.origen() + " -" + DTOshow.simbolo() + "-> " + DTOshow.destino());
        }
        view.showMessageNL("]");
    }

    private String estadosToString(List<Estado> estados){
        return estados.stream()
                .map(Estado::getNombre)
                .collect(Collectors.joining(", "));
    }

    private String toString(List<String> lista){
        return String.join(", ", lista);
    }

    public List<String> dividirPorComas(String texto) {
        if (texto == null || texto.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(texto.split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());
    }

    public String obtenerEstadoInicial(List<String> estados) {
        String estadosString = String.join(", ", estados);
        String estadoInicial = "";
        boolean valido = false;
        
        view.showMessage(" ");
        view.showMessage("          --> ESTADO INICIAL <--");
        while (!valido) {
            view.showMessage("Ingrese uno de los siguientes estados como estado inicial: " + estadosString);
            estadoInicial = view.entradaString();
            
            if (estados.contains(estadoInicial)) {
                valido = true;
            } else {
                view.showMessage("Estado inválido, ingrese uno de los estados mostrados");
            }
        }
        return estadoInicial;
    }

    public List<String> obtenerEstadosAceptacion(List<String> estados) {
        String estadosString = String.join(", ", estados);
        List<String> aceptacion = new ArrayList<>();
        boolean valido = false;
        
        view.showMessage(" ");
        view.showMessage("          --> ESTADO(S) DE ACEPTACIÓN <--");
        while (!valido) {
            view.showMessage("Ingrese los estados de aceptación separados por comas\n" +
                            "Estados disponibles: " + estadosString);
            String input = view.entradaString();
            
            String[] partes = input.split(",");
            List<String> temp = new ArrayList<>();
            boolean todosValidos = true;
            
            for (String parte : partes) {
                String estado = parte.trim();
                if (estados.contains(estado)) {
                    if (!temp.contains(estado)) {
                        temp.add(estado);
                    }
                } else {
                    view.showMessage("Estado inválido: " + estado);
                    todosValidos = false;
                    break;
                }
            }
            
            if (todosValidos && !temp.isEmpty()) {
                aceptacion = temp;
                valido = true;
            } else if (temp.isEmpty()) {
                view.showMessage("Debe ingresar al menos un estado de aceptación");
            }
        }
        
        return aceptacion;
    }

    //El metodo pide uno por uno los destinos de las transiciones
    @Override
    public void crearFuncionTransicion() {
        //pedirle a la vista que muestre y llene las transicionesDTO incompletas
        for(DTOs.TransicionDTO tBase: simulacion.getTransicionesBase()){
            String estado = tBase.origen();
            String simbolo = tBase.simbolo();
            //El metodo en vista debe retornar una TransicionDTO completa!
            /*
                En vista usa el constructor DTOs.TransicionDTO completo
                DTOs.TransicionDTO aux = new DTOs.TransicionDTO(nombreEstadoOigen: String, simboloDeTransicion: String, estadosDestino: List<String>)
            */
           // se asume que getTransicion muestra y retorna
            DTOs.TransicionDTO aux = view.getTransicion(estado,simbolo);
            simulacion.completarTransicion(aux);

        }

    }

    //El metodo pide los destinos de las transiciones de una sola vez
    public void crearFuncionTransicionAll() {
        //obtener estados de transiciones base como strings para la vista
        List<String> estados = simulacion.getTransicionesBase().stream().
                map(DTOs.TransicionDTO::origen).
                collect(Collectors.toList());
        //obtener simbolos de transiciones base como strings para la vista
        List<String> simbolos = simulacion.getTransicionesBase().stream().
                map(DTOs.TransicionDTO::simbolo).
                collect(Collectors.toList());
        for(DTOs.TransicionDTO tBase: simulacion.getTransicionesBase()){
            String estado = tBase.origen();
            String simbolo = tBase.simbolo();
            //El metodo en vista debe retornar una TransicionDTO completa!
            /*
                En vista usa el constructor DTOs.TransicionDTO completo
                DTOs.TransicionDTO aux = new DTOs.TransicionDTO(nombreEstadoOigen: String, simboloDeTransicion: String, estadosDestino: List<String>)
            */
           // se asume que getTransiciones muestra y retorna a todas las transiciones
            List<DTOs.TransicionDTO> aux = view.getTransiciones(estados,simbolos);
            for(DTOs.TransicionDTO transicion: aux){
                simulacion.completarTransicion(transicion);
            }

        }

    }

    @Override
    public void exportarAutomata() {
        String ruta = view.entradaString();
        boolean exito = simulacion.exportarAutomata(ruta);
        if (exito) {
            view.mostrarMensaje("Automata exportado exitosamente a: " + ruta, "EXIT");
        } else {
            view.mostrarMensaje("Error al exportar el automata. Verifique la ruta e intente nuevamente.", "ERROR");
        }
    }

    @Override
    public void importarAutomata() {
        String ruta = view.getRutaImportar();
        boolean exito = simulacion.importarAutomata(ruta);
        if (exito) {
            view.mostrarMensaje("Automata importado exitosamente desde: " + ruta, "EXIT");
        } else {
            view.mostrarMensaje("Error al importar el automata. Verifique la ruta e intente nuevamente.", "ERROR");
        }
    }

    @Override
    public void evaluarCadenas() {
        //metodo en vista que pide ingresar cadena

        //Metodo para obtener cadena/s ingresadas
        Map<String,Boolean> resultados = simulacion.evaluarCadenasPrueba(view.getCadenasPruebas());
        view.mostrarResultadosPrueba(resultados);

    }

    @Override
    public void ObtenerTrazabilidad() {
        List<String> cadenaTrazabilidad = view.getCadenaPrueba();
        HashMap<String, String> resultadosTrazabilidad = new HashMap<>();
        for (String string : cadenaTrazabilidad) {
            String trazabilidad = simulacion.obtenerTrazabilidad(string);
            resultadosTrazabilidad.put(string, trazabilidad);
        }
        view.mostrarTrazabilidad(resultadosTrazabilidad);
    }

}