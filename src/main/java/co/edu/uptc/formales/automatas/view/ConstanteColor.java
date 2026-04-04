package co.edu.uptc.formales.automatas.view;

import java.awt.Color;

public class ConstanteColor {

    private Color blanco;
    private Color colorBorde;
    private Color colorFondo;
    private Color colorTitulo;

    public ConstanteColor() {
        blanco = new Color(255, 255, 255);
        colorBorde = new Color(80, 116, 95);
        colorFondo = new Color(216, 227, 231);
        colorTitulo = new Color(91, 141, 143);
    }

    public Color getBlanco() {
        return blanco;
    }
    public Color getColorBorde() {
        return colorBorde;
    }
    public Color getColorFondo() {
        return colorFondo;
    }
    public Color getColorTitulo() {
        return colorTitulo;
    }
    
}
