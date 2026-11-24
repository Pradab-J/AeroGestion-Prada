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
import modelo.entidad.aeropuerto;

/** DAO de Aeropuertos: consultas SQL de la tabla de aeropuertos (CRUD). */
public class aeropuertoDAO {

    Connection con = Conectar.conexi();

    /** Inserta un nuevo registro de aeropuerto en la base de datos. Devuelve true si se guardó. */
    // =========================
    // GUARDAR
    // =========================
    public boolean guardar(aeropuerto a) {

        try {

            String sql = "INSERT INTO aeropuerto "
                    + "(nombre, codigo_iata, ciudad, pais) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, a.getNombre());
            ps.setString(2, a.getCodigo_iata());
            ps.setString(3, a.getCiudad());
            ps.setString(4, a.getPais());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Consulta todos los registros de aeropuerto y los devuelve en una lista. */
    // =========================
    // LISTAR
    // =========================
    public ArrayList<aeropuerto> listar() {

        ArrayList<aeropuerto> lista = new ArrayList<>();

        try {

            String sql = "SELECT * FROM aeropuerto";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                aeropuerto a = new aeropuerto();

                a.setId_aeropuerto(
                        rs.getInt("id_aeropuerto")
                );

                a.setNombre(
                        rs.getString("nombre")
                );

                a.setCodigo_iata(
                        rs.getString("codigo_iata")
                );

                a.setCiudad(
                        rs.getString("ciudad")
                );

                a.setPais(
                        rs.getString("pais")
                );

                lista.add(a);
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return lista;
    }

    /** Actualiza en la base de datos los datos de aeropuerto. Devuelve true si se actualizó. */
    // =========================
    // EDITAR
    // =========================
    public boolean editar(aeropuerto a) {

        try {

            String sql = "UPDATE aeropuerto "
                    + "SET nombre=?, codigo_iata=?, ciudad=?, pais=? "
                    + "WHERE id_aeropuerto=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, a.getNombre());
            ps.setString(2, a.getCodigo_iata());
            ps.setString(3, a.getCiudad());
            ps.setString(4, a.getPais());
            ps.setInt(5, a.getId_aeropuerto());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Elimina el registro de aeropuerto con ese identificador. Devuelve true si se eliminó. */
    // =========================
    // ELIMINAR
    // =========================
    public boolean eliminar(int id) {

        try {

            String sql = "DELETE FROM aeropuerto "
                    + "WHERE id_aeropuerto=?";

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
