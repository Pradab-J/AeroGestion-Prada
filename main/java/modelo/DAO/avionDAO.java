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
import modelo.entidad.avion;

/** DAO de Aviones: consultas SQL de la tabla de aviones (CRUD). */
public class avionDAO {

    Connection con = Conectar.conexi();

    /** Inserta un nuevo registro de avión en la base de datos. Devuelve true si se guardó. */
    // =========================
    // GUARDAR
    // =========================
    public boolean guardar(avion a) {

        try {

            String sql = "INSERT INTO avion "
                    + "(modelo, año, capacidad, estado, fk_aerolinea) "
                    + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, a.getModelo());
            ps.setString(2, a.getAño());
            ps.setString(3, a.getCapacidad());
            ps.setString(4, a.getEstado());
            ps.setInt(5, a.getFk_aerolinea());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Consulta todos los registros de avión y los devuelve en una lista. */
    // =========================
    // LISTAR
    // =========================
    public ArrayList<avion> listar() {

        ArrayList<avion> lista = new ArrayList<>();

        try {

            String sql = "SELECT * FROM avion";

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                avion a = new avion();

                a.setModelo(rs.getString("modelo"));
                a.setAño(rs.getString("año"));
                a.setCapacidad(rs.getString("capacidad"));
                a.setEstado(rs.getString("estado"));
                a.setFk_aerolinea(rs.getInt("fk_aerolinea"));

                lista.add(a);
            }

        } catch (Exception ex) {

            System.out.println(ex);
        }

        return lista;
    }

    /** Actualiza en la base de datos los datos de avión. Devuelve true si se actualizó. */
    // =========================
    // EDITAR
    // =========================
    public boolean editar(avion a) {

        try {

            String sql = "UPDATE avion SET "
                    + "año=?, capacidad=?, estado=?, fk_aerolinea=? "
                    + "WHERE modelo=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, a.getAño());
            ps.setString(2, a.getCapacidad());
            ps.setString(3, a.getEstado());
            ps.setInt(4, a.getFk_aerolinea());
            ps.setString(5, a.getModelo());

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }

    /** Elimina el registro de avión con ese identificador. Devuelve true si se eliminó. */
    // =========================
    // ELIMINAR
    // =========================
    public boolean eliminar(String modelo) {

        try {

            String sql = "DELETE FROM avion WHERE modelo=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, modelo);

            ps.executeUpdate();

            return true;

        } catch (Exception ex) {

            System.out.println(ex);
            return false;
        }
    }
}