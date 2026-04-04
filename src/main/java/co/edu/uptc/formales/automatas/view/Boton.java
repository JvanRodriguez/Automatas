package co.edu.uptc.formales.automatas.view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

public class Boton extends JButton{

    private ConstanteColor color;

    public Boton(String texto, String actionCommand, ActionListener actionListener, int width, int height, int fontSize) {
        super(texto);
        setActionCommand(actionCommand);
        addActionListener(actionListener);
        Dimension tamaño = new Dimension(width, height);
        setPreferredSize(tamaño);
        setMaximumSize(tamaño);
        
        color = new ConstanteColor();
        
        setBackground(color.getColorFondo());
        setForeground(color.getColorBorde());
        setBorder(new LineBorder(color.getColorBorde(), 2));
        setOpaque(true);
        setFont(new Font("Arial", Font.PLAIN, fontSize));
    }
    
}
