/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



package modelo.entidad;

/**
 * ENTIDAD Asiento: una silla de un vuelo concreto
 * (id, número como "12A", clase, estado, vuelo al que pertenece y precio).
 */
public class asiento {

    private int id_asiento;
    private String num_asiento;
    private String clase;
    private String estado;
    private int fk_vuelo;
    private double precio;

    /** Constructor vacío. */
    public asiento() {
    }

    /** Constructor con todos los atributos. */
    public asiento(int id_asiento, String num_asiento, String clase,
            String estado, int fk_vuelo, double precio) {

        this.id_asiento = id_asiento;
        this.num_asiento = num_asiento;
        this.clase = clase;
        this.estado = estado;
        this.fk_vuelo = fk_vuelo;
        this.precio = precio;
    }

    /** Devuelve el valor de Id_asiento. */
    public int getId_asiento() {
        return id_asiento;
    }

    /** Asigna el valor de Id_asiento. */
    public void setId_asiento(int id_asiento) {
        this.id_asiento = id_asiento;
    }

    /** Devuelve el valor de Num_asiento. */
    public String getNum_asiento() {
        return num_asiento;
    }

    /** Asigna el valor de Num_asiento. */
    public void setNum_asiento(String num_asiento) {
        this.num_asiento = num_asiento;
    }

    /** Devuelve el valor de Clase. */
    public String getClase() {
        return clase;
    }

    /** Asigna el valor de Clase. */
    public void setClase(String clase) {
        this.clase = clase;
    }

    /** Devuelve el valor de Estado. */
    public String getEstado() {
        return estado;
    }

    /** Asigna el valor de Estado. */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /** Devuelve el valor de Fk_vuelo. */
    public int getFk_vuelo() {
        return fk_vuelo;
    }

    /** Asigna el valor de Fk_vuelo. */
    public void setFk_vuelo(int fk_vuelo) {
        this.fk_vuelo = fk_vuelo;
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