/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package modelo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import modelo.conexion.Conectar;
import modelo.entidad.usuario;

/** DAO de Usuarios: valida el inicio de sesión del administrador. */
public class usuarioDAO {

    Connection con = Conectar.conexi();

    /**
     * Comprueba usuario y contraseña del administrador. Devuelve el usuario si son
     * correctos o null si no.
     */
    public usuario iniciarSesion(String usuario, String contraseña) {

        usuario u = null;

        try {
            String sql = "SELECT * FROM usuario "
                    + "WHERE usuario = ? "
                    + "AND contraseña = ? "
                    + "AND rol = 'Administrador' "
                    + "AND estado = 'Activo'";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, usuario);
            ps.setString(2, contraseña);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                u = new usuario();

                u.setId_usuario(rs.getInt("id_usuario"));
                u.setUsuario(rs.getString("usuario"));
                u.setContraseña(rs.getString("contraseña"));
                u.setNombre(rs.getString("nombre"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getString("estado"));
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return u;
    }

    /** Devuelve todos los usuarios registrados (sin cargar la contraseña). */
    public java.util.ArrayList<usuario> listar() {
        java.util.ArrayList<usuario> lista = new java.util.ArrayList<>();
        String sql = "SELECT id_usuario, usuario, nombre, rol, estado FROM usuario ORDER BY id_usuario";
        try (PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuario u = new usuario();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setUsuario(rs.getString("usuario"));
                u.setNombre(rs.getString("nombre"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getString("estado"));
                lista.add(u);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return lista;
    }

    /** Registra un usuario nuevo. Devuelve true si se guardó. */
    public boolean crear(usuario u) {
        String sql = "INSERT INTO usuario (usuario, contraseña, nombre, rol, estado) VALUES (?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getUsuario());
            ps.setString(2, u.getContraseña());
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getRol());
            ps.setString(5, u.getEstado());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    /**
     * Actualiza nombre, rol y estado de un usuario. La contraseña solo se cambia
     * si el objeto trae una contraseña nueva (no vacía). El nombre de usuario no cambia.
     */
    public boolean actualizar(usuario u) {
        boolean conClave = u.getContraseña() != null && !u.getContraseña().isEmpty();
        String sql = "UPDATE usuario SET nombre = ?, rol = ?, estado = ?"
                + (conClave ? ", contraseña = ?" : "")
                + " WHERE id_usuario = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, u.getNombre());
            ps.setString(i++, u.getRol());
            ps.setString(i++, u.getEstado());
            if (conClave) {
                ps.setString(i++, u.getContraseña());
            }
            ps.setInt(i, u.getId_usuario());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    /** Cambia solo el estado (Activo / Inactivo) de un usuario. */
    public boolean cambiarEstado(int idUsuario, String estado) {
        String sql = "UPDATE usuario SET estado = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    /**
     * Cuenta los administradores activos, sin contar al usuario "idExcluir".
     * Sirve para no dejar el sistema sin ningún administrador que pueda entrar.
     * Si hay un error devuelve 0 (modo seguro: se bloquea el cambio).
     */
    public int contarAdministradoresActivos(int idExcluir) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE rol = 'Administrador' AND estado = 'Activo' AND id_usuario <> ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idExcluir);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (Exception ex) {
            System.out.println(ex);
            return 0;
        }
    }
}
