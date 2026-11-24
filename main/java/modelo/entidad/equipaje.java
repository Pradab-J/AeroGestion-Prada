/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package modelo.entidad;

/**
 * ENTIDAD Equipaje: tipo de equipaje que se puede comprar
 * (id, peso, estado, tipo y precio).
 */
public class equipaje {

    private int id_equipaje;
    private double peso;
    private String estado;
    private String tipo;
    private double precio;

    /** Constructor vacío. */
    public equipaje() {
    }

    /** Constructor con todos los atributos. */
    public equipaje(
            int id_equipaje,
            double peso,
            String estado,
            String tipo,
            double precio) {

        this.id_equipaje = id_equipaje;
        this.peso = peso;
        this.estado = estado;
        this.tipo = tipo;
        this.precio = precio;
    }

    /** Devuelve el valor de Id_equipaje. */
    public int getId_equipaje() {
        return id_equipaje;
    }

    /** Asigna el valor de Id_equipaje. */
    public void setId_equipaje(int id_equipaje) {
        this.id_equipaje = id_equipaje;
    }

    /** Devuelve el valor de Peso. */
    public double getPeso() {
        return peso;
    }

    /** Asigna el valor de Peso. */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /** Devuelve el valor de Estado. */
    public String getEstado() {
        return estado;
    }

    /** Asigna el valor de Estado. */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Devuelve el valor de Tipo. */
    public String getTipo() {
        return tipo;
    }

    /** Asigna el valor de Tipo. */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /** Devuelve el valor de Precio. */
    public double getPrecio() {
        return precio;
    }

    /** Asigna el valor de Precio. */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}