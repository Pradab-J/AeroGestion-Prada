/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo.entidad;

/**
 * ENTIDAD Vuelo: un viaje programado (avión, ruta, fechas y horas de salida y
 * llegada, y estado).
 */
public class vuelo {

    private int id_vuelo;
    private String fecha_salida;
    private String fecha_llegada;
    private String hora_salida;
    private String hora_llegada;
    private String estado;
    private String fk_avion;
    private int fk_ruta;

    /** Constructor vacío. */
    public vuelo() {
    }

    /** Constructor con todos los atributos. */
    public vuelo(int id_vuelo, String fecha_salida, String fecha_llegada,
            String hora_salida, String hora_llegada, String estado,
            String fk_avion, int fk_ruta) {

        this.id_vuelo = id_vuelo;
        this.fecha_salida = fecha_salida;
        this.fecha_llegada = fecha_llegada;
        this.hora_salida = hora_salida;
        this.hora_llegada = hora_llegada;
        this.estado = estado;
        this.fk_avion = fk_avion;
        this.fk_ruta = fk_ruta;
    }

    /** Devuelve el valor de Id_vuelo. */
    public int getId_vuelo() {
        return id_vuelo;
    }

    /** Asigna el valor de Id_vuelo. */
    public void setId_vuelo(int id_vuelo) {
        this.id_vuelo = id_vuelo;
    }

    /** Devuelve el valor de Fecha_salida. */
    public String getFecha_salida() {
        return fecha_salida;
    }

    /** Asigna el valor de Fecha_salida. */
    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    /** Devuelve el valor de Fecha_llegada. */
    public String getFecha_llegada() {
        return fecha_llegada;
    }

    /** Asigna el valor de Fecha_llegada. */
    public void setFecha_llegada(String fecha_llegada) {
        this.fecha_llegada = fecha_llegada;
    }

    /** Devuelve el valor de Hora_salida. */
    public String getHora_salida() {
        return hora_salida;
    }

    /** Asigna el valor de Hora_salida. */
    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    /** Devuelve el valor de Hora_llegada. */
    public String getHora_llegada() {
        return hora_llegada;
    }

    /** Asigna el valor de Hora_llegada. */
    public void setHora_llegada(String hora_llegada) {
        this.hora_llegada = hora_llegada;
    }

    /** Devuelve el valor de Estado. */
    public String getEstado() {
        return estado;
    }

    /** Asigna el valor de Estado. */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Devuelve el valor de Fk_avion. */
    public String getFk_avion() {
        return fk_avion;
    }

    /** Asigna el valor de Fk_avion. */
    public void setFk_avion(String fk_avion) {
        this.fk_avion = fk_avion;
    }

    /** Devuelve el valor de Fk_ruta. */
    public int getFk_ruta() {
        return fk_ruta;
    }

    /** Asigna el valor de Fk_ruta. */
    public void setFk_ruta(int fk_ruta) {
        this.fk_ruta = fk_ruta;
    }
}