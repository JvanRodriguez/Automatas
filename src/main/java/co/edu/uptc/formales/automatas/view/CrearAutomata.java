package co.edu.uptc.formales.automatas.view;


import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class CrearAutomata extends JPanel {
    
    private JComboBox<String> comboTipo;
    private JTable tablaEstados;
    private DefaultTableModel modeloTabla;
    private ActionListener actionListener;
    
    public CrearAutomata(ActionListener actionListener) {
        this.actionListener = actionListener;
        inicializar();
    }
    
    private void inicializar() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Espacio inicial
        this.add(Box.createVerticalGlue());
        
        // Panel superior con descripción e instrucciones
        JPanel panelSuperior = crearPanelSuperior();
        this.add(panelSuperior);
        
        // Separación
        this.add(Box.createVerticalStrut(20));
        
        // Panel de botones AÑADIR ESTADO y ALFABETO
        JPanel panelBotonesAgregar = crearPanelBotonesAgregar();
        this.add(panelBotonesAgregar);
        
        // Separación
        this.add(Box.createVerticalStrut(20));
        
        // Tabla de estados
        JPanel panelTabla = crearPanelTabla();
        this.add(panelTabla);
        
        // Separación
        this.add(Box.createVerticalStrut(20));
        
        // Panel inferior con botón de transición
        JPanel panelInferior = crearPanelInferior();
        this.add(panelInferior);
        
        // Espacio final
        this.add(Box.createVerticalGlue());
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(CENTER_ALIGNMENT);
        
        // Texto descriptivo
        Texto labelDescripcion = new Texto("Ingrese los datos correspondientes de la quintupla del automata", "NORMAL");
        labelDescripcion.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(labelDescripcion);
        
        // Separación
        panel.add(Box.createVerticalStrut(15));
        
        // Tipo de autómata con su etiqueta
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        Texto labelTipo = new Texto("Tipo", "NORMAL");
        comboTipo = new JComboBox<>(new String[]{"AFD", "AFN"});
        panelTipo.add(labelTipo);
        panelTipo.add(comboTipo);
        panel.add(panelTipo);
        
        return panel;
    }
    
    private JPanel crearPanelBotonesAgregar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(CENTER_ALIGNMENT);
        
        panel.add(Box.createHorizontalGlue());
        
        Boton btnAnadirEstado = new Boton("AÑADIR ESTADOS", "ANADIR_ESTADO", actionListener, 250, 50, 20);
        btnAnadirEstado.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(btnAnadirEstado);
        
        panel.add(Box.createHorizontalStrut(30));
        
        Boton btnAnadirAlfabeto = new Boton("AÑADIR ALFABETO", "ANADIR_ALFABETO", actionListener, 250, 50, 20);
        btnAnadirAlfabeto.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(btnAnadirAlfabeto);
        
        panel.add(Box.createHorizontalGlue());
        
        return panel;
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(CENTER_ALIGNMENT);
        
        // Tabla de estados
        String[] columnasTabla = {"ESTADOS", "TIPO"};
        modeloTabla = new DefaultTableModel(columnasTabla, 0);
        tablaEstados = new JTable(modeloTabla);
        tablaEstados.setPreferredSize(new java.awt.Dimension(600, 300));
        
        JScrollPane scrollTable = new JScrollPane(tablaEstados);
        scrollTable.setPreferredSize(new java.awt.Dimension(600, 300));
        scrollTable.setMaximumSize(new java.awt.Dimension(600, 300));
        scrollTable.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(scrollTable);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setAlignmentX(CENTER_ALIGNMENT);
        
        Boton btnAnadirTransicion = new Boton("AÑADIR FUNCION TRANSICION", "ANADIR_FUNCION_TRANSICION", actionListener, 400, 50, 20);
        btnAnadirTransicion.setAlignmentX(CENTER_ALIGNMENT);
        panel.add(btnAnadirTransicion);
        
        return panel;
    }
}
