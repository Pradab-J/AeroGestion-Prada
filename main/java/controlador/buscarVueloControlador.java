/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package controlador;

import vista.Tema;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import modelo.DAO.aeropuertoDAO;
import modelo.DAO.rutaDAO;
import modelo.DAO.vueloDAO;
import modelo.entidad.aeropuerto;
import modelo.entidad.ruta;
import modelo.entidad.vuelo;

import vista.Inicio;
import vista.JpnlBuscarVuelo;
import vista.JpnlPasajeros;

/**
 * CONTROLADOR de la pantalla "Buscar vuelo" (paso 1 de la reserva): busca vuelos
 * por origen, destino y fecha, y al elegir uno avanza a los datos del pasajero.
 */
public class buscarVueloControlador implements ActionListener {

    private Inicio inicio;
    private JpnlBuscarVuelo vista;

    private aeropuertoDAO daoAeropuerto;
    private vueloDAO daoVuelo;

    private JpnlPasajeros panelPasajeros;

    // Guarda el vuelo seleccionado
    private int idVueloSeleccionado;

    // true cuando el pasajero ya hizo una búsqueda (para no borrarle el resultado al volver a esta pantalla)
    private boolean hayBusqueda = false;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    public buscarVueloControlador(
            Inicio inicio,
            JpnlBuscarVuelo vista,
            aeropuertoDAO daoAeropuerto,
            vueloDAO daoVuelo,
            JpnlPasajeros panelPasajeros) {

        this.inicio = inicio;
        this.vista = vista;
        this.daoAeropuerto = daoAeropuerto;
        this.daoVuelo = daoVuelo;
        this.panelPasajeros = panelPasajeros;

        cargarAeropuertos();
        mostrarTodos(); // al abrir, la tabla ya muestra todos los vuelos disponibles
        Tema.alMostrar(vista, () -> {
            cargarAeropuertos();
            if (!hayBusqueda) {
                mostrarTodos(); // recoge vuelos nuevos creados desde el panel administrativo
            }
        });

        vista.btnBuscar.addActionListener(this);
        vista.btnContinuar.addActionListener(this);
    }

    /** Llena los combos de origen y destino con los aeropuertos registrados. */
    private void cargarAeropuertos() {
        java.util.List<String> items = new java.util.ArrayList<>();
        for (aeropuerto a : daoAeropuerto.listar()) {
            items.add(a.getCiudad() + " - " + a.getNombre());
        }
        Tema.recargarCombo(vista.cmbOrigen, items);
        Tema.recargarCombo(vista.cmbDestino, items);
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vista.btnBuscar) {
            buscarVuelos();
        }

        if (e.getSource() == vista.btnContinuar) {
            seleccionarVuelo();
        }
    }

    /** Valida origen, destino y fecha, busca los vuelos y los muestra en la tabla. */
    private void buscarVuelos() {

        if (vista.cmbOrigen.getSelectedItem() == null
                || vista.cmbDestino.getSelectedItem() == null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione el origen y el destino."
            );

            return;
        }

        if (vista.dateFecha.getDate() == null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione una fecha."
            );

            return;
        }

        if (vista.dateFecha.getDate().toInstant().atZone(java.time.ZoneId.systemDefault())
                .toLocalDate().isBefore(java.time.LocalDate.now())) {
            JOptionPane.showMessageDialog(
                    vista,
                    "La fecha de salida no puede ser anterior a hoy."
            );
            return;
        }
        String origenCompleto =
                vista.cmbOrigen.getSelectedItem().toString();

        String destinoCompleto =
                vista.cmbDestino.getSelectedItem().toString();

        String origen =
                origenCompleto.split(" - ")[0];

        String destino =
                destinoCompleto.split(" - ")[0];

        if (origen.equalsIgnoreCase(destino)) {

            JOptionPane.showMessageDialog(
                    vista,
                    "El origen y el destino no pueden ser iguales."
            );

            return;
        }

        SimpleDateFormat formato =
                new SimpleDateFormat("yyyy-MM-dd");

        String fecha =
                formato.format(vista.dateFecha.getDate());

        ArrayList<vuelo> lista =
                daoVuelo.buscarVuelos(
                        origen,
                        destino,
                        fecha
                );

        hayBusqueda = true;
        cargarTabla(lista, true);
    }

    /** Muestra en la tabla todos los vuelos disponibles (sin filtrar por origen, destino ni fecha). */
    private void mostrarTodos() {
        cargarTabla(daoVuelo.listarDisponibles(), false);
    }

    /** Pinta la lista de vuelos en la tabla. Si avisarVacio es true y no hay vuelos, informa al usuario. */
    private void cargarTabla(ArrayList<vuelo> lista, boolean avisarVacio) {

        // La tabla no se puede editar: solo se selecciona una fila
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        // Nombre de la ruta (Origen - Destino) de cada vuelo
        java.util.Map<Integer, String> rutas = new java.util.HashMap<>();
        for (ruta r : new rutaDAO().listar()) {
            rutas.put(r.getId_ruta(), r.getOrigen() + " - " + r.getDestino());
        }

        modelo.addColumn("ID VUELO");
        modelo.addColumn("RUTA");
        modelo.addColumn("FECHA SALIDA");
        modelo.addColumn("FECHA LLEGADA");
        modelo.addColumn("HORA SALIDA");
        modelo.addColumn("HORA LLEGADA");
        modelo.addColumn("AVIÓN");
        modelo.addColumn("ESTADO");

        for (vuelo v : lista) {

            modelo.addRow(new Object[]{
                v.getId_vuelo(),
                rutas.getOrDefault(v.getFk_ruta(), ""),
                v.getFecha_salida(),
                v.getFecha_llegada(),
                v.getHora_salida(),
                v.getHora_llegada(),
                v.getFk_avion(),
                v.getEstado()
            });
        }

        vista.tblVuelos.setModel(modelo);

        if (lista.isEmpty() && avisarVacio) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se encontraron vuelos para los datos seleccionados."
            );
        }
    }

    /** Toma el vuelo elegido en la tabla y avanza al paso de datos del pasajero. */
    private void seleccionarVuelo() {

        int fila =
                vista.tblVuelos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    vista,
                    "Seleccione un vuelo de la tabla."
            );

            return;
        }

        // Guardar el ID del vuelo seleccionado
        idVueloSeleccionado =
                Integer.parseInt(
                        vista.tblVuelos
                                .getValueAt(fila, 0)
                                .toString()
                );

        // Ir al registro del pasajero
        mostrarPasajeros();
    }

    /** Muestra la pantalla de datos del pasajero. */
    private void mostrarPasajeros() {

        if (panelPasajeros == null) {

            JOptionPane.showMessageDialog(
                    vista,
                    "No se pudo abrir el registro del pasajero."
            );

            return;
        }

        // Cambiar el contenido de la ventana Inicio
        inicio.mostrarPanel(panelPasajeros);
    }

    /** Devuelve el id del vuelo elegido por el pasajero. */
    // Este método nos servirá después para continuar
    // con el asiento y la reserva.
    public int getIdVueloSeleccionado() {

        return idVueloSeleccionado;
    }
}
