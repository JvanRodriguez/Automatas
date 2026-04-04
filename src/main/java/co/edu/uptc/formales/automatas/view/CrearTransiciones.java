package co.edu.uptc.formales.automatas.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import co.edu.uptc.formales.automatas.DTO.DTOs;

public class CrearTransiciones extends JPanel implements ActionListener {

    private JTable tablaTransiciones;
    private DefaultTableModel modeloTabla;
    private Texto lblEstadoOrigen;
    private Texto lblSimbolo;
    private Texto lblProgreso;
    private JPanel panelCheckboxes;
    private List<DTOs.TransicionDTO> listaTransiciones;
    private List<JCheckBox> checkboxes;
    private ConstanteColor color;
    private Boton btnSiguiente;
    private Boton btnCancelar;
    
    private ActionListener parentListener;
    private int indiceActual;
    private List<DTOs.TransicionDTO> transicionesCompletadas;

    public CrearTransiciones(List<DTOs.TransicionDTO> transicionesIncompletas, ActionListener parentListener) {
        this.listaTransiciones = transicionesIncompletas;
        this.color = new ConstanteColor();
        this.parentListener = parentListener;
        this.indiceActual = 0;
        this.transicionesCompletadas = new ArrayList<>();
        initUI();
        mostrarTransicionActual();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(color.getColorFondo());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(color.getColorFondo());
        add(panelPrincipal, BorderLayout.CENTER);

        // Panel superior (transición actual)
        JPanel panelSuperior = crearPanelSuperior();
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        // Panel central (checkboxes)
        panelCheckboxes = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelCheckboxes.setBackground(color.getColorFondo());
        panelCheckboxes.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(color.getColorBorde()),
            "Seleccione los estados destino",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            null,
            color.getColorBorde()
        ));
        panelPrincipal.add(panelCheckboxes, BorderLayout.CENTER);

        // Panel inferior (botones)
        JPanel panelInferior = crearPanelInferior();
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

        // Tabla de transiciones registradas
        JPanel panelTabla = crearPanelTabla();
        add(panelTabla, BorderLayout.SOUTH);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(color.getColorFondo());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(color.getColorBorde()),
            "Transición actual",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            null,
            color.getColorBorde()
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);

        lblEstadoOrigen = new Texto("Estado origen: ---", "NORMAL");
        lblSimbolo = new Texto("Símbolo: ---", "NORMAL");
        lblProgreso = new Texto("Progreso: 0/0", "NORMAL");

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblEstadoOrigen, gbc);

        gbc.gridx = 1;
        panel.add(lblSimbolo, gbc);

        gbc.gridx = 2;
        panel.add(lblProgreso, gbc);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(color.getColorFondo());
        panel.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(Box.createHorizontalGlue());

        btnCancelar = new Boton("CANCELAR", "CANCELAR_TRANSICIONES", this, 150, 40, 14);
        panel.add(btnCancelar);

        panel.add(Box.createHorizontalStrut(20));

        btnSiguiente = new Boton("AGREGAR TRANSICIÓN", "AGREGAR_TRANSICION", this, 200, 40, 14);
        panel.add(btnSiguiente);

        panel.add(Box.createHorizontalGlue());

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color.getColorFondo());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        String[] columnas = {"Estado Origen", "Símbolo", "Estado(s) Destino"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaTransiciones = new JTable(modeloTabla);
        tablaTransiciones.setRowHeight(25);
        tablaTransiciones.getTableHeader().setBackground(color.getColorTitulo());
        tablaTransiciones.getTableHeader().setForeground(color.getBlanco());
        tablaTransiciones.setForeground(color.getColorBorde());

        JScrollPane scrollTabla = new JScrollPane(tablaTransiciones);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Transiciones registradas"));
        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }

    private void actualizarCheckboxes(List<String> estadosDisponibles) {
        panelCheckboxes.removeAll();
        checkboxes = new ArrayList<>();
        
        if (estadosDisponibles == null || estadosDisponibles.isEmpty()) {
            Texto sinEstados = new Texto("No hay estados disponibles", "NORMAL");
            sinEstados.setForeground(color.getColorBorde());
            panelCheckboxes.add(sinEstados);
        } else {
            for (String estado : estadosDisponibles) {
                JCheckBox ch = new JCheckBox(estado);
                ch.setBackground(color.getColorFondo());
                ch.setForeground(color.getColorBorde());
                checkboxes.add(ch);
                panelCheckboxes.add(ch);
            }
        }
        
        panelCheckboxes.revalidate();
        panelCheckboxes.repaint();
    }

    private void mostrarTransicionActual() {
        if (indiceActual < listaTransiciones.size()) {
            DTOs.TransicionDTO actual = listaTransiciones.get(indiceActual);
            lblEstadoOrigen.setText("Estado origen: " + actual.origen());
            lblSimbolo.setText("Símbolo: " + actual.simbolo());
            lblProgreso.setText("Progreso: " + (indiceActual + 1) + "/" + listaTransiciones.size());
            
            // Actualizar checkboxes con los estados destino disponibles
            // Los estados disponibles deberían venir de algún lado (todos los estados del autómata)
            // Por ahora, llamamos a un método que el presenter debe proveer
            if (parentListener != null) {
                parentListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "OBTENER_ESTADOS"));
            }
        } else {
            // Terminaron todas las transiciones
            lblEstadoOrigen.setText("Estado origen: ---");
            lblSimbolo.setText("Símbolo: ---");
            lblProgreso.setText("Progreso: COMPLETADO");
            btnSiguiente.setEnabled(false);
            
            // Notificar al presenter que terminó
            if (parentListener != null) {
                parentListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "TERMINAR_TRANSICIONES"));
            }
        }
    }

    // ========== MÉTODOS PÚBLICOS PARA EL PRESENTER ==========

    public void actualizarEstadosDisponibles(List<String> estadosDisponibles) {
        actualizarCheckboxes(estadosDisponibles);
    }

    public DTOs.TransicionDTO getTransicionActual() {
        if (indiceActual < listaTransiciones.size()) {
            return listaTransiciones.get(indiceActual);
        }
        return null;
    }

    public List<String> getDestinosSeleccionados() {
        List<String> seleccionados = new ArrayList<>();
        if (checkboxes != null) {
            for (JCheckBox ch : checkboxes) {
                if (ch.isSelected()) {
                    seleccionados.add(ch.getText());
                }
            }
        }
        return seleccionados;
    }

    public void guardarTransicionActual(DTOs.TransicionDTO transicionCompleta) {
        transicionesCompletadas.add(transicionCompleta);
        agregarTransicionATabla(transicionCompleta.origen(), transicionCompleta.simbolo(), transicionCompleta.destino());
        limpiarCheckboxes();
        indiceActual++;
        mostrarTransicionActual();
    }

    public void limpiarCheckboxes() {
        if (checkboxes != null) {
            for (JCheckBox ch : checkboxes) {
                ch.setSelected(false);
            }
        }
    }

    private void agregarTransicionATabla(String origen, String simbolo, List<String> destinos) {
        String destinosStr = (destinos != null) ? String.join(", ", destinos) : "---";
        modeloTabla.addRow(new Object[]{origen, simbolo, destinosStr});
    }

    public void limpiarTabla() {
        modeloTabla.setRowCount(0);
        transicionesCompletadas.clear();
        indiceActual = 0;
        mostrarTransicionActual();
    }

    public List<DTOs.TransicionDTO> getTransicionesCompletadas() {
        return transicionesCompletadas;
    }

    // ========== ActionListener ==========

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        
        switch (comando) {
            case "AGREGAR_TRANSICION":
                if (parentListener != null) {
                    parentListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "GUARDAR_TRANSICION"));
                }
                break;
            case "CANCELAR_TRANSICIONES":
                if (parentListener != null) {
                    parentListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CANCELAR_TRANSICIONES"));
                }
                break;
            default:
                if (parentListener != null) {
                    parentListener.actionPerformed(e);
                }
                break;
        }
    }
}