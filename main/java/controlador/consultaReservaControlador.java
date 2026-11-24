package controlador;

import java.util.Locale;
import javax.swing.JOptionPane;
import modelo.DAO.consultaReservaDAO;
import vista.JfrmMisReservas;

/**
 * CONTROLADOR de "Mis reservas" (lado del pasajero): valida los datos escritos,
 * consulta la reserva en el DAO, arma el texto de detalles y cancela la reserva.
 */
public class consultaReservaControlador {

    private final JfrmMisReservas vista;
    private final consultaReservaDAO dao;

    private int idTiqueteActual = -1;
    private String documentoActual;
    private String codigoActual;

    /** Constructor: guarda la vista y el DAO y conecta los botones con sus acciones. */
    public consultaReservaControlador(JfrmMisReservas vista, consultaReservaDAO dao) {
        this.vista = vista;
        this.dao = dao;

        vista.btnConsultar.addActionListener(e -> consultarReserva());
        vista.btnCancelar.addActionListener(e -> cancelarReserva());
        vista.btnLimpiar.addActionListener(e -> limpiar());
    }

    /**
     * Busca la reserva con el documento y el código escritos, muestra sus detalles y
     * habilita "Cancelar reserva" si todavía no está cancelada.
     */
    public void consultarReserva() {
        String documento = vista.getDocumento();
        String codigo = vista.getCodigo();

        if (documento.isEmpty() || codigo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Ingresa el documento y el código de reserva.",
                    "Datos incompletos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Object[] datos = dao.consultarReserva(documento, codigo);

            if (datos == null) {
                idTiqueteActual = -1;
                vista.setCancelarHabilitado(false);
                vista.mostrarDetalles("No se encontró una reserva con esos datos.");
                JOptionPane.showMessageDialog(vista, "No se encontró la reserva.");
                return;
            }

            idTiqueteActual = Integer.parseInt(datos[0].toString());
            documentoActual = documento;
            codigoActual = codigo;

            String estado = datos[11] == null
                    ? "Sin estado" : datos[11].toString();

            vista.mostrarDetalles(
                    "CÓDIGO DE RESERVA: " + datos[1]
                    + "\nFecha de emisión: " + datos[2]
                    + "\nDocumento: " + datos[3]
                    + "\nPasajero: " + datos[4]
                    + "\nVuelo: " + datos[5]
                    + "\nAsiento: " + datos[6]
                    + "\nEquipaje (ID): " + datos[7]
                    + "\nPrecio del asiento: " + dinero(datos[8])
                    + "\nPrecio del equipaje: " + dinero(datos[9])
                    + "\nTOTAL: " + dinero(datos[10])
                    + "\nEstado: " + estado);

            vista.setCancelarHabilitado(!estado.equalsIgnoreCase("Cancelada"));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo consultar la reserva:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Pide confirmación y cancela la reserva consultada (libera el asiento); luego vuelve a consultarla. */
    public void cancelarReserva() {
        if (idTiqueteActual == -1) {
            JOptionPane.showMessageDialog(vista, "Primero consulta una reserva.");
            return;
        }

        int respuesta = JOptionPane.showConfirmDialog(
                vista,
                "¿Estás seguro de que deseas cancelar esta reserva?",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean cancelada = dao.cancelarReserva(
                    idTiqueteActual, documentoActual, codigoActual);

            if (cancelada) {
                JOptionPane.showMessageDialog(
                        vista, "La reserva fue cancelada correctamente.");
            } else {
                JOptionPane.showMessageDialog(
                        vista,
                        "No se pudo cancelar. Puede que ya esté cancelada.");
            }
            consultarReserva();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Error al cancelar la reserva:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Vacía los campos y reinicia el estado de la consulta. */
    public void limpiar() {
        vista.limpiarCampos();

        idTiqueteActual = -1;
        documentoActual = null;
        codigoActual = null;
    }

    /** Da formato de dinero a un valor. */
    private String dinero(Object valor) {
        if (valor == null) {
            return "$0.00";
        }

        try {
            double cantidad = Double.parseDouble(valor.toString());
            return String.format(Locale.US, "$%,.2f", cantidad);
        } catch (NumberFormatException ex) {
            return "$0.00";
        }
    }
}
