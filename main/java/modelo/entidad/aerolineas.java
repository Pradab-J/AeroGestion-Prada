/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package modelo.entidad;

/**
 * ENTIDAD Aerolínea: representa una fila de la tabla de aerolíneas
 * (id, nombre, correo y contacto). Solo guarda datos; no tiene lógica.
 */
public class aerolineas {

    private int id_aerolinea;
    private String nombre;
    private String correo;
    private String contacto;

    /** Constructor vacío. */
    public aerolineas() {
    }

    /** Devuelve el valor de Id_aerolinea. */
    public int getId_aerolinea() {
        return id_aerolinea;
    }

    /** Asigna el valor de Id_aerolinea. */
    public void setId_aerolinea(int id_aerolinea) {
        this.id_aerolinea = id_aerolinea;
    }

    /** Devuelve el valor de Nombre. */
    public String getNombre() {
        return nombre;
    }

    /** Asigna el valor de Nombre. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Devuelve el valor de Correo. */
    public String getCorreo() {
        return correo;
    }

    /** Asigna el valor de Correo. */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /** Devuelve el valor de Contacto. */
    public String getContacto() {
        return contacto;
    }

    /** Asigna el valor de Contacto. */
    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

   

   
}
