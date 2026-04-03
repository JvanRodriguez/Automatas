package co.edu.uptc.formales.automatas.presenter;

public interface IPresenter {
    public void crearAutomata();
    //Pregunta una por una cada transicion
    public void crearFuncionTransicion();
    //pregunta por todos los destinos de todas las transiciones
    public void crearFuncionTransicionAll();
    public void exportarAutomata();
    public void importarAutomata();
    public void evaluarCadenas();
    public void ObtenerTrazabilidad();
}
