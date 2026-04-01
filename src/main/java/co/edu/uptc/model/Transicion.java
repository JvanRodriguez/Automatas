package co.edu.uptc.model;

import co.edu.uptc.DTO.DTOs;

import java.util.List;

public class Transicion {
    private Estado estadoOrigen;
    private String simbolo;
    private List<Estado> estadoDestino;

    public Transicion(Estado estadoOrigen, String Simbolo){
        this.estadoOrigen = estadoOrigen;
        this.simbolo = simbolo;
    }

    public DTOs.TransicionDTO toDTO() {
        List<String> nombreDestinos = this.estadoDestino.stream()
                .map(Estado::getNombre)
                .toList();
        return new DTOs.TransicionDTO(this.estadoOrigen.getNombre(), this.simbolo, nombreDestinos);
    }

    public List<Estado> getEstadoDestino() {
        return estadoDestino;
    }

    public void setEstadoDestino(List<Estado> estadoDestino) {
        this.estadoDestino = estadoDestino;
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
