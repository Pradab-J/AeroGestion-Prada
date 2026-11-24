/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.aerolineaControlador;
import controlador.aeropuertoControlador;
import controlador.asientoControlador;
import controlador.avionControlador;
import controlador.avion_asientosControlador;
import controlador.buscarVueloControlador;
import controlador.equipajeControlador;
import controlador.inicioControlador;
import controlador.pasajeroControlador;
import controlador.rutaControlador;
import controlador.vueloControlador;
import controlador.pasajeroGestionControlador;
import controlador.reservaControlador;
import controlador.tiqueteControlador;
import controlador.usuarioControlador;
import modelo.DAO.pasajeroGestionDAO;

import modelo.DAO.aerolineaDAO;
import modelo.DAO.aeropuertoDAO;
import modelo.DAO.asientoDAO;
import modelo.DAO.avionDAO;
import modelo.conexion.Conectar;
import modelo.DAO.avion_asientosDAO;
import modelo.DAO.equipajeDAO;
import modelo.DAO.pasajeroDAO;
import modelo.DAO.reservaDAO;
import modelo.DAO.rutaDAO;
import modelo.DAO.tiqueteDAO;
import modelo.DAO.usuarioDAO;
import modelo.DAO.vueloDAO;

/**
 * CLASE PRINCIPAL: punto de entrada del programa. Instala el tema, comprueba la
 * conexión con la base de datos, crea todos los paneles, DAO y controladores y los
 * conecta entre sí, y finalmente abre la portada.
 */
public class main {

    
    /** Comprueba que se pueda conectar con la base de datos. */
    private static boolean hayConexion() {
        try (java.sql.Connection c = Conectar.conexi()) {
            return c != null && c.isValid(3);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Arranque del programa: instala el tema, verifica la base de datos, crea paneles,
     * DAO y controladores, los conecta y abre la portada.
     */
    public static void main(String[] args) {

        // Cualquier error inesperado se muestra con un mensaje claro en vez de dejar la pantalla "congelada"
        Thread.setDefaultUncaughtExceptionHandler((hilo, error) -> {
            error.printStackTrace();
            javax.swing.SwingUtilities.invokeLater(() -> javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Ocurrió un error inesperado y la operación no se completó.\n"
                    + "Intente de nuevo. Si el problema continúa, cierre y vuelva a abrir el programa.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE));
        });

        java.awt.EventQueue.invokeLater(() -> {

            // TEMA VISUAL 
            Tema.instalar();

            // Sin base de datos el sistema no puede trabajar: se avisa en lugar de abrir pantallas vacias
            if (!hayConexion()) {
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "No se pudo conectar con la base de datos.\n\n"
                        + "Verifique que MySQL esté encendido y que el usuario y la contraseña\n"
                        + "de la clase Conectar sean correctos. Luego abra el programa de nuevo.",
                        "Sin conexión",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            JpnlReservas panelReservas = new JpnlReservas();
            reservaControlador controladorReservas
                    = new reservaControlador(panelReservas, new reservaDAO());

            // =========================
            // PASAJEROS
            // =========================
            JpnlPasajeros panelPasajeros
                    = new JpnlPasajeros();

            pasajeroDAO daoPasajero
                    = new pasajeroDAO();
            

            // =========================
            // EQUIPAJES
            // =========================
            JpnlEquipaje panelEquipaje
                    = new JpnlEquipaje();

            equipajeDAO daoEquipaje
                    = new equipajeDAO();

            equipajeControlador controladorEquipaje
                    = new equipajeControlador(
                            panelEquipaje,
                            daoEquipaje
                    );

            // =========================
            // AEROLÍNEAS
            // =========================
            JpnlAerolineas panelAerolineas
                    = new JpnlAerolineas();

            aerolineaDAO daoAerolinea
                    = new aerolineaDAO();

            aerolineaControlador controladorAerolinea
                    = new aerolineaControlador(
                            panelAerolineas,
                            daoAerolinea
                    );

            // =========================
            // AEROPUERTOS
            // =========================
            JpnlAeropuerto panelAeropuerto
                    = new JpnlAeropuerto();

            aeropuertoDAO daoAeropuerto
                    = new aeropuertoDAO();

            aeropuertoControlador controladorAeropuerto
                    = new aeropuertoControlador(
                            panelAeropuerto,
                            daoAeropuerto
                    );

            // =========================
            // RUTAS
            // =========================
            JpnlRuta panelRuta
                    = new JpnlRuta();

            rutaDAO daoRuta
                    = new rutaDAO();

            rutaControlador controladorRuta
                    = new rutaControlador(
                            panelRuta,
                            daoRuta,
                            daoAeropuerto
                    );

            // =========================
            // AVIONES
            // =========================
            JpnlAvion panelAvion
                    = new JpnlAvion();

            avionDAO daoAvion
                    = new avionDAO();

            avionControlador controladorAvion
                    = new avionControlador(
                            panelAvion,
                            daoAvion,
                            daoAerolinea
                    );

            // =========================
            // ASIENTOS
            // =========================
            JpnlAsientos panelAsientos
                    = new JpnlAsientos();
            asientoDAO daoAsiento
                    = new asientoDAO();
            avion_asientosDAO daoConfiguracion
                    = new avion_asientosDAO();
            vueloDAO daoVuelo
                    = new vueloDAO();
            asientoControlador controladorAsiento
                    = new asientoControlador(
                            panelAsientos,
                            daoAsiento,
                            daoAvion,
                            daoConfiguracion,
                            daoVuelo
                    );

            // =========================
            // CONFIGURACIÓN DEL AVIÓN
            // =========================
            JpnlConfiguracionAvion panelConfiguracion
                    = new JpnlConfiguracionAvion();

            avion_asientosControlador controladorConfiguracion
                    = new avion_asientosControlador(
                            panelConfiguracion,
                            daoConfiguracion,
                            daoAvion
                    );

            // =========================
            // VUELOS
            // =========================
            JpnlVuelo panelVuelo
                    = new JpnlVuelo();

            
            vueloControlador controladorVuelo
                    = new vueloControlador(
                            panelVuelo,
                            daoVuelo,
                            daoAvion,
                            daoRuta
                    );

            // =========================
            // BÚSQUEDA DE VUELOS
            // =========================
            JpnlBuscarVuelo panelBuscarVuelo
                    = new JpnlBuscarVuelo();

            // =========================
            // GESTIÓN DE PASAJEROS
            // =========================
            JpnlGestionPasajeros panelGestionPasajeros
                    = new JpnlGestionPasajeros();

            pasajeroGestionDAO daoGestionPasajeros
                    = new pasajeroGestionDAO();

            pasajeroGestionControlador controladorGestionPasajeros
                    = new pasajeroGestionControlador(
                            panelGestionPasajeros,
                            daoGestionPasajeros
                    );

            // =========================
            // GESTIÓN DE TUQUETES
            // =========================
            JpnlTiquete panelTiquete = new JpnlTiquete();

            tiqueteDAO daoTiquete = new tiqueteDAO();

            tiqueteControlador controladorTiquete
                    = new tiqueteControlador(
                            panelTiquete,
                            daoTiquete,
                            daoEquipaje,
                            daoGestionPasajeros,
                            daoAsiento,
                            daoVuelo
                    );

            // =========================
            // VENTANA PRINCIPAL ADMIN
            // =========================
            VentanaPrincipal ventanaPrincipal
                    = new VentanaPrincipal(
                            panelPasajeros,
                            panelEquipaje,
                            panelAerolineas,
                            panelAeropuerto,
                            panelRuta,
                            panelVuelo,
                            panelAsientos,
                            panelConfiguracion,
                            panelAvion,
                            panelGestionPasajeros,
                            panelTiquete,
                            panelReservas
                    );

            // =========================
            // INICIO
            // =========================
            Inicio inicio
                    = new Inicio();

            // =========================
            // CONTROLADOR BÚSQUEDA
            // =========================
            buscarVueloControlador controladorBuscarVuelo
                    = new buscarVueloControlador(
                            inicio,
                            panelBuscarVuelo,
                            daoAeropuerto,
                            daoVuelo,
                            panelPasajeros
                    );

            // =========================
            // CONTROLADOR PASAJEROS
            // =========================
            pasajeroControlador controladorPasajero
                    = new pasajeroControlador(
                            panelPasajeros,
                            daoPasajero,
                            inicio,
                            controladorBuscarVuelo,
                            daoAsiento,
                            daoVuelo,
                            daoConfiguracion,
                            daoEquipaje,
                            daoTiquete,
                            daoGestionPasajeros
                    );

            // =========================
            // LOGIN
            // =========================
            LoginAdminn login
                    = new LoginAdminn();

            usuarioDAO daoUsuario
                    = new usuarioDAO();

            // =========================
            // GESTIÓN DE USUARIOS
            // =========================
            JpnlUsuarios panelUsuarios
                    = new JpnlUsuarios(
                            usuarioControlador.ROLES,
                            usuarioControlador.ESTADOS
                    );
            usuarioControlador controladorUsuarios
                    = new usuarioControlador(
                            panelUsuarios,
                            daoUsuario
                    );
            ventanaPrincipal.setPanelUsuarios(panelUsuarios);

            // =========================
            // CONTROLADOR DE INICIO
            // =========================
            inicioControlador controladorInicio
                    = new inicioControlador(
                            inicio,
                            panelBuscarVuelo,
                            panelPasajeros,
                            daoAeropuerto,
                            daoVuelo,
                            login,
                            daoUsuario,
                            ventanaPrincipal
                    );

            // =========================
            // APLICAR DISEÑO A LOS PANELES
            // =========================
            // Al volver a la portada se limpian los formularios del proceso de reserva
            inicio.setAlVolverInicio(() -> {
                Tema.limpiar(panelBuscarVuelo);
                Tema.limpiar(panelPasajeros);
                controladorPasajero.reiniciarFormulario();
            });

            Tema.aplicar(panelPasajeros);
            Tema.aplicar(panelEquipaje);
            Tema.aplicar(panelAerolineas);
            Tema.aplicar(panelAeropuerto);
            Tema.aplicar(panelRuta);
            Tema.aplicar(panelAvion);
            Tema.aplicar(panelAsientos);
            Tema.aplicar(panelConfiguracion);
            Tema.aplicar(panelVuelo);
            Tema.aplicar(panelBuscarVuelo);
            Tema.aplicar(panelGestionPasajeros);
            Tema.aplicar(panelTiquete);
            Tema.aplicar(panelReservas);

            // =========================
            // MOSTRAR INICIO
            // =========================
            inicio.setLocationRelativeTo(null);
            inicio.setVisible(true);
        });
    }
}
