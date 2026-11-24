/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.DAO.aeropuertoDAO;
import modelo.DAO.consultaReservaDAO;
import modelo.DAO.usuarioDAO;
import modelo.DAO.vueloDAO;

import vista.Inicio;
import vista.JfrmMisReservas;
import vista.JpnlBuscarVuelo;
import vista.JpnlPasajeros;
import vista.LoginAdminn;
import vista.VentanaPrincipal;

/**
 * CONTROLADOR de la portada (Inicio): atiende los botones Reservar, Consultar
 * reserva e Iniciar sesión (administrador).
 */
public class inicioControlador implements ActionListener {

    private Inicio vista;

    private JpnlBuscarVuelo panelBuscarVuelo;
    private JpnlPasajeros panelPasajeros;

    private LoginAdminn login;
    private VentanaPrincipal ventanaPrincipal;

    private buscarVueloControlador controladorBuscarVuelo;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public inicioControlador(
            Inicio vista,
            JpnlBuscarVuelo panelBuscarVuelo,
            JpnlPasajeros panelPasajeros,
            aeropuertoDAO daoAeropuerto,
            vueloDAO daoVuelo,
            LoginAdminn login,
            usuarioDAO daoUsuario,
            VentanaPrincipal ventanaPrincipal) {

        this.vista = vista;
        this.panelBuscarVuelo = panelBuscarVuelo;
        this.panelPasajeros = panelPasajeros;
        this.login = login;
        this.ventanaPrincipal = ventanaPrincipal;

        // =========================================
        // CONTROLADOR DE BÚSQUEDA DE VUELOS
        // =========================================

        // (El controlador de busqueda de vuelos lo crea main.java.
        //  Crearlo aqui tambien duplicaba cada clic: dos mensajes, dos busquedas.)

        // =========================================
        // BOTONES DE INICIO
        // =========================================

        vista.btnReserva.addActionListener(this);
        vista.btnConsultar.addActionListener(this);
        vista.btnIniciarSesion.addActionListener(this);

        // =========================================
        // CONTROLADOR DEL LOGIN
        // =========================================

        new loginAdminControlador(
                login,
                daoUsuario,
                vista,
                ventanaPrincipal
        );

        // =========================================
        // CERRAR SESION DEL ADMINISTRADOR
        // =========================================
        ventanaPrincipal.setAlCerrarSesion(() -> {
            ventanaPrincipal.setVisible(false);
            ventanaPrincipal.reiniciar();
            login.txtUsuario.setText("");
            login.txtContrasena.setText("");
            vista.mostrarInicio();
            vista.setLocationRelativeTo(null);
            vista.setVisible(true);
        });
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        // =========================================
        // BOTÓN RESERVA DE VUELO
        // =========================================

        if (e.getSource() == vista.btnReserva) {

            vista.mostrarPanel(panelBuscarVuelo);
        }

        // =========================================
        // BOTÓN CONSULTAR RESERVA
        // =========================================

        if (e.getSource() == vista.btnConsultar) {

            JfrmMisReservas ventana = new JfrmMisReservas();
            new consultaReservaControlador(ventana, new consultaReservaDAO());
            ventana.setVisible(true);
        }

        // =========================================
        // BOTÓN INICIAR SESIÓN
        // =========================================

        if (e.getSource() == vista.btnIniciarSesion) {

            vista.setVisible(false);
            login.setVisible(true);
        }
    }
}
