package co.edu.uptc.formales.automatas.view;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import co.edu.uptc.formales.automatas.DTO.DTOs;
import co.edu.uptc.formales.automatas.model.TipoAutomata;

public class View extends JFrame implements IView {

    private ActionListener actionListener;
    private JPanel contenido;
    private CrearAutomata crearAutomata;
    private CrearTransiciones crearTransiciones;

    public View(ActionListener actionListener) {
        this.actionListener = actionListener;
        this.contenido = new JPanel();
        inicializar();
        setVisible(true);
    }
    private void inicializar() {
        setTitle("Simulador de Autómatas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        contenido.setLayout(new BorderLayout());
        add(contenido, BorderLayout.CENTER);
        this.menuPrincipal();
    }

    private void menuPrincipal() {
        MenuPrincipal menu = new MenuPrincipal(actionListener);
        this.cambiarPanel(menu);
    }

    private void cambiarPanel(JPanel nuevoPanel) {
        contenido.removeAll();
        contenido.add(nuevoPanel, BorderLayout.CENTER);
        contenido.revalidate();
        contenido.repaint();
    }

    @Override
    public void mostrarAnadirTransicion(List<DTOs.TransicionDTO> dtoTransiciones){
        crearTransiciones = new CrearTransiciones(dtoTransiciones, actionListener);
        cambiarPanel(crearTransiciones);
    }

    @Override
    public void mostrarCrearAutomata() {
        crearAutomata = new CrearAutomata(this.actionListener);
        cambiarPanel(crearAutomata);
    }

    @Override
    public void mostrarTrazabilidad(Map<String, String> resultadosTrazabilidad) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mostrarResultadosPrueba(Map<String, Boolean> resultados) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getCadenasPruebas() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getEstadosAceptacion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DTOs.TransicionDTO getTransicion(String estado, String simbolo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getEstadoInicial() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getAlfabeto() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TipoAutomata getTipoAutomata() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getEstados() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<DTOs.TransicionDTO> getTransiciones(List<String> estados, List<String> simbolos) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getCadenaPrueba() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mostrarMensaje(String mensaje, String tipo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRutaExportar() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRutaImportar() {
        // Configurar idioma español para el JFileChooser
        Locale.setDefault(new Locale("es", "ES"));
        UIManager.put("FileChooser.lookInLabelText", "Buscar en:");
        UIManager.put("FileChooser.saveInLabelText", "Guardar en:");
        UIManager.put("FileChooser.fileNameLabelText", "Nombre del archivo:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Tipo de archivo:");
        UIManager.put("FileChooser.cancelButtonText", "Cancelar");
        UIManager.put("FileChooser.openButtonText", "Abrir");
        UIManager.put("FileChooser.saveButtonText", "Guardar");
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Importar Autómata");
        
        // Establecer la carpeta del proyecto como directorio inicial
        File directorioProyecto = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(directorioProyecto);
        
        // Filtrar solo archivos JSON
        FileNameExtensionFilter filtroJSON = new FileNameExtensionFilter("Archivo JSON (*.json)", "json");
        fileChooser.setFileFilter(filtroJSON);
        fileChooser.setAcceptAllFileFilterUsed(false);
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        }
        
        return null;
    }


    
}
