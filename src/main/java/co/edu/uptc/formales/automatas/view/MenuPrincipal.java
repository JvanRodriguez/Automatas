package co.edu.uptc.formales.automatas.view;

import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class MenuPrincipal extends JPanel {
    
    private ActionListener actionListener;

    public MenuPrincipal(ActionListener actionListener) {
        this.actionListener = actionListener;
        inicializar();
    }

    private void inicializar() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // Espacio superior
        this.add(Box.createVerticalGlue());
        
        // Título
        Texto titulo = new Texto("Simulador de Autómatas", "TITULO");
        titulo.setAlignmentX(CENTER_ALIGNMENT);
        this.add(titulo);
        
        // Separación entre título y subtítulo
        this.add(Box.createVerticalStrut(15));
        
        // Subtítulo
        Texto subtitulo = new Texto("Seleccione una opción para comenzar", "NORMAL");
        subtitulo.setAlignmentX(CENTER_ALIGNMENT);
        this.add(subtitulo);

        //this.add(Box.createVerticalGlue());
        this.add(Box.createVerticalStrut(30));
        this.añadirBotones();
        this.add(Box.createVerticalGlue());
    }

    private void añadirBotones() {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new BoxLayout(panelBotones, BoxLayout.Y_AXIS));
        panelBotones.setAlignmentX(CENTER_ALIGNMENT);
        
        Boton crearAutomata = new Boton("Crear Autómata", "CREAR_AUTOMATA", actionListener, 350, 100,  30);
        crearAutomata.setAlignmentX(CENTER_ALIGNMENT);
        panelBotones.add(crearAutomata);
        
        // Separación entre botones
        panelBotones.add(Box.createVerticalStrut(30));
        
        Boton cargarAutomata = new Boton("Cargar Autómata", "CARGAR_AUTOMATA", actionListener, 350, 100, 30);
        cargarAutomata.setAlignmentX(CENTER_ALIGNMENT);
        panelBotones.add(cargarAutomata);
        
        this.add(panelBotones);
    }
}
