package co.edu.uptc.formales.automatas.DTO;

import java.util.List;

/**
 * Clase contenedora para los Objetos de Transferencia de Datos (DTOs) 
 * utilizados para representar autómatas y sus componentes en la aplicación,
 * simplificando así la información que viaja entre las distintas capas.
 */
public class DTOs {
    /**
     * DTO que representa una transición.
     * Facilita el mapeo de las transiciones obviando las referencias completas de los estados.
     * 
     * @param origen  El nombre del estado de donde parte la transición.
     * @param simbolo El carácter o cadena que se lee en esta transición.
     * @param destino Los nombres de los estados a los que se dirige la transición (soporta AFN).
     */
    public record TransicionDTO(String origen, String simbolo, List<String> destino) {
        
        /**
         * Constructor para crear un DTO de transición parcial.
         * Útil, por ejemplo, para presentar al usuario en la vista las transiciones que
         * debe completar al definir un autómata.
         * 
         * @param origen  El nombre del estado de origen.
         * @param simbolo El símbolo de la transición.
         */
        public TransicionDTO(String origen, String simbolo) {
            this(origen, simbolo, null); // Llama al constructor de DTO completo y le asigna el destino vacío o nulo
        }
    }

    /**
     * DTO que representa la configuración y todos los elementos de un autómata.
     * Especialmente diseñado para serializar, exportar e importar datos con JSON.
     * 
     * @param tipo              El tipo del autómata (AFN o AFD, como texto).
     * @param alfabeto          La lista de los símbolos que conforman su alfabeto.
     * @param estadoInicial     El nombre del estado principal de arranque.
     * @param estados           El conjunto total de nombres de todos los estados en el autómata.
     * @param estadosAceptacion El subconjunto de estados que son marcados como finales/aceptación.
     * @param transiciones      La colección de transiciones configuradas, representadas como DTOs.
     */
    public record AutomataDTO(String tipo, List<String> alfabeto, String estadoInicial, List<String> estados,
        List<String> estadosAceptacion, List<TransicionDTO> transiciones) {}
}

