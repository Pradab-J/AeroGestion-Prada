/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package modelo.entidad;

/**
 * ENTIDAD Ruta: trayecto entre dos ciudades (origen, destino, duración y
 * aeropuerto asociado).
 */
public class ruta {

    private int id_ruta;
    private String origen;
    private String destino;
    private int duracion;
    private int fk_aeropuerto;

    /** Constructor vacío. */
    public ruta() {
    }

    /** Constructor con todos los atributos. */
    public ruta(int id_ruta, String origen, String destino,
            int duracion, int fk_aeropuerto) {

        this.id_ruta = id_ruta;
        this.origen = origen;
        this.destino = destino;
        this.duracion = duracion;
        this.fk_aeropuerto = fk_aeropuerto;
    }

    /** Devuelve el valor de Id_ruta. */
    public int getId_ruta() {
        return id_ruta;
    }

    /** Asigna el valor de Id_ruta. */
    public void setId_ruta(int id_ruta) {
        this.id_ruta = id_ruta;
    }

    /** Devuelve el valor de Origen. */
    public String getOrigen() {
        return origen;
    }

    /** Asigna el valor de Origen. */
    public void setOrigen(String origen) {
        this.origen = origen;
    }

    /** Devuelve el valor de Destino. */
    public String getDestino() {
        return destino;
    }

    /** Asigna el valor de Destino. */
    public void setDestino(String destino) {
        this.destino = destino;
    }

    /** Devuelve el valor de Duracion. */
    public int getDuracion() {
        return duracion;
    }

    /** Asigna el valor de Duracion. */
    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    /** Devuelve el valor de Fk_aeropuerto. */
    public int getFk_aeropuerto() {
        return fk_aeropuerto;
    }

    /** Asigna el valor de Fk_aeropuerto. */
    public void setFk_aeropuerto(int fk_aeropuerto) {
        this.fk_aeropuerto = fk_aeropuerto;
    }
}