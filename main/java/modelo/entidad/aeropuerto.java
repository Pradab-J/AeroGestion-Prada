/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package modelo.entidad;

/**
 * ENTIDAD Aeropuerto: una fila de la tabla de aeropuertos
 * (id, nombre, código IATA, ciudad y país). Solo guarda datos.
 */
public class aeropuerto {

    private int id_aeropuerto;
    private String nombre;
    private String codigo_iata;
    private String ciudad;
    private String pais;

    /** Constructor vacío. */
    public aeropuerto() {
    }

    /** Constructor con todos los atributos. */
    public aeropuerto(int id_aeropuerto, String nombre,
            String codigo_iata, String ciudad, String pais) {

        this.id_aeropuerto = id_aeropuerto;
        this.nombre = nombre;
        this.codigo_iata = codigo_iata;
        this.ciudad = ciudad;
        this.pais = pais;
    }

    /** Devuelve el valor de Id_aeropuerto. */
    public int getId_aeropuerto() {
        return id_aeropuerto;
    }

    /** Asigna el valor de Id_aeropuerto. */
    public void setId_aeropuerto(int id_aeropuerto) {
        this.id_aeropuerto = id_aeropuerto;
    }

    /** Devuelve el valor de Nombre. */
    public String getNombre() {
        return nombre;
    }

    /** Asigna el valor de Nombre. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Devuelve el valor de Codigo_iata. */
    public String getCodigo_iata() {
        return codigo_iata;
    }

    /** Asigna el valor de Codigo_iata. */
    public void setCodigo_iata(String codigo_iata) {
        this.codigo_iata = codigo_iata;
    }

    /** Devuelve el valor de Ciudad. */
    public String getCiudad() {
        return ciudad;
    }

    /** Asigna el valor de Ciudad. */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /** Devuelve el valor de Pais. */
    public String getPais() {
        return pais;
    }

    /** Asigna el valor de Pais. */
    public void setPais(String pais) {
        this.pais = pais;
    }
}
