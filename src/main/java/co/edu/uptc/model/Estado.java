package co.edu.uptc.model;

public class Estado {
    private TipoEstado tipo;
    private String nombre;

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
