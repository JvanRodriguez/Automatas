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

/**
 * Presentador de la aplicación bajo el patrón MVP (Model-View-Presenter).
 * Actúa como intermediario entre la vista (IView) y el modelo (IModel/Simulacion).
 * Es responsable de controlar el flujo del programa, reaccionar a las acciones del usuario,
 * manipular los datos del autómata y actualizar la interfaz.
 */
public class Presenter implements IPresenter{
    private final IModel simulacion;
    private final IView view;

    /**
     * Constructor por defecto del presentador.
     * Inicializa las implementaciones de la simulación (modelo) y la consola (vista).
     */
    public Presenter(){
        this.simulacion = new Simulacion();
        this.view = new View();
    }

    /**
     * Inicia el menú principal de la aplicación, controlando
     * todo el flujo de ejecución (creación, importación y evaluación).
     */
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
                subOption = "";
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

    /**
     * Coordina la inserción de las cadenas de prueba, manda a evaluarlas
     * en lote y luego genera la trazabilidad para todas las cadenas, comunicando luego
     * los resultados de vuelta hacia la consola (vista).
     */
    private void evaluarCadenasyGenerarTrazabilidad(){
        List<String> listaCadenas = view.getCadenasPruebas();
        evaluarLotes(listaCadenas);
        generarTrazabilidad(listaCadenas);

    }

    /**
     * Manda a procesar el recorrido detallado o comprobación paso a paso de cada cadena 
     * usando el autómata y finalmente emite los datos hacia la vista.
     *
     * @param listaCadenas Lista de cadenas ingresadas para ser procesadas.
     */
    private void generarTrazabilidad(List<String> listaCadenas) {
        LinkedHashMap<String, String> resultadosTrazabilidad = new LinkedHashMap<>();
        for (String string : listaCadenas) {
            String trazabildiad = simulacion.obtenerTrazabilidad(string);
            resultadosTrazabilidad.put(string, trazabildiad);
        }
        view.mostrarTrazabilidad(resultadosTrazabilidad);
    }

    /**
     * Pide la simulación final y muestra cuáles de los lotes de cadenas
     * fueron aceptadas o rechazadas.
     *
     * @param listaCadenas Lista de cadenas a verificar.
     */
    private void evaluarLotes(List<String> listaCadenas) {
        HashMap<String, Boolean> resultadosLotes = simulacion.evaluarCadenasPrueba(listaCadenas);
        view.mostrarResultadosPrueba(resultadosLotes);
    }

    /**
     * Muestra el menú de administración y evaluación disponible 
     * después de haber creado desde cero un autómata y tenerlo en memoria.
     */
    private void showSubmenu() {
        view.showMessage(" ");
        view.showMessage("          --> AUTOMATA ACTUAL <--");
        view.showMessage("Digite <<1>> para EXPORTAR archivo automata en formato JSON.");
        view.showMessage("Digite <<2>> para EVALUAR POR LOTES.");
        view.showMessage("Digite <<0>> para VOLVER al menú principal.");
    }

    /**
     * Muestra el menú de alternativas disponibles tras haber
     * finalizado con éxito la importación por archivo JSON.
     */
    private void showMenuImportado() {
        view.showMessage(" ");
        view.showMessage("          --> AUTOMATA ACTUAL <--");
        view.showMessage("Digite <<1>> para EVALUAR POR LOTES.");
        view.showMessage("Digite <<0>> para VOLVER al menú principal.");
    }

    /**
     * Solicita secuencialmente a la vista los destinos de las transiciones
     * basados en un mapeo previo (`getTransicionesBase`), y completa su objeto.
     */
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

    /**
     * Muestra el menú raíz inicial de la ventana de aplicación.
     */
    private void showMenu() {
        view.showMessage(" ");
        view.showMessage("          --> OPCIONES <--");
        view.showMessage("Digite <<1>> para CREAR un automata");
        view.showMessage("Digite <<2>> para CARGAR un automata");
        view.showMessage("Digite <<0>> para SALIR del programa");
    }

    /**
     * Metodo centralizado para interactuar con la vista y extraer toda 
     * la definición del autómata, incluyendo alfabetos, estados, estado 
     * inicial, estados de aceptación, creación del modelo y su rellenado intermedio.
     */
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

    /**
     * Instancia arreglos de representaciones de Estados basados
     * en entradas desde string dadas por la vista, dándoles valor predeterminado.
     *
     * @param estados Lista con los nombres de todos los estados.
     * @return Lista parseada de modelos Estado instanciados.
     */
    private List<Estado> crearEstados(List<String> estados) {
        List<Estado> estadosModel = new ArrayList<>();
        for (String estadoNombre : estados) {
            Estado aux = new Estado(estadoNombre, TipoEstado.DEFAULT);
            estadosModel.add(aux);
        }
        return estadosModel;
    }

    /**
     * Mapea un estado específico en la lista para que actúe oficialmente como
     * el estado inicial del autómata, reemplazando su tipo a `INITIAL`.
     *
     * @param estadosModel Lista base de estados.
     * @param estadoInicial Nombre String del estado objetivo.
     * @return El propio modelo de Estado inicial una vez alterado. Null si no lo encuentra.
     */
    private Estado asignarEstadoInicial(List<Estado> estadosModel, String estadoInicial) {
        for (Estado estado : estadosModel) {
            if(estado.getNombre().equals(estadoInicial)){
                estado.setTipo(TipoEstado.INITIAL);
                return estado;
            }
        }
        return null;
    }

    /**
     * Recorre y asocia a múltiples estados con el atributo de `FINAL`
     * en función a su coincidencia de nombre con una lista cruda proveniente de la vista.
     *
     * @param estadosModel Lista instanciada de Estados en el dominio.
     * @param estadosAceptacion Conjunto de cadenas referenciándolos.
     * @return Sublista acotada que guarda y refleja el vector de estados de aceptación.
     */
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

    /**
     * Obtiene el producto final procesado desde la Simulación y le encarga
     * a la Vista que lo enseñe como texto plano (Alfabeto, Estados, Estados
     * Aceptación, Inicial y sus Transiciones mapeadas).
     */
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

    /**
     * Auxiliar utilitario que, dado un Objeto autómata, formatea por terminal 
     * todos sus pasos posibles de un punto en estado $x$ transitando por símbolo a $y$.
     *
     * @param automata Componente core con la data procesada.
     */
    private void mostrarTransiciones(Automata automata){
        List<Transicion> transiciones = automata.getFuncionTransicion();
        view.showMessage("TRANSICIONES");
        for (Transicion transicion : transiciones) {
            DTOs.TransicionDTO DTOshow = transicion.toDTO();
            view.showMessage(DTOshow.origen() + " -" + DTOshow.simbolo() + "-> " + DTOshow.destino());
        }
        view.showMessage("]");
    }

    /**
     * Covierte una lista de nodos Estado en una única cadena (nombres concatenados con ", ").
     * 
     * @param estados Lista original.
     * @return String con formato "Q0, Q1, Q2...".
     */
    private String estadosToString(List<Estado> estados){
        return estados.stream()
                .map(Estado::getNombre)
                .collect(Collectors.joining(", "));
    }

    /**
     * Envuelve una lista de hileras a String estándar y las une con coma.
     * 
     * @param lista String originarios.
     * @return Lista unida.
     */
    private String toString(List<String> lista){
        return String.join(", ", lista);
    }

    /**
     * El metodo pide uno por uno los destinos de las transiciones usando una función parcial de Vista
     * a manera interactiva y las completa en el core del simulador.
     */
    @Override
    public void crearFuncionTransicion() {
        // pedirle a la vista que muestre y llene las transicionesDTO incompletas
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

    /**
     * Alternativa a crearFuncionTransicion para recolectar y encajar todos los estados y
     * destinos en un listado a golpe de la vista. Realiza iteración total y le da todos
     * los datos finalizados a Simulator para armar el autómata.
     */
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

    /**
     * Interroga al usuario por una ruta de guardado mediante IView y procede
     * a delegar la tarea de serializar en un JSON e importarlo.
     */
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

    /**
     * Interroga al usuario por una ruta física o relativa para volcar hacia  
     * adentro (importar) una configuración y armar directamente el modelo de Automata.
     */
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

    /**
     * Manda a recolectar múltiples iteraciones de texto simple al usuario,
     * para enviarlas al simulador y probar cuáles coinciden como cadenas válidas y cuáles no.
     */
    @Override
    public void evaluarCadenas() {
        //metodo en vista que pide ingresar cadena

        //Metodo para obtener cadena/s ingresadas
        Map<String,Boolean> resultados = simulacion.evaluarCadenasPrueba(view.getCadenasPruebas());
        view.mostrarResultadosPrueba(resultados);

    }

    /**
     * Evalúa las cadenas enviadas por el usuario para poder escrutar,
     * estado a estado, dónde y cómo es procesada la derivación sintáctica.
     */
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