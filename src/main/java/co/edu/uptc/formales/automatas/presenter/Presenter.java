package co.edu.uptc.formales.automatas.presenter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.model.Automata;
import co.edu.uptc.formales.automatas.model.Estado;
import co.edu.uptc.formales.automatas.model.IModel;
import co.edu.uptc.formales.automatas.model.Simulacion;
import co.edu.uptc.formales.automatas.model.TipoAutomata;
import co.edu.uptc.formales.automatas.model.TipoEstado;
import co.edu.uptc.formales.automatas.model.Transicion;
import co.edu.uptc.formales.automatas.view.IView;
import co.edu.uptc.formales.automatas.view.View;

public class Presenter implements IPresenter{
    private final IModel simulacion;
    private final IView view;

    public Presenter(){
        this.simulacion = new Simulacion();
        this.view = new View();
    }

    public void inicioMenu(){
        String option = "";
        String subOption = "";
        String importOption = "";
        view.showMessage("Bienvenido a la Simulación de Automatas");
        while (!option.equals("0")) {
            showMenu();
            option = view.entradaString();
            if(option.equals("1")){
                crearAutomata();
                while(!subOption.equals("0")){
                    showSubmenu();
                    subOption = view.entradaString();
                    if(subOption.equals("1")){
                        exportarAutomata();
                    }else if(subOption.equals("2")){
                        evaluarCadenasyGenerarTrazabilidad();
                    }else if(!subOption.equals("0")){
                        view.showMessage("Opción inválida, intente nuevamente");
                    }
                }
            }else if(option.equals("2")){
                importarAutomata();
                while (!importOption.equals("0")) {
                    showMenuImportado();
                    importOption = view.entradaString();
                    if (importOption.equals("1")) {
                        evaluarCadenasyGenerarTrazabilidad();
                    }else if(!importOption.equals("0")){
                        view.showMessage("Opción inválida, intente nuevamente");
                    }
                }
                importOption = "";
            }else if(!option.equals("0")){
                view.showMessage("Opción inválida, intente nuevamente");
            }
        }
    }

    private void evaluarCadenasyGenerarTrazabilidad(){
        List<String> listaCadenas = view.getCadenasPruebas();
        evaluarLotes(listaCadenas);
        generarTrazabilidad(listaCadenas);

    }

    private void generarTrazabilidad(List<String> listaCadenas) {
        LinkedHashMap<String, String> resultadosTrazabilidad = new LinkedHashMap<>();
        for (String string : listaCadenas) {
            String trazabildiad = simulacion.obtenerTrazabilidad(string);
            resultadosTrazabilidad.put(string, trazabildiad);
        }
        view.mostrarTrazabilidad(resultadosTrazabilidad);
    }

    private void evaluarLotes(List<String> listaCadenas) {
        HashMap<String, Boolean> resultadosLotes = simulacion.evaluarCadenasPrueba(listaCadenas);
        view.mostrarResultadosPrueba(resultadosLotes);
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
        transicionesBase.sort(Comparator.comparing(DTOs.TransicionDTO::origen));
        view.showMessage(" ");
        view.showMessage("          --> TRANSICIONES <--");
        view.showMessage("Ingrese el estado destino para las siguientes transiciones:");
        view.showMessage(" ");
        for (DTOs.TransicionDTO transicionDTO : transicionesBase) {
            DTOs.TransicionDTO transicionCompleta = view.getTransicion(transicionDTO.origen(), transicionDTO.simbolo());
            simulacion.completarTransicion(transicionCompleta);
        }
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
        List<String> alfabeto = view.getAlfabeto();
        List<String> estados = view.getEstados();
        String estadoInicialV = view.getEstadoInicial(estados);
        List<String> estadosAceptacionV = view.getEstadosAceptacion(estados);
        List<Estado> estadosModel = crearEstados(estados);
        TipoAutomata tipo = TipoAutomata.AFD; //Por el momento solo se maneja AFD, pero se puede extender para manejar AFN;
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
        view.showMessage("TRANSICIONES");
        for (Transicion transicion : transiciones) {
            DTOs.TransicionDTO DTOshow = transicion.toDTO();
            view.showMessage(DTOshow.origen() + " -" + DTOshow.simbolo() + "-> " + DTOshow.destino());
        }
        view.showMessage("]");
    }

    private String estadosToString(List<Estado> estados){
        return estados.stream()
                .map(Estado::getNombre)
                .collect(Collectors.joining(", "));
    }

    private String toString(List<String> lista){
        return String.join(", ", lista);
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
    @Override
    public void crearFuncionTransicionAll() {
        //obtener estados de transiciones base como strings para la vista
        List<String> estados = simulacion.getTransicionesBase().stream().
                map(DTOs.TransicionDTO::origen).
                collect(Collectors.toList());
        //obtener simbolos de transiciones base como strings para la vista
        List<String> simbolos = simulacion.getTransicionesBase().stream().
                map(DTOs.TransicionDTO::simbolo).
                collect(Collectors.toList());
        List<DTOs.TransicionDTO> aux = view.getTransiciones(estados,simbolos);
        for (DTOs.TransicionDTO transicion : aux) {
            simulacion.completarTransicion(transicion);
        }

    }

    @Override
    public void exportarAutomata() {
        String ruta = view.getRutaExportar();
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
        LinkedHashMap<String, String> resultadosTrazabilidad = new LinkedHashMap<>();
        for (String string : cadenaTrazabilidad) {
            String trazabilidad = simulacion.obtenerTrazabilidad(string);
            resultadosTrazabilidad.put(string, trazabilidad);
        }
        view.mostrarTrazabilidad(resultadosTrazabilidad);
    }

}