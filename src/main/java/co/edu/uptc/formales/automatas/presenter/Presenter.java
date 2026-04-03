package co.edu.uptc.formales.automatas.presenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.edu.uptc.formales.automatas.model.Estado;
import co.edu.uptc.formales.automatas.model.IModel;
import co.edu.uptc.formales.automatas.model.Simulacion;
import co.edu.uptc.formales.automatas.model.TipoAutomata;
import co.edu.uptc.formales.automatas.model.TipoEstado;
import co.edu.uptc.formales.automatas.view.IView;
import co.edu.uptc.formales.automatas.view.View;

public class Presenter implements IPresenter{
    
    private IModel simulacion;
    private IView view;

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

    @Override
    public void crearFuncionTransicion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearFuncionTransicion'");
    }

    @Override
    public void exportarAutomata() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'exportarAutomata'");
    }

    @Override
    public void importarAutomata() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'importarAutomata'");
    }

    @Override
    public void evaluarCadenas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'evaluarCadenas'");
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