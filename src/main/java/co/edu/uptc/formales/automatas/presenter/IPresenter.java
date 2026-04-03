package co.edu.uptc.formales.automatas.presenter;

public interface IPresenter {
    public void crearAutomata();
    public void guardarEstados();
    public void guardarAlfabeto();
    public void seleccionarEstadoInicial();
    public void seleccionarEstadosAceptacion();
    public void crearFuncionTransicion();
    public void exportarAutomata();
    public void importarAutomata();
    public void evaluarCadenas();
    public String ObtenerTrazabilidad(String cadena);
}
