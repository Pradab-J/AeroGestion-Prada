/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package modelo.entidad;

/**
 * ENTIDAD Tiquete: es la reserva/compra de un pasajero en un vuelo. Une pasajero,
 * vuelo, asiento y equipaje, y guarda fecha de emisión, código de reserva,
 * método de pago y precios (asiento, equipaje y total).
 */
public class tiquete {

    private int id_tiquete;
    private String fecha_emision;
    private String reserva;
    private String metodo_pago;
    private int equipaje;
    private int asiento;
    private int vuelo;
    private String pasajero;

    // PRECIOS
    private double precio_asiento;
    private double precio_equipaje;
    private double total;

    /** Constructor vacío. */
    public tiquete() {
    }

    /** Constructor con todos los atributos. */
    public tiquete(
            int id_tiquete,
            String fecha_emision,
            String reserva,
            String metodo_pago,
            int equipaje,
            String pasajero,
            int asiento,
            int vuelo,
            double precio_asiento,
            double precio_equipaje,
            double total) {

        this.id_tiquete = id_tiquete;
        this.fecha_emision = fecha_emision;
        this.reserva = reserva;
        this.metodo_pago = metodo_pago;
        this.equipaje = equipaje;
        this.pasajero = pasajero;
        this.asiento = asiento;
        this.vuelo = vuelo;
        this.precio_asiento = precio_asiento;
        this.precio_equipaje = precio_equipaje;
        this.total = total;
    }

    /** Devuelve el valor de Id_tiquete. */
    public int getId_tiquete() {
        return id_tiquete;
    }

    /** Asigna el valor de Id_tiquete. */
    public void setId_tiquete(int id_tiquete) {
        this.id_tiquete = id_tiquete;
    }

    /** Devuelve el valor de Fecha_emision. */
    public String getFecha_emision() {
        return fecha_emision;
    }

    /** Asigna el valor de Fecha_emision. */
    public void setFecha_emision(String fecha_emision) {
        this.fecha_emision = fecha_emision;
    }

    /** Devuelve el valor de Reserva. */
    public String getReserva() {
        return reserva;
    }

    /** Asigna el valor de Reserva. */
    public void setReserva(String reserva) {
        this.reserva = reserva;
    }

    /** Devuelve el valor de Metodo_pago. */
    public String getMetodo_pago() {
        return metodo_pago;
    }

    /** Asigna el valor de Metodo_pago. */
    public void setMetodo_pago(String metodo_pago) {
        this.metodo_pago = metodo_pago;
    }

    /** Devuelve el valor de Equipaje. */
    public int getEquipaje() {
        return equipaje;
    }

    /** Asigna el valor de Equipaje. */
    public void setEquipaje(int equipaje) {
        this.equipaje = equipaje;
    }

    /** Devuelve el valor de Pasajero. */
    public String getPasajero() {
        return pasajero;
    }

    /** Asigna el valor de Pasajero. */
    public void setPasajero(String pasajero) {
        this.pasajero = pasajero;
    }

    /** Devuelve el valor de Asiento. */
    public int getAsiento() {
        return asiento;
    }

    /** Asigna el valor de Asiento. */
    public void setAsiento(int asiento) {
        this.asiento = asiento;
    }

    /** Devuelve el valor de Vuelo. */
    public int getVuelo() {
        return vuelo;
    }

    /** Asigna el valor de Vuelo. */
    public void setVuelo(int vuelo) {
        this.vuelo = vuelo;
    }

    // =========================================================
    // PRECIOS
    // =========================================================

    /** Devuelve el valor de Precio_asiento. */
    public double getPrecio_asiento() {
        return precio_asiento;
    }

    /** Asigna el valor de Precio_asiento. */
    public void setPrecio_asiento(double precio_asiento) {
        this.precio_asiento = precio_asiento;
    }

    /** Devuelve el valor de Precio_equipaje. */
    public double getPrecio_equipaje() {
        return precio_equipaje;
    }

    /** Asigna el valor de Precio_equipaje. */
    public void setPrecio_equipaje(double precio_equipaje) {
        this.precio_equipaje = precio_equipaje;
    }

    /** Devuelve el valor de Total. */
    public double getTotal() {
        return total;
    }

    /** Asigna el valor de Total. */
    public void setTotal(double total) {
        this.total = total;
    }
}