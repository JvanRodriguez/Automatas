package co.edu.uptc.formales.automatas.view;

import java.awt.Font;

import javax.swing.JLabel;

public class Texto extends JLabel {
    
    public Texto(String texto, String tipo) {
        super(texto);
        if(tipo.equals("TITULO")) {
            setFont(new Font("Arial", Font.BOLD, 36));
            setForeground(new ConstanteColor().getColorTitulo());
        } else if(tipo.equals("SUBTITULO")){
            setFont(new Font("Arial", Font.BOLD, 28));
            setForeground(new ConstanteColor().getColorBorde());
        } else {
             setFont(new Font("Arial", Font.PLAIN, 24));
            setForeground(new ConstanteColor().getColorBorde());
        }
        
    }
    
}
