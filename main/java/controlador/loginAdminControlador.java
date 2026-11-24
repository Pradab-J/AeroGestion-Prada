/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import modelo.DAO.usuarioDAO;
import modelo.entidad.usuario;
import vista.Inicio;
import vista.LoginAdminn;
import vista.VentanaPrincipal;

/**
 * CONTROLADOR del inicio de sesión del administrador: valida usuario y
 * contraseña y abre el panel administrativo.
 */
public class loginAdminControlador implements ActionListener {

    private LoginAdminn vista;
    private usuarioDAO daoUsuario;
    private Inicio inicio;
    private VentanaPrincipal ventanaPrincipal;

   public loginAdminControlador(
        LoginAdminn vista,
        usuarioDAO daoUsuario,
        Inicio inicio,
        VentanaPrincipal ventanaPrincipal) {

    this.vista = vista;
    this.daoUsuario = daoUsuario;
    this.inicio = inicio;
    this.ventanaPrincipal = ventanaPrincipal;

    vista.btnIngresar.addActionListener(this);
    vista.btnVolver.addActionListener(this);
}

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnIngresar) {
            iniciarSesion();
        }

        if (e.getSource() == vista.btnVolver) {
            volverInicio();
        }
    }

    /** Verifica usuario y contraseña con usuarioDAO y, si son correctos, abre el panel administrativo. */
    private void iniciarSesion() {

        String nombreUsuario =
                vista.txtUsuario.getText();

        String contraseña =
                new String(vista.txtContrasena.getPassword());

        if (nombreUsuario.isEmpty()
                || contraseña.isEmpty()) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Ingrese el usuario y la contraseña."
            );

            return;
        }

        usuario u =
                daoUsuario.iniciarSesion(
                        nombreUsuario,
                        contraseña
                );

        if (u != null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Bienvenido administrador"
            );

            limpiarCampos();
            vista.setVisible(false);
            ventanaPrincipal.setVisible(true);

        } else {

            JOptionPane.showMessageDialog(
                    vista,
                    "Usuario o contraseña incorrectos."
            );
        }
    }

    /** Vacía los campos de usuario y contraseña. */
    private void limpiarCampos() {
        vista.txtUsuario.setText("");
        vista.txtContrasena.setText("");
    }

    /** Cierra el login y regresa a la portada. */
    private void volverInicio() {
        limpiarCampos();
        vista.setVisible(false);
        // Se reutiliza el Inicio original; un "new Inicio()" nacia sin controladores y sus botones no respondian
        inicio.mostrarInicio();
        inicio.setLocationRelativeTo(null);
        inicio.setVisible(true);
    }
}
