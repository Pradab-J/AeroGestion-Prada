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
import modelo.entidad.aerolineas;

/**
 * DAO de Aerolíneas: todas las consultas SQL (guardar, listar, editar, eliminar)
 * de la tabla de aerolíneas. Los controladores usan esta clase y nunca escriben SQL.
 */
public class aerolineaDAO {

    Connection con = Conectar.conexi();

    /** Inserta un nuevo registro de aerolínea en la base de datos. Devuelve true si se guardó. */
    // =========================
    // GUARDAR
    // =========================
    public boolean guardar(aerolineas e) {

        try {

            String sql = "INSERT INTO aerolineas "
                    + "(nombre_aerolinea, correo, contacto) "
                    + "VALUES (?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCorreo());
            ps.setString(3, e.getContacto());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Consulta todos los registros de aerolínea y los devuelve en una lista. */
    // =========================
    // LISTAR
    // =========================
    public ArrayList<aerolineas> listar() {

        ArrayList<aerolineas> lista = new ArrayList<>();

        try {

            String sql = "SELECT * FROM aerolineas";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                aerolineas e = new aerolineas();

                e.setId_aerolinea(
                        rs.getInt("id_aerolinea")
                );

                e.setNombre(
                        rs.getString("nombre_aerolinea")
                );

                e.setCorreo(
                        rs.getString("correo")
                );

                e.setContacto(
                        rs.getString("contacto")
                );

                lista.add(e);
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return lista;
    }

    /** Actualiza en la base de datos los datos de aerolínea. Devuelve true si se actualizó. */
    // =========================
    // EDITAR
    // =========================
    public boolean editar(aerolineas e) {

        try {

            String sql = "UPDATE aerolineas "
                    + "SET nombre_aerolinea=?, correo=?, contacto=? "
                    + "WHERE id_aerolinea=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCorreo());
            ps.setString(3, e.getContacto());
            ps.setInt(4, e.getId_aerolinea());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Elimina el registro de aerolínea con ese identificador. Devuelve true si se eliminó. */
    // =========================
    // ELIMINAR
    // =========================
    public boolean eliminar(int id) {

        try {

            String sql = "DELETE FROM aerolineas "
                    + "WHERE id_aerolinea=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }
}

