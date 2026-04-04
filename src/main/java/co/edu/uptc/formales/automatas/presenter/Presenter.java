package co.edu.uptc.formales.automatas.presenter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.model.Estado;
import co.edu.uptc.formales.automatas.model.IModel;
import co.edu.uptc.formales.automatas.model.Simulacion;
import co.edu.uptc.formales.automatas.model.TipoAutomata;
import co.edu.uptc.formales.automatas.model.TipoEstado;
import co.edu.uptc.formales.automatas.view.IView;
import co.edu.uptc.formales.automatas.view.View;

public class Presenter implements IPresenter, ActionListener{
    
    private IModel simulacion;
    private IView view;

    public Presenter() {
        this.simulacion = new Simulacion();
        this.view = new View(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()){
            case "CREAR_AUTOMATA":
                view.mostrarCrearAutomata();
                crearAutomata();
                break;
            case "CARGAR_AUTOMATA":
                importarAutomata();
                break;
            case "ANADIR_FUNCION_TRANSICION":
                //view.mostrarAnadirTransicion();
                crearFuncionTransicionAll();
                break;
            default:
                break;
        }

    }

    @Override
    public void crearAutomata() {
        List<String> alfabeto = obtenerAlfabeto();
        List<String> estados = obtenerEstados();
        String estadoInicialV = obtenerEstadoInicial();
        List<String> estadosAceptacionV = obtenerEstadosAceptacion();
        List<Estado> estadosModel = crearEstados(estados);
        TipoAutomata tipo = obtenerTipoAutomata();
        Estado estadoInicial = asignarEstadoInicial(estadosModel, estadoInicialV);
        List<Estado> estadosAceptacion = asignarEstadosFinales(estadosModel, estadosAceptacionV);
        simulacion.crearAutomata(estadosModel, alfabeto, estadoInicial, estadosAceptacion, tipo);
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

    private TipoAutomata obtenerTipoAutomata(){
        TipoAutomata tipo = view.getTipoAutomata();
        return tipo;
    }

    public List<String> obtenerEstados() {
        List<String> estadosV = view.getEstados();
        return estadosV;
    }

    public List<String> obtenerAlfabeto() {
        List<String> alfabetoV = view.getAlfabeto();
        return alfabetoV;
    }

    public String obtenerEstadoInicial() {
        String estadoInicialV = view.getEstadoInicial();
        return estadoInicialV;
    }

    public List<String> obtenerEstadosAceptacion() {
        List<String> estadosApectacionV = view.getEstadosAceptacion();
        return estadosApectacionV;
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
        //view.mostrarAnadirTransicion(aux);
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
        HashMap<String, String> resultadosTrazabilidad = new HashMap<>();
        for (String string : cadenaTrazabilidad) {
            String trazabilidad = simulacion.obtenerTrazabilidad(string);
            resultadosTrazabilidad.put(string, trazabilidad);
        }
        view.mostrarTrazabilidad(resultadosTrazabilidad);
    }

}