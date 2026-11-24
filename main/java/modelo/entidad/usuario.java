/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package modelo.entidad;

/** ENTIDAD Usuario: cuenta del administrador que inicia sesión en el sistema
 * (id, usuario, nombre, rol y estado). */
public class usuario {

    private int id_usuario;
    private String usuario;
    private String contraseña;
    private String nombre;
    private String rol;
    private String estado;

    /** Constructor vacío. */
    public usuario() {
    }

    /** Constructor con todos los atributos. */
    public usuario(int id_usuario, String usuario, String contraseña,
            String nombre, String rol, String estado) {

        this.id_usuario = id_usuario;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.nombre = nombre;
        this.rol = rol;
        this.estado = estado;
    }

    /** Devuelve el valor de Id_usuario. */
    public int getId_usuario() {
        return id_usuario;
    }

    /** Asigna el valor de Id_usuario. */
    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    /** Devuelve el valor de Usuario. */
    public String getUsuario() {
        return usuario;
    }

    /** Asigna el valor de Usuario. */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /** Devuelve el valor de Contraseña. */
    public String getContraseña() {
        return contraseña;
    }

    /** Asigna el valor de Contraseña. */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /** Devuelve el valor de Nombre. */
    public String getNombre() {
        return nombre;
    }

    /** Asigna el valor de Nombre. */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** Devuelve el valor de Rol. */
    public String getRol() {
        return rol;
    }

    /** Asigna el valor de Rol. */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /** Devuelve el valor de Estado. */
    public String getEstado() {
        return estado;
    }

    /** Asigna el valor de Estado. */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}