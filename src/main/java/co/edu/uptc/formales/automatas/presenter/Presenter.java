package co.edu.uptc.formales.automatas.presenter;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.model.Simulacion;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Presenter implements IPresenter{

    private Simulacion simulacion;

    @Override
    public void crearAutomata() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'crearAutomata'");
    }

    @Override
    public void guardarEstados() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardarEstados'");
    }

    @Override
    public void guardarAlfabeto() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'guardarAlfabeto'");
    }

    @Override
    public void seleccionarEstadoInicial() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'seleccionarEstadoInicial'");
    }

    @Override
    public void seleccionarEstadosAceptacion() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'seleccionarEstadosAceptacion'");
    }

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
            DTOs.TransicionDTO aux = view.getTransicion(estado,simbolo);
            simulacion.completarTransicion(aux);

        }

    }

    public void crearFuncionTransicion() {
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
            List<DTOs.TransicionDTO> aux = view.getTransiciones(estados,simbolos);
            for(DTOs.TransicionDTO transicion: aux){
                simulacion.completarTransicion(transicion);
            }

        }

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
        //metodo en vista que pide ingresar cadena
        //Metodo para obtener cadena/s ingresadas
        Map<String,Boolean> resultados = simulacion.evaluarCadenasPrueba(view.getCadenasPruebas());
        view.mostrarResultadosPrueba(resultados);

    }

    @Override
    public String ObtenerTrazabilidad(String cadena) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ObtenerTrazabilidad'");
    }

}