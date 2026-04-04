package co.edu.uptc.formales.automatas.DTO;

import java.util.List;

public class DTOs {
    //Constructor para completar la transicion
    public record TransicionDTO(String origen, String simbolo, List<String> destino) {
        //Constructor para crear la transicionDTO incompleta y mostrarla en vista
        public TransicionDTO(String origen, String simbolo) {
            this(origen, simbolo, null); // Llama al constructor de DTO completa y le pone el destino vacío
        }
    }

    public record AutomataDTO(String tipo, List<String> alfabeto, String estadoInicial, List<String> estados,
        List<String> estadosAceptacion, List<TransicionDTO> transiciones) {}
}

