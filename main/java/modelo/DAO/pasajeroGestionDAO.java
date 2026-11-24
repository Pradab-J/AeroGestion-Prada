/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import modelo.conexion.Conectar;
import modelo.entidad.pasajero;

/**
 * DAO de Pasajeros para el panel administrativo: listar, buscar por documento,
 * guardar, actualizar y eliminar.
 */
public class pasajeroGestionDAO {

    Connection con = Conectar.conexi();

    /** Consulta todos los registros de pasajero y los devuelve en una lista. */
    // =========================
    // LISTAR PASAJEROS
    // =========================
    public ArrayList<pasajero> listar() {

        ArrayList<pasajero> lista
                = new ArrayList<>();

        try {

            String sql
                    = "SELECT * FROM pasajeros";

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ResultSet rs
                    = ps.executeQuery();

            while (rs.next()) {

                pasajero p
                        = new pasajero();

                p.setDocumento(
                        rs.getString("documento")
                );

                p.setTipoDocumento(
                        rs.getString("tipo_documento")
                );

                p.setNombre(
                        rs.getString("nombre")
                );

                p.setApellido(
                        rs.getString("apellido")
                );

                p.setGenero(
                        rs.getString("genero")
                );

                p.setFechaNacimiento(
                        rs.getString("fecha_nacimiento")
                );

                p.setNacionalidad(
                        rs.getString("nacionalidad")
                );

                p.setTelefono(
                        rs.getString("telefono")
                );

                p.setCorreo(
                        rs.getString("correo")
                );

                lista.add(p);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al listar pasajeros: "
                    + e
            );
        }

        return lista;
    }

    /** Busca un pasajero por su documento. Devuelve null si no existe. */
    // =========================
// BUSCAR POR DOCUMENTO
// =========================
    public pasajero buscarPorDocumento(String documento) {
        pasajero p = null;
        try {
            String sql
                    = "SELECT * FROM pasajeros "
                    + "WHERE documento = ?";
            PreparedStatement ps
                    = con.prepareStatement(sql);
            ps.setString(
                    1,
                    documento
            );
            ResultSet rs
                    = ps.executeQuery();
            if (rs.next()) {
                p = new pasajero();
                p.setDocumento(
                        rs.getString("documento")
                );
                p.setTipoDocumento(
                        rs.getString("tipo_documento")
                );
                p.setNombre(
                        rs.getString("nombre")
                );
                p.setApellido(
                        rs.getString("apellido")
                );
                p.setGenero(
                        rs.getString("genero")
                );
                p.setFechaNacimiento(
                        rs.getString("fecha_nacimiento")
                );
                p.setNacionalidad(
                        rs.getString("nacionalidad")
                );
                p.setTelefono(
                        rs.getString("telefono")
                );
                p.setCorreo(
                        rs.getString("correo")
                );
            }
        } catch (Exception e) {
            System.out.println(
                    "Error al buscar pasajero: "
                    + e
            );
        }
        return p;
    }

    /** Inserta un nuevo registro de pasajero en la base de datos. Devuelve true si se guardó. */
    // =========================
    // GUARDAR
    // =========================
    public boolean guardar(pasajero p) {

        try {

            String sql
                    = "INSERT INTO pasajeros "
                    + "(documento, tipo_documento, nombre, apellido, genero, "
                    + "fecha_nacimiento, nacionalidad, telefono, correo) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ps.setString(
                    1,
                    p.getDocumento()
            );

            ps.setString(
                    2,
                    p.getTipoDocumento()
            );

            ps.setString(
                    3,
                    p.getNombre()
            );

            ps.setString(
                    4,
                    p.getApellido()
            );

            ps.setString(
                    5,
                    p.getGenero()
            );

            ps.setString(
                    6,
                    p.getFechaNacimiento()
            );

            ps.setString(
                    7,
                    p.getNacionalidad()
            );

            ps.setString(
                    8,
                    p.getTelefono()
            );

            ps.setString(
                    9,
                    p.getCorreo()
            );

            ps.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error al guardar pasajero: "
                    + e
            );

            return false;
        }
    }

    /** Actualiza todos los datos de un pasajero existente (identificado por su documento). */
    // =========================
    // ACTUALIZAR
    // =========================
    public boolean actualizar(pasajero p) {

        try {

            String sql
                    = "UPDATE pasajeros SET "
                    + "tipo_documento = ?, "
                    + "nombre = ?, "
                    + "apellido = ?, "
                    + "genero = ?, "
                    + "fecha_nacimiento = ?, "
                    + "nacionalidad = ?, "
                    + "telefono = ?, "
                    + "correo = ? "
                    + "WHERE documento = ?";

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ps.setString(
                    1,
                    p.getTipoDocumento()
            );

            ps.setString(
                    2,
                    p.getNombre()
            );

            ps.setString(
                    3,
                    p.getApellido()
            );

            ps.setString(
                    4,
                    p.getGenero()
            );

            ps.setString(
                    5,
                    p.getFechaNacimiento()
            );

            ps.setString(
                    6,
                    p.getNacionalidad()
            );

            ps.setString(
                    7,
                    p.getTelefono()
            );

            ps.setString(
                    8,
                    p.getCorreo()
            );

            ps.setString(
                    9,
                    p.getDocumento()
            );

            ps.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error al actualizar pasajero: "
                    + e
            );

            return false;
        }
    }

    /** Elimina un pasajero por su documento. */
    // =========================
    // ELIMINAR
    // =========================
    public boolean eliminar(String documento) {

        try {

            String sql
                    = "DELETE FROM pasajeros "
                    + "WHERE documento = ?";

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ps.setString(
                    1,
                    documento
            );

            ps.executeUpdate();

            return true;

        } catch (Exception e) {

            System.out.println(
                    "Error al eliminar pasajero: "
                    + e
            );

            return false;
        }
    }
}
