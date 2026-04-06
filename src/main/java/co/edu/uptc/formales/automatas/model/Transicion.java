package co.edu.uptc.formales.automatas.model;

import java.util.List;

import co.edu.uptc.formales.automatas.DTO.DTOs;

/**
 * Representa una función de transición dentro de un autómata.
 * Modela el desplazamiento entre estados, teniendo un estado de origen, un símbolo
 * de lectura, y una lista de estados de destino, lo cual permite soportar tanto 
 * autómatas deterministas como no deterministas.
 */
public class Transicion {
    private Estado estadoOrigen;
    private String simbolo;
    private List<Estado> estadoDestino;

    /**
     * Crea una nueva transición.
     * 
     * @param estadoOrigen El estado desde donde inicia la transición.
     * @param simbolo El símbolo leído para realizar la transición.
     */
    public Transicion(Estado estadoOrigen, String simbolo){
        this.estadoOrigen = estadoOrigen;
        this.simbolo = simbolo;
    }

    /**
     * Transforma la entidad en un objeto de transferencia de datos (DTO), 
     * incluyendo los estados de destino.
     * 
     * @return Un objeto TransicionDTO con el estado origen, símbolo y lista de destinos.
     */
    public DTOs.TransicionDTO toDTO() {
        List<String> nombreDestinos = this.estadoDestino.stream()
                .map(Estado::getNombre)
                .toList();
        return new DTOs.TransicionDTO(this.estadoOrigen.getNombre(), this.simbolo, nombreDestinos);
    }

    public DTOs.TransicionDTO toDTOBase() {
        return new DTOs.TransicionDTO(this.estadoOrigen.getNombre(), this.simbolo);
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
