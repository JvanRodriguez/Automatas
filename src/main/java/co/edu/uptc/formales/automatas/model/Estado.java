package co.edu.uptc.formales.automatas.model;

import java.util.Objects;

/**
 * Representa un estado dentro de un autómata finito (AF).
 * Almacena la información de un estado y proporciona los métodos para acceder a esta.
 * Además, implementa la lógica de igualdad (`equals` y `hashCode`) basándose
 * únicamente en el nombre del estado, lo que permite que sea fácil de buscar o
 * comparar dentro de colecciones de Java como Sets o Maps.
 */
public class Estado {
    private TipoEstado tipo;
    private String nombre;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estado otro = (Estado) obj;
        return Objects.equals(nombre, otro.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    public Estado(String nombre, TipoEstado tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoEstado getTipo() {
        return tipo;
    }

    public void setTipo(TipoEstado tipo) {
        this.tipo = tipo;
    }
}
