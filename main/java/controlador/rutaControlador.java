/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import vista.Tema;
import modelo.DAO.integridadDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.DAO.aeropuertoDAO;
import modelo.DAO.rutaDAO;
import modelo.entidad.aeropuerto;
import modelo.entidad.ruta;
import vista.JpnlRuta;

/**
 * CONTROLADOR de Rutas: CRUD de rutas, validando que las ciudades existan en
 * un aeropuerto registrado y que no haya vuelos que dependan de la ruta al borrar.
 */
public class rutaControlador implements ActionListener {

    private JpnlRuta vistaRuta;
    private rutaDAO daoRuta;
    private aeropuertoDAO daoAeropuerto;

    /** Constructor: guarda la vista y los DAO y conecta los botones con sus acciones. */
    // =====================================================
    // CONSTRUCTOR
    // =====================================================
    public rutaControlador(
            JpnlRuta vista,
            rutaDAO daoRuta,
            aeropuertoDAO daoAeropuerto) {

        this.vistaRuta = vista;
        this.daoRuta = daoRuta;
        this.daoAeropuerto = daoAeropuerto;

        // BOTONES
        vistaRuta.btnGuardar.addActionListener(this);
        vistaRuta.btnEditar.addActionListener(this);
        vistaRuta.btnEliminar.addActionListener(this);
        vistaRuta.btnLimpiar.addActionListener(this);

        // =================================================
        // SELECCIONAR RUTA DE LA TABLA
        // =================================================
        vistaRuta.tblRuta.addMouseListener(
                new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {

                int fila = vistaRuta.tblRuta.getSelectedRow();

                if (fila >= 0) {

                    vistaRuta.txtIdRuta.setText(
                            vistaRuta.tblRuta
                                    .getValueAt(fila, 0)
                                    .toString()
                    );

                    vistaRuta.txtOrigen.setText(
                            vistaRuta.tblRuta
                                    .getValueAt(fila, 1)
                                    .toString()
                    );

                    vistaRuta.txtDestino.setText(
                            vistaRuta.tblRuta
                                    .getValueAt(fila, 2)
                                    .toString()
                    );

                    vistaRuta.txtDuracion.setText(
                            vistaRuta.tblRuta
                                    .getValueAt(fila, 3)
                                    .toString()
                    );

                    int idAeropuerto = Integer.parseInt(
                            vistaRuta.tblRuta
                                    .getValueAt(fila, 4)
                                    .toString()
                    );

                    seleccionarAeropuerto(idAeropuerto);
                }
            }
        }
        );

        // CARGAR DATOS
        cargarAeropuertos();
        Tema.alMostrar(vistaRuta, this::cargarAeropuertos);
        listarRutas();
    }

    /** Recibe los clics de los botones de la vista y llama a la acción que corresponde. */
    // =====================================================
    // ACTION PERFORMED
    // =====================================================
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == vistaRuta.btnGuardar) {
            guardarRuta();
        }

        if (e.getSource() == vistaRuta.btnEditar) {
            editarRuta();
        }

        if (e.getSource() == vistaRuta.btnEliminar) {
            eliminarRuta();
        }

        if (e.getSource() == vistaRuta.btnLimpiar) {
            limpiarCampos();
        }
    }

    /** Llena el combo de aeropuertos con los registrados. */
    // =====================================================
    // CARGAR AEROPUERTOS EN EL COMBO
    // =====================================================
    public void cargarAeropuertos() {
        java.util.List<String> items = new java.util.ArrayList<>();
        for (aeropuerto a : daoAeropuerto.listar()) {
            items.add(a.getId_aeropuerto() + " - " + a.getNombre());
        }
        Tema.recargarCombo(vistaRuta.cmbAeropuerto, items);
    }

    /** Selecciona en el combo el aeropuerto con ese id. */
    // =====================================================
    // SELECCIONAR AEROPUERTO
    // =====================================================
    private void seleccionarAeropuerto(int idAeropuerto) {

        for (int i = 0;
                i < vistaRuta.cmbAeropuerto.getItemCount();
                i++) {

            String item
                    = vistaRuta.cmbAeropuerto.getItemAt(i);

            if (item.startsWith(idAeropuerto + " -")) {

                vistaRuta.cmbAeropuerto.setSelectedIndex(i);

                break;
            }
        }
    }

    /** Devuelve el id del aeropuerto elegido en el combo (-1 si no hay). */
    // =====================================================
    // OBTENER ID DEL AEROPUERTO
    // =====================================================
    private int obtenerIdAeropuerto() {

        if (vistaRuta.cmbAeropuerto.getSelectedItem() == null) {
            return -1;
        }

        String seleccionado
                = vistaRuta.cmbAeropuerto
                        .getSelectedItem()
                        .toString();

        String id
                = seleccionado.split(" - ")[0];

        return Integer.parseInt(id);
    }

    /** Valida el formulario y guarda un nuevo registro de ruta; luego refresca la tabla y limpia el formulario. */
    // =====================================================
    // GUARDAR RUTA
    // =====================================================
    public void guardarRuta() {
        if (!validarRuta(false)) {
            return;
        }

        try {

            String origen
                    = vistaRuta.txtOrigen
                            .getText()
                            .trim();

            String destino
                    = vistaRuta.txtDestino
                            .getText()
                            .trim();

            String duracionTexto
                    = vistaRuta.txtDuracion
                            .getText()
                            .trim();

            // VALIDAR CAMPOS
            if (origen.isEmpty()
                    || destino.isEmpty()
                    || duracionTexto.isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Complete todos los campos"
                );

                return;
            }

            // VALIDAR AEROPUERTO
            if (vistaRuta.cmbAeropuerto.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione un aeropuerto"
                );

                return;
            }

            int duracion
                    = Integer.parseInt(duracionTexto);

            int idAeropuerto
                    = obtenerIdAeropuerto();

            if (idAeropuerto == -1) {

                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione un aeropuerto"
                );

                return;
            }

            ruta r = new ruta();

            r.setOrigen(origen);
            r.setDestino(destino);
            r.setDuracion(duracion);
            r.setFk_aeropuerto(idAeropuerto);

            if (daoRuta.guardar(r)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Ruta guardada correctamente"
                );

                listarRutas();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Error al guardar la ruta"
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "La duración debe ser un número"
            );
        }
    }

    /** Carga en la tabla los registros de ruta que hay en la base de datos. */
    // =====================================================
    // LISTAR RUTAS
    // =====================================================
    public void listarRutas() {

        ArrayList<ruta> lista
                = daoRuta.listar();

        DefaultTableModel modelo
                = new DefaultTableModel();

        modelo.addColumn("ID Ruta");
        modelo.addColumn("Origen");
        modelo.addColumn("Destino");
        modelo.addColumn("Duración");
        modelo.addColumn("ID Aeropuerto");

        for (ruta r : lista) {

            Object datos[] = {
                r.getId_ruta(),
                r.getOrigen(),
                r.getDestino(),
                r.getDuracion(),
                r.getFk_aeropuerto()
            };

            modelo.addRow(datos);
        }

        vistaRuta.tblRuta.setModel(modelo);
    }

    /** Valida y actualiza el registro de ruta seleccionado en la tabla. */
    // =====================================================
    // EDITAR RUTA
    // =====================================================
    public void editarRuta() {
        if (!validarRuta(true)) {
            return;
        }

        try {

            // VALIDAR ID
            if (vistaRuta.txtIdRuta
                    .getText()
                    .trim()
                    .isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione una ruta de la tabla"
                );

                return;
            }

            int id
                    = Integer.parseInt(
                            vistaRuta.txtIdRuta
                                    .getText()
                                    .trim()
                    );

            String origen
                    = vistaRuta.txtOrigen
                            .getText()
                            .trim();

            String destino
                    = vistaRuta.txtDestino
                            .getText()
                            .trim();

            String duracionTexto
                    = vistaRuta.txtDuracion
                            .getText()
                            .trim();

            // VALIDAR CAMPOS
            if (origen.isEmpty()
                    || destino.isEmpty()
                    || duracionTexto.isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Complete todos los campos"
                );

                return;
            }

            // VALIDAR COMBO
            if (vistaRuta.cmbAeropuerto.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione un aeropuerto"
                );

                return;
            }

            int duracion
                    = Integer.parseInt(duracionTexto);

            int idAeropuerto
                    = obtenerIdAeropuerto();

            if (idAeropuerto == -1) {

                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione un aeropuerto"
                );

                return;
            }

            ruta r = new ruta();

            r.setId_ruta(id);
            r.setOrigen(origen);
            r.setDestino(destino);
            r.setDuracion(duracion);
            r.setFk_aeropuerto(idAeropuerto);

            if (daoRuta.editar(r)) {

                JOptionPane.showMessageDialog(
                        null,
                        "Ruta actualizada correctamente"
                );

                listarRutas();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(
                        null,
                        "Error al actualizar la ruta"
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "La duración debe ser un número"
            );
        }
    }

    /** Elimina el registro de ruta seleccionado, tras pedir confirmación y comprobar que nada dependa de él. */
    // =====================================================
    // ELIMINAR RUTA
    // =====================================================
    public void eliminarRuta() {
        if (!puedeEliminarRuta()) {
            return;
        }

        try {

            if (vistaRuta.txtIdRuta
                    .getText()
                    .trim()
                    .isEmpty()) {

                JOptionPane.showMessageDialog(
                        null,
                        "Seleccione una ruta de la tabla"
                );

                return;
            }

            int id
                    = Integer.parseInt(
                            vistaRuta.txtIdRuta
                                    .getText()
                                    .trim()
                    );

            int respuesta
                    = JOptionPane.showConfirmDialog(
                            null,
                            "¿Está seguro de eliminar esta ruta?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION
                    );

            if (respuesta == JOptionPane.YES_OPTION) {

                if (daoRuta.eliminar(id)) {

                    JOptionPane.showMessageDialog(
                            null,
                            "Ruta eliminada correctamente"
                    );

                    listarRutas();
                    limpiarCampos();

                } else {

                    JOptionPane.showMessageDialog(
                            null,
                            "Error al eliminar la ruta"
                    );
                }
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    null,
                    "Seleccione una ruta para eliminar"
            );
        }
    }

    /** Vacía el formulario y quita la selección de la tabla. */
    // =====================================================
    // LIMPIAR CAMPOS
    // =====================================================
    public void limpiarCampos() {

        vistaRuta.txtIdRuta.setText("");
        vistaRuta.txtOrigen.setText("");
        vistaRuta.txtDestino.setText("");
        vistaRuta.txtDuracion.setText("");

        if (vistaRuta.cmbAeropuerto.getItemCount() > 0) {

            vistaRuta.cmbAeropuerto.setSelectedIndex(0);
        }

        vistaRuta.tblRuta.clearSelection();
    }

    // =========================================================
    // VALIDACIONES
    // =========================================================
    /** Devuelve el nombre de la ciudad tal como esta en un aeropuerto registrado (o null si no existe). */
    private String ciudadRegistrada(String texto) {
        String k = Validar.clave(texto);
        for (aeropuerto a : daoAeropuerto.listar()) {
            if (Validar.clave(a.getCiudad()).equals(k)) {
                return a.getCiudad();
            }
        }
        return null;
    }

    /** Revisa los campos del formulario; muestra el primer error y devuelve false si hay datos inválidos. */
    private boolean validarRuta(boolean edicion) {
        Integer id = null;
        if (edicion) {
            id = Validar.aEntero(vistaRuta.txtIdRuta.getText());
            if (id == null) {
                Validar.aviso(vistaRuta, "Seleccione una ruta de la tabla antes de editar.");
                return false;
            }
        }
        String origen = Validar.limpiar(vistaRuta.txtOrigen.getText());
        String destino = Validar.limpiar(vistaRuta.txtDestino.getText());
        String duracion = Validar.limpiar(vistaRuta.txtDuracion.getText());
        vistaRuta.txtDuracion.setText(duracion);

        String error = Validar.primero(
                Validar.texto("Origen", origen, 2, 60),
                Validar.texto("Destino", destino, 2, 60),
                Validar.entero("Duración", duracion, 1, 2000),
                Validar.seleccion("Aeropuerto", vistaRuta.cmbAeropuerto.getSelectedItem()));

        if (error == null) {
            // El pasajero busca por ciudad: origen y destino deben ser ciudades de aeropuertos registrados.
            String canO = ciudadRegistrada(origen);
            String canD = ciudadRegistrada(destino);
            if (canO == null) {
                error = "El origen «" + origen + "» no coincide con la ciudad de ningún aeropuerto registrado.\n"
                        + "Registre primero el aeropuerto o escriba la ciudad tal como aparece en él.";
            } else if (canD == null) {
                error = "El destino «" + destino + "» no coincide con la ciudad de ningún aeropuerto registrado.\n"
                        + "Registre primero el aeropuerto o escriba la ciudad tal como aparece en él.";
            } else if (Validar.clave(canO).equals(Validar.clave(canD))) {
                error = "El origen y el destino no pueden ser la misma ciudad.";
            } else {
                origen = canO;
                destino = canD;
                vistaRuta.txtOrigen.setText(origen);
                vistaRuta.txtDestino.setText(destino);
            }
        }

        if (error == null) {
            int idAeropuerto = obtenerIdAeropuerto();
            for (ruta r : daoRuta.listar()) {
                if ((id == null || r.getId_ruta() != id)
                        && Validar.clave(r.getOrigen()).equals(Validar.clave(origen))
                        && Validar.clave(r.getDestino()).equals(Validar.clave(destino))
                        && r.getFk_aeropuerto() == idAeropuerto) {
                    error = "Ya existe una ruta " + origen + " → " + destino + " con ese aeropuerto.";
                    break;
                }
            }
        }

        if (error == null && edicion) {
            for (ruta r : daoRuta.listar()) {
                if (r.getId_ruta() == id) {
                    boolean cambia = !Validar.clave(r.getOrigen()).equals(Validar.clave(origen))
                            || !Validar.clave(r.getDestino()).equals(Validar.clave(destino));
                    if (cambia && integridadDAO.contar("vuelo", "fk_ruta", id) > 0) {
                        error = "No se puede cambiar el origen ni el destino: esta ruta ya tiene vuelos.\n"
                                + "Cree una ruta nueva en su lugar.";
                    }
                    break;
                }
            }
        }
        return !Validar.mostrar(vistaRuta, error);
    }

    /** Comprueba que no existan registros que dependan de este ruta antes de borrarlo; avisa y devuelve false si los hay. */
    private boolean puedeEliminarRuta() {
        Integer id = Validar.aEntero(vistaRuta.txtIdRuta.getText());
        if (id == null) {
            return true; // el metodo original ya avisa que hay que seleccionar una fila
        }
        int vuelos = integridadDAO.contar("vuelo", "fk_ruta", id);
        if (vuelos > 0) {
            Validar.aviso(vistaRuta, "No se puede eliminar esta ruta porque tiene " + vuelos
                    + " vuelo(s) registrado(s).\nElimine primero esos vuelos.");
            return false;
        }
        return true;
    }
}
