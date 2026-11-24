/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */





package controlador;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import modelo.DAO.reservaDAO;
import vista.JpnlReservas;

/** CONTROLADOR de Reservas (administrador): carga la lista, busca por código o documento y cancela reservas. */
public class reservaControlador {

    private final JpnlReservas vista;
    private final reservaDAO dao;

    /** Constructor: guarda la vista y el DAO, conecta los botones con sus acciones y carga la tabla. */
    public reservaControlador(JpnlReservas vista, reservaDAO dao) {
        this.vista = vista;
        this.dao = dao;

        vista.btnBuscar.addActionListener(e -> buscarReserva(vista.getTextoBusqueda()));
        vista.txtBuscar.addActionListener(e -> buscarReserva(vista.getTextoBusqueda()));
        vista.btnLimpiar.addActionListener(e -> {
            vista.limpiarBusqueda();
            cargarReservas();
        });
        vista.btnCancelar.addActionListener(e -> cancelarSeleccionada());

        cargarReservas();
    }

    public void cargarReservas() {
        try {
            List<Object[]> reservas = dao.listarReservas();
            vista.mostrarReservas(reservas);

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudieron cargar las reservas:\n"
                    + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void buscarReserva(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            cargarReservas();
            return;
        }

        try {
            List<Object[]> reservas = dao.listarReservas();
            List<Object[]> resultados = new ArrayList<>();

            String busqueda = texto.trim().toLowerCase();

            for (Object[] reserva : reservas) {
                String codigo =
                        String.valueOf(reserva[0]).toLowerCase();

                String documento =
                        String.valueOf(reserva[2]).toLowerCase();

                if (codigo.contains(busqueda)
                        || documento.contains(busqueda)) {
                    resultados.add(reserva);
                }
            }

            vista.mostrarReservas(resultados);

            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(
                        vista,
                        "No se encontraron reservas con ese código "
                        + "o documento."
                );
            }

        } catch (Exception e) {
            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    vista,
                    "Error al buscar reservas:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /** Cancela la reserva seleccionada en la tabla (pide confirmación y libera el asiento). */
    public void cancelarSeleccionada() {
        String[] seleccion = vista.getReservaSeleccionada();
        if (seleccion == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione una reserva de la tabla.");
            return;
        }

        String codigo = seleccion[0];
        String estado = seleccion[1];

        if ("Cancelada".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(vista, "Esa reserva ya está cancelada.");
            return;
        }

        int r = JOptionPane.showConfirmDialog(vista,
                "¿Cancelar la reserva " + codigo + "?\nEl asiento quedará disponible.",
                "Confirmar cancelación", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (r != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            if (dao.cancelarPorCodigo(codigo)) {
                JOptionPane.showMessageDialog(vista, "Reserva cancelada correctamente.");
            } else {
                JOptionPane.showMessageDialog(vista,
                        "No se pudo cancelar. Puede que ya esté cancelada.");
            }
            cargarReservas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
