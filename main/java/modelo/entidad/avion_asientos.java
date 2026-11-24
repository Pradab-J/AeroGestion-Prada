/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo.entidad;

/**
 * ENTIDAD Configuración de asientos de un avión: define cada clase de cabina
 * (nombre, número de filas y distribución por fila, ej. "2-4-2") para un avión.
 */
public class avion_asientos {

    private int id_clase;
    private String nombre_clase;
    private int filas;
    private String distribucion;
    private String fk_avion;

    /** Constructor vacío. */
    public avion_asientos() {
    }

    /** Constructor con todos los atributos. */
    public avion_asientos(int id_clase, String nombre_clase,
            int filas, String distribucion, String fk_avion) {

        this.id_clase = id_clase;
        this.nombre_clase = nombre_clase;
        this.filas = filas;
        this.distribucion = distribucion;
        this.fk_avion = fk_avion;
    }

    /** Devuelve el valor de Id_clase. */
    public int getId_clase() {
        return id_clase;
    }

    /** Asigna el valor de Id_clase. */
    public void setId_clase(int id_clase) {
        this.id_clase = id_clase;
    }

    /** Devuelve el valor de Nombre_clase. */
    public String getNombre_clase() {
        return nombre_clase;
    }

    /** Asigna el valor de Nombre_clase. */
    public void setNombre_clase(String nombre_clase) {
        this.nombre_clase = nombre_clase;
    }

    /** Devuelve el valor de Filas. */
    public int getFilas() {
        return filas;
    }

    /** Asigna el valor de Filas. */
    public void setFilas(int filas) {
        this.filas = filas;
    }

    /** Devuelve el valor de Distribucion. */
    public String getDistribucion() {
        return distribucion;
    }

    /** Asigna el valor de Distribucion. */
    public void setDistribucion(String distribucion) {
        this.distribucion = distribucion;
    }

    /** Devuelve el valor de Fk_avion. */
    public String getFk_avion() {
        return fk_avion;
    }

    /** Asigna el valor de Fk_avion. */
    public void setFk_avion(String fk_avion) {
        this.fk_avion = fk_avion;
    }
}
