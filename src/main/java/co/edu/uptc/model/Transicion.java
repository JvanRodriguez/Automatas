package co.edu.uptc.model;

import java.util.List;

public class Transicion {
    private Estado estadoOrigen;
    private String simbolo;
    private Estado estadoDestinoAFD;
    private List<Estado> estadoDestinoAFN;

    public Transicion(Estado estadoDestinoAFD, List<Estado> estadoDestinoAFN, Estado estadoOrigen, String simbolo) {
        this.estadoDestinoAFD = estadoDestinoAFD;
        this.estadoDestinoAFN = estadoDestinoAFN;
        this.estadoOrigen = estadoOrigen;
        this.simbolo = simbolo;
    }

    public Estado getEstadoDestinoAFD() {
        return estadoDestinoAFD;
    }

    public void setEstadoDestinoAFD(Estado estadoDestinoAFD) {
        this.estadoDestinoAFD = estadoDestinoAFD;
    }

    public List<Estado> getEstadoDestinoAFN() {
        return estadoDestinoAFN;
    }

    public void setEstadoDestinoAFN(List<Estado> estadoDestinoAFN) {
        this.estadoDestinoAFN = estadoDestinoAFN;
    }

    public Estado getEstadoOrigen() {
        return estadoOrigen;
    }

    public void setEstadoOrigen(Estado estadoOrigen) {
        this.estadoOrigen = estadoOrigen;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }
}
