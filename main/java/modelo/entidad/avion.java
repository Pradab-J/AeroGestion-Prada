/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.entidad;

/**
 * ENTIDAD Avión: un avión de la flota (modelo, año, capacidad, estado y
 * aerolínea dueña). El modelo funciona como identificador.
 */
public class avion {

    private String modelo;
    private String año;
    private String capacidad;
    private String estado;
    private int fk_aerolinea;

    /** Constructor vacío. */
    public avion() {
    }

    /** Constructor con todos los atributos. */
    public avion(String modelo, String año, String capacidad,
            String estado, int fk_aerolinea) {

        this.modelo = modelo;
        this.año = año;
        this.capacidad = capacidad;
        this.estado = estado;
        this.fk_aerolinea = fk_aerolinea;
    }

    /** Devuelve el valor de Modelo. */
    public String getModelo() {
        return modelo;
    }

    /** Asigna el valor de Modelo. */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /** Devuelve el valor de Año. */
    public String getAño() {
        return año;
    }

    /** Asigna el valor de Año. */
    public void setAño(String año) {
        this.año = año;
    }

    /** Devuelve el valor de Capacidad. */
    public String getCapacidad() {
        return capacidad;
    }

    /** Asigna el valor de Capacidad. */
    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    /** Devuelve el valor de Estado. */
    public String getEstado() {
        return estado;
    }

    /** Asigna el valor de Estado. */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Devuelve el valor de Fk_aerolinea. */
    public int getFk_aerolinea() {
        return fk_aerolinea;
    }

    /** Asigna el valor de Fk_aerolinea. */
    public void setFk_aerolinea(int fk_aerolinea) {
        this.fk_aerolinea = fk_aerolinea;
    }
}